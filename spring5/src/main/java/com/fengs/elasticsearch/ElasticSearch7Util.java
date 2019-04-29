package com.fengs.elasticsearch;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.Token;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.nlpcn.es4sql.domain.Where;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.parse.ElasticSqlExprParser;
import org.nlpcn.es4sql.parse.SqlParser;
import org.nlpcn.es4sql.parse.WhereParser;
import org.nlpcn.es4sql.query.maker.QueryMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * elasticsearch7.x版本数据获取
 * @data 2019年4月29日09:40:09
 * @author fengs
 */
public class ElasticSearch7Util {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearch7Util.class);

    /**
     * 构建查询构造器
     * @param indexName  索引名
     * @param whereExpress  查询条件:(f1=2 and f2=1) or (f3=1 and f4=1)
     * @return
     */
    public static BoolQueryBuilder createQueryBuilderByWhere(String indexName, String whereExpress) {
        BoolQueryBuilder boolQuery = null;

        try {
            String sql = "select * from " + indexName;
            String whereTemp = "";
            if (StringUtils.isNotBlank(whereExpress)) {
                whereTemp = " where 1=1 " + whereExpress;
            }
            SQLQueryExpr sqlExpr = (SQLQueryExpr) toSqlExpr(sql + whereTemp);
            SqlParser sqlParser = new SqlParser();
            MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) sqlExpr.getSubQuery().getQuery();
            WhereParser whereParser = new WhereParser(sqlParser, query);
            Where where = whereParser.findWhere();
            if (where != null) {
                boolQuery = QueryMaker.explan(where);
            }
        } catch (SqlParseException e) {
            logger.info("ReadES.createQueryBuilderByExpress-Exception,"+e.getMessage());
            e.printStackTrace();
        }
        return boolQuery;
    }

    /**
     * 验证sql
     *
     * @param sql sql查询语句
     * @return and (a=1 and b=1) or (c=1 and d=1)
     */
    private static SQLExpr toSqlExpr(String sql) {
        SQLExprParser parser = new ElasticSqlExprParser(sql);
        SQLExpr expr = parser.expr();

        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql);
        }
        return expr;
    }

    /**
     * 查询指定索引下的信息
     * @param indexName 索引名称
     * @param condition 查询条件
     */
    public static List<Map<String,Object>> queryIndexContent(String indexName, String condition) throws IOException {
        //实例化查询请求对象
        SearchRequest request = new SearchRequest();
        //构建查询客户端
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.12.128", 9200, "http")));//初始化
        //实例化SearchSourceBuilder
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        //根据索引、查询条件构建查询构造器
        BoolQueryBuilder boolQueryBuilder = createQueryBuilderByWhere(indexName,condition);
        //将查询构造器注入SearchSourceBuilder
        searchBuilder.query(boolQueryBuilder);
        //设置请求查询的索引（查询构造器中已指定，无需重复设置）
        //request.indices(indexName);
        //将构建好的SearchSourceBuilder注入请求
        request.source(searchBuilder);

        //带入请求执行查询
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        //得到查询结果
        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();

        List<Map<String,Object>> listData = new ArrayList<>();
        //遍历查询结果
        for(SearchHit hit : searchHits){
            Map<String,Object> datas = hit.getSourceAsMap();
            listData.add(datas);
            logger.info(datas.toString());
        }
        //关闭客户端连接
        client.close();

        return listData;
    }

    public static void main(String[] args) {
        try {
            queryIndexContent("user_site"," and phone_no in('12234567890') ");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

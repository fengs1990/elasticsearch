package com.fengs.elasticsearch;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.Token;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.nlpcn.es4sql.domain.Where;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.parse.ElasticSqlExprParser;
import org.nlpcn.es4sql.parse.SqlParser;
import org.nlpcn.es4sql.parse.WhereParser;
import org.nlpcn.es4sql.query.maker.QueryMaker;

import java.net.InetAddress;

public class ElasticSearch6Util {

    /**
     *
     * @param indexName  索引名
     * @param whereExpress  查询条件:(f1=2 and f2=1) or (f3=1 and f4=1)
     * @return
     */
    public static QueryBuilder createQueryBuilderByWhere(String indexName, String whereExpress) {
        BoolQueryBuilder boolQuery = null;

        try {
            String sql = "select * from " + indexName;
            String whereTemp = "";
            if (StringUtils.isNotBlank(whereExpress)) {
                whereTemp = " where " + whereExpress;
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
            System.out.println("ReadES.createQueryBuilderByExpress-Exception,"+e.getMessage());
            e.printStackTrace();
        }
        return boolQuery;
    }

    /**
     * 验证sql
     *
     * @param sql sql查询语句
     * @return
     */
    private static SQLExpr toSqlExpr(String sql) {
        SQLExprParser parser = new ElasticSqlExprParser(sql);
        SQLExpr expr = parser.expr();

        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql);
        }
        return expr;
    }

    public static long searchTotalByApi(String indexName, String whereExpress) {
//        try {
//            // 获取Elasticsearch的服务
//            Settings setting = Settings.builder().put("cluster.name", "node-1")
//                    .put("client.transport.sniff", false).build();
//            TransportClient client = new PreBuiltTransportClient(setting)
//                    .addTransportAddress(new TransportAddress(InetAddress.getByName("vm1"),9300));
//            System.out.println("success to connect escluster");
//
//            // 转换Elasticsearch格式的查询条件
//            QueryBuilder queryBuilder = createQueryBuilderByWhere(indexName, whereExpress);
//            // 减少资源消耗，只查询总数
//            long resultNum = client.prepareSearch(indexName).setQuery(queryBuilder).setFrom(0).setSize(0).execute()
//                    .actionGet().getHits().getTotalHits();
//
//            SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).execute()
//                    .actionGet();
//            for(SearchHit hit: response.getHits().getHits()) {
//                System.out.println(hit.getId());
//                System.out.println("source: " + hit.getSourceAsString());
//            }
//
//            for(SearchHit hit: response.getHits().getHits()) {
//                System.out.println(hit.getId());
//                if (hit.getFields().containsKey("title")) {
//                    System.out.println("field.title: "+ hit.getFields().get("title").getValue());
//                }
//                System.out.println("source.title: " + hit.getSource().get("title"));
//            }
//
//            if (0 == resultNum) {
//                System.out.println("ReadES.seatchTotalByApi-queryBuilder:"+queryBuilder);
//            }
//            return resultNum;
//        } catch (Exception e) {
//            System.out.println("ReadES.seatchTotalByApi-Exception:"+e.getMessage());
//            e.printStackTrace();
//        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(searchTotalByApi("user_site","phone_no in('12234567890')"));
    }
}

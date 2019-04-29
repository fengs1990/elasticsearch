package com.fengs.quartz;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class RemoteShellExecutor {
    private Connection conn;
    private String ip;
    private String username;
    private String password;
    private String charset = Charset.defaultCharset().toString();
    private static final int TIME_OUT = 0;// 表示不超时

    /**
     * 构造函数
     *
     * @param ip       远程ip
     * @param username 远程机器用户名
     * @param password 远程机器密码
     */
    public RemoteShellExecutor(String ip, String username, String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
    }


    /**
     * 登录
     *
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(username, password);
    }

    /**
     * 执行脚本
     *
     * @param shell
     * @return
     * @throws Exception
     */
    public int exec(String shell) throws Exception {
        int ret = -1;
        try {
            if (login()) {
                Session session = conn.openSession();
                session.execCommand(shell);
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                InputStream in = session.getStdout();
                ret = session.getExitStatus();

                System.err.println("1------"+processStdout(in,charset));
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return ret;
    }

    public String processStdout(InputStream in, String charset) {
        byte[] buf = new byte[1024];
        StringBuffer sb = new StringBuffer();
        try {
            while (in.read(buf) != -1) {
                sb.append(new String(buf, charset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            RemoteShellExecutor executor = new RemoteShellExecutor("192.168.12.128", "root", "123456");
            executor.exec("sh /usr/local/fengs/mkdir.sh");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

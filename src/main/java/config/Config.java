package config;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Config {

    public static final String LOCALHOST;

    static {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        LOCALHOST = addr.getHostAddress();
    }

    // linux config
    public static final String LINUX_USER_NAME = "root";
    public static final String LINUX_USER_PASSWORD = "123456";

    // sqoop config
    public static final String SQOOP_HOME = "/training/sqoop-1.4.6/bin/sqoop";

    // zookeeper config
    public static final String ZOOKEEPER_QUOEUM = "niit";
    public static final String NULLVALUE = "N/A";

    // hdfs config
    // public static final String TMP = "src/main/java/utils/tmp/";
    public static final String HDFS_URI_LINE = "hdfs://niit:8020";
    public static final String HDFS_USER = "root";
    public static final String PATH = "/";

    // hive config
    public static final String HIVE_DATABASE = "niit";

    // jdbc config
    public static final String JDBC = "com.mysql.jdbc.Driver";
    public static final String DATABASE_NAME = "niit";
    // 默认JDBC使用本地LOCALHOST作为MySQL服务器
    public static final String JDBC_URL = "jdbc:mysql://" + LOCALHOST + "/" + DATABASE_NAME +
            "?serverTimezone=Asia/Shanghai&characterEncoding=utf-8&useSSL=false&rewriteBatchedStatements=true";
    public static final String DATABASE_USER = "root";
    public static final String DATABASE_PASSWORD = "niit1234";
    public static final int BATCH_LINE = 50;
    public static final int MAX_LINE = 100;
    public static final String TABLE_NAME = "imdb_movies";

    // http config
    public static final int MAX_CONNECTION_TIMEOUT = 3 * 1000;
    public static final int MAX_SO_TIMEOUT = 3*60*1000;


    // web config
    public static final String WEB_URL_BEGIN = "http://localhost:8080/Group1Project/";
    public static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;
    public static final int MAX_FILE_SIZE = 1024 * 1024 * 50;
    public static final int MAX_REQUEST_SIZE = 1024 * 1024 * 60;
    public static final int MAX_RETRY_TIME = 3;


}

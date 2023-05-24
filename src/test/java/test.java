import com.alibaba.fastjson.JSON;
import com.carrotsearch.sizeof.RamUsageEstimator;
import model.Result;
import utils.HttpUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static config.Config.*;
import static config.Config.FLUME_URL;
import static utils.MySqlUtils.createConnection;

public class test {
    private static final String URL = WEB_URL_BEGIN + "analyze/flume_mysql_to_hdfs.jsp";
    static int httpTime = 0;
    static long httpSize = 0;
    static int idx = 1;
    static ResultSet rs;
    static String log = "Transferred 1,001.8311 MB in 33.9232 seconds (29.5323 KB/sec)";

    public static void main(String[] args) throws IOException {
        Matcher tran = Pattern.compile("Transferred ((\\d+,)*\\d+.\\d+) ([KM])B in ((\\d+,)*\\d+.\\d+) seconds").matcher(log);
        if (tran.find() == true) {
            Pattern pattern = Pattern.compile(","); //去掉空格符合换行符
            System.out.println(pattern.matcher(tran.group(1)).replaceAll(""));
            System.out.println(tran.group(3));
            if ("M".equals(tran.group(3))){
                double size = Double.parseDouble(pattern.matcher(tran.group(1)).replaceAll(""));
                System.out.println(size * 1024);
            }
            System.out.println(pattern.matcher(tran.group(4)).replaceAll(""));
        }
    }

}

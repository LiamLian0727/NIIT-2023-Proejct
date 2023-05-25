package utils;

import com.carrotsearch.sizeof.RamUsageEstimator;
import model.Result;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

import static config.Config.*;

public class FlumeUtils {

    static String line = "";
    static String str = "";
    static Pattern pattern = Pattern.compile("\"");

    public static Result putFilesInToHDFS(InputStream fin, String fileName) throws SQLException, ClassNotFoundException, IOException {
        System.out.println(fileName);

        Result result = new Result();
        HashMap<String, String> headers = new HashMap<>(3);
        headers.put("content-type", "text/plain");
        int httpTime = 0;
        int idx = 0;
        long httpSize = 0;

        long startTime = System.currentTimeMillis();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fin))) {
            while ((line = reader.readLine()) != null) {
                str += line + "\n";
                str = pattern.matcher(str).replaceAll("'");
                idx += 1;
                if (idx % HTTP_MAX_UPLODING_EVENT == 0) {
                    httpTime++;
                    httpSize += RamUsageEstimator.sizeOf(str);
                    HttpUtils.sendPostWithJson(FLUME_URL, "[{\"body\" : \"" + str + "\"}]", headers);
                    str = "";

                }
            }
            if (idx % HTTP_MAX_UPLODING_EVENT != 0) {
                httpTime++;
                httpSize += RamUsageEstimator.sizeOf(str);
                HttpUtils.sendPostWithJson(FLUME_URL, "[{\"body\" : \"" + str + "\"}]", headers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

        result.setCount(String.valueOf(idx));
        result.setTime(String.valueOf(((double) endTime - startTime) / 1000)); // ms
        result.setSize(String.valueOf((double) httpSize / (2 * 1024 * 1024))); // MB
        result.setRetry(String.valueOf(httpTime));
        return result;
    }

    public static void main(String[] args) {
        File src = new File("D:\\work\\NIIT Project\\now\\Group1Project\\dataset\\Zookeeper.log");
        InputStream is = null;
        try {
            is = new FileInputStream(src);
            putFilesInToHDFS(is, "Zookeeper.log");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

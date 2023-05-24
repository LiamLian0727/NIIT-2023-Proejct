package utils;

import com.carrotsearch.sizeof.RamUsageEstimator;
import model.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;

import static config.Config.*;

public class FlumeUtils {

    static String line = "";
    static String str = "";

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

    }
}

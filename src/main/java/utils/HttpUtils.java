package utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import static config.Config.*;


public class HttpUtils {
    public static String sendPostWithJson(String url, String jsonStr, HashMap<String,String> headers) {
        // 返回的结果
        String jsonResult = "";
        try {
            HttpClient client = new HttpClient();
            // 连接超时
            client.getHttpConnectionManager().getParams().setConnectionTimeout(MAX_CONNECTION_TIMEOUT);
            // 读取数据超时
            client.getHttpConnectionManager().getParams().setSoTimeout(MAX_SO_TIMEOUT);
            client.getParams().setContentCharset("UTF-8");
            PostMethod postMethod = new PostMethod(url);

            postMethod.setRequestHeader("content-type", headers.get("content-type"));

            // 非空
            if (null != jsonStr && !"".equals(jsonStr)) {
                StringRequestEntity requestEntity = new StringRequestEntity(jsonStr, headers.get("content-type"), "UTF-8");
                postMethod.setRequestEntity(requestEntity);
            }
            int status = client.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                InputStream inputStream = postMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                jsonResult = stringBuffer.toString();
            } else {
                throw new RuntimeException("接口连接失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("接口连接失败！");
        }
        return jsonResult;
    }

    public static void main(String[] args) {

        HashMap<String, String> headers = new HashMap<>(3);
        String requestUrl = "http://niit:22222";
        String jsonStr = "[{\"body\" : \"Hello, this is a http post request test\"}]";
        headers.put("content-type", "text/plain");
        // 发送post请求
        String resultData = HttpUtils.sendPostWithJson(requestUrl, jsonStr,headers);
        // 并接收返回结果
        System.out.println(resultData);
    }
}

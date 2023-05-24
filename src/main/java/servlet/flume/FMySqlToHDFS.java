package servlet.flume;

import com.alibaba.fastjson.JSON;
import com.carrotsearch.sizeof.RamUsageEstimator;
import model.Result;
import utils.HttpUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static config.Config.*;
import static config.Config.HTTP_MAX_UPLODING_EVENT;
import static utils.MySqlUtils.createConnection;

@WebServlet(urlPatterns = "/FMySqlToHDFS")
public class FMySqlToHDFS extends HttpServlet {
    private static final String URL = WEB_URL_BEGIN + "analyze/flume_mysql_to_hdfs.jsp";
    Result result = new Result();
    ResultSet rs;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int httpTime = 0;
        long httpSize = 0;
        int idx = 0;
        HttpSession session = request.getSession();
        String fieldsTerminated = request.getParameter("fields_terminated");
        HashMap<String, String> headers = new HashMap<>(1);
        headers.put("content-type", "text/plain");
        long startTime = System.currentTimeMillis();
        try (Connection con = createConnection();
             Statement stmt = con.createStatement()){
            rs = stmt.executeQuery("select * from " + TABLE_NAME);
            int columnCount = rs.getMetaData().getColumnCount();
            List list = new ArrayList<Map<String, String>>();
            while (rs.next()) {
                Map rowData = new HashMap<String, String>();
                String data = "";
                for (int i = 1; i < columnCount; i++) {
                    data += rs.getString(i) + fieldsTerminated;
                }
                data += rs.getObject(columnCount);
                rowData.put("body", data);
                list.add(rowData);
                idx ++;
                if(idx % HTTP_MAX_UPLODING_EVENT == 0){
                    String str = JSON.toJSONString(list);
                    System.out.println(str);
                    String resultData = HttpUtils.sendPostWithJson(FLUME_URL, str, headers);
                    System.out.println(resultData);
                    httpTime ++;
                    httpSize += RamUsageEstimator.sizeOf(str);
                }
            }
            if(idx % HTTP_MAX_UPLODING_EVENT != 0){
                String str = JSON.toJSONString(list);
                System.out.println(str);
                String resultData = HttpUtils.sendPostWithJson(FLUME_URL, str, headers);
                httpTime ++;
                httpSize += RamUsageEstimator.sizeOf(str);
                System.out.println(resultData);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

        session.setAttribute("Count", idx);
        session.setAttribute("Time", ((double)endTime - startTime) / 1000); // ms
        session.setAttribute("Size", (double)httpSize / (2 * 1024 * 1024)); // MB
        session.setAttribute("Retry", httpTime);
        response.sendRedirect(URL + "?status=true");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

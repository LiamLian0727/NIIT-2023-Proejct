package servlet.sqoop;

import model.Result;
import utils.SqoopUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static config.Config.*;

@WebServlet(urlPatterns = "/MySqlToHive")
public class MySqlToHive extends HttpServlet {
    static final String URL = WEB_URL_BEGIN + "analyze/sqoop_mysql_to_hive.jsp";
    int retryTime = 0;
    Result result = new Result();
    Pattern pattern = Pattern.compile(",");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hiveTable = request.getParameter("target_table");
        String fieldsTerminated = request.getParameter("fields_terminated");
        String linesTerminated = request.getParameter("lines_terminated");
        int mapNum = Integer.parseInt(request.getParameter("map_num"));
        String customParameters = request.getParameter("custom_parameters");
        HttpSession session = request.getSession();
        String sqoopCommand = SQOOP_HOME + " import --connect " + JDBC_URL +
                " --username " + DATABASE_USER +
                " --password " + DATABASE_PASSWORD +
                " --table " + TABLE_NAME +
                " --split-by imdb_title_id" +
                " --m " + mapNum +
                " --hive-import" +
//                " --create-hive-table" +
                " --hive-table " + HIVE_DATABASE + "." + hiveTable +
                " --fields-terminated-by '" + fieldsTerminated + "'" +
                " --lines-terminated-by '" + linesTerminated + "'" +
                " --delete-target-dir --hive-overwrite";

        if (!"".equals(customParameters)) {
            sqoopCommand = sqoopCommand + " " + customParameters;
        }
        sqoopCommand = sqoopCommand.replaceAll("&", "\\\\&");
        System.out.println("---------------------------begin---------------------------");
        while (retryTime <= MAX_RETRY_TIME) {
            System.out.println("run: " + sqoopCommand);
            String log = SqoopUtils.sqoopExec(sqoopCommand);
            Matcher flag = Pattern.compile("successfully").matcher(log);
            if (flag.find()) {
                Matcher tran = Pattern.compile(REX_TIME_AND_SIZE).matcher(log);
                if (tran.find() == true) {
                    double size = Double.parseDouble(pattern.matcher(tran.group(1)).replaceAll(""));
                    if ("M".equals(tran.group(3))){
                        size *= 1024;
                    }
                    result.setSize(String.valueOf(size));
                    result.setTime(pattern.matcher(tran.group(4)).replaceAll(""));
                }
                Matcher time = Pattern.compile(REX_TIME).matcher(log);
                int matcher_start = 0;
                while (time.find(matcher_start)) {
                    result.setTime(
                            String.valueOf(
                                    Double.parseDouble(pattern.matcher(tran.group(1)).replaceAll(""))
                                            + Double.parseDouble(result.getTime())
                            )
                    );
                    matcher_start = time.end();
                }
                Matcher m = Pattern.compile(REX_RETRY).matcher(log);
                if (m.find() == true) {
                    result.setCount(m.group(1));
                }
                result.setRetry(String.valueOf(retryTime));
                break;
            } else {
                retryTime++;
            }
        }
        System.out.println("----------------------------end----------------------------");
        System.out.println(result);
        session.setAttribute("Count", result.getCount());
        session.setAttribute("Time", result.getTime()); // second
        session.setAttribute("Size", result.getSize()); // KB
        session.setAttribute("Retry", result.getRetry());
        response.sendRedirect(URL + "?status=true");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

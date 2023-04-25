package servlet.sqoop;

import utils.SqoopUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static config.Config.*;
import static config.Config.DATABASE_PASSWORD;

@WebServlet(urlPatterns = "/MySqlToHDFS")
public class MySqlToHDFS extends HttpServlet {
    static final String URL = WEB_URL_BEGIN + "analyze/hdfs_result.jsp";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String targetDir = request.getParameter("target_dir");
        String fieldsTerminated = request.getParameter("fields_terminated");
        String linesTerminated = request.getParameter("lines_terminated");
        int mapNum = Integer.parseInt(request.getParameter("map_num"));
        String customParameters = request.getParameter("custom_parameters");

        String sqoopCommand = SQOOP_HOME + " import --connect " + JDBC_URL +
                " --username " + DATABASE_USER +
                " --password " + DATABASE_PASSWORD +
                " --query 'select * from user where $CONDITIONS'" +
                " --m " + mapNum +
                " --split-by UserName" +
                " --target-dir " + targetDir +
                " --fields-terminated-by '" + fieldsTerminated + "'" +
                " --lines-terminated-by '" + linesTerminated + "'"; // 要执行的 Sqoop 命令，根据需要自行更改
        if (!"".equals(customParameters)) {
            sqoopCommand = sqoopCommand + " " + customParameters;
        }
        sqoopCommand = sqoopCommand.replaceAll("&", "\\\\&");
        System.out.println("---------------------------begin---------------------------");
        System.out.println("run: " + sqoopCommand);
        SqoopUtils.sqoopExec(sqoopCommand);
        System.out.println("----------------------------end----------------------------");
        response.sendRedirect(URL + "?status=true");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

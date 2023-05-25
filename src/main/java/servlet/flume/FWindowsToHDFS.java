package servlet.flume;

import model.Result;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.FlumeUtils;
import utils.MySqlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static config.Config.*;

@WebServlet(urlPatterns = "/FWindowToHDFS")
@MultipartConfig
public class FWindowsToHDFS extends HttpServlet {
    private static final String URL = WEB_URL_BEGIN + "analyze/flume_window_to_hdfs.jsp";
    private static final String KV[] = {"fup", "success"};

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long event = 0, httpPost = 0;
        double time = 0, size = 0;
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        List<Result> results = new ArrayList<>();
        Collection<Part> parts = request.getParts();

        for (Part p : parts) {
            if(p.getSubmittedFileName()!=null){
                try {
                    results.add(FlumeUtils.putFilesInToHDFS(p.getInputStream(), p.getSubmittedFileName()));
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        for (Result result : results) {
            System.out.println(result);
            event += Long.parseLong(result.getCount());
            httpPost += Long.parseLong(result.getRetry());
            time += Double.parseDouble(result.getTime());
            size += Double.parseDouble(result.getSize());
        }

        session.setAttribute("Count", event);
        session.setAttribute("Time", time); // second
        session.setAttribute("Size", size); // KB
        session.setAttribute("Retry", httpPost);
        response.sendRedirect(URL + "?status=true");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

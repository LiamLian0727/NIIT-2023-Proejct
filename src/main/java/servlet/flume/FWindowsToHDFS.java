package servlet.flume;

import model.Result;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.FlumeUtils;
import utils.MySqlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static config.Config.*;

@WebServlet(urlPatterns = "/FWindowToHDFS")
public class FWindowsToHDFS extends HttpServlet {
    private static final String URL = WEB_URL_BEGIN + "analyze/flume_window_to_hdfs.jsp";
    Result result = new Result();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (!ServletFileUpload.isMultipartContent(request)) { // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: form must have enctype=multipart/form-data");
            writer.flush();
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        upload.setHeaderEncoding("UTF-8");

        try {
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        result = FlumeUtils.putFilesInToHDFS(item.getInputStream(), fileName);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

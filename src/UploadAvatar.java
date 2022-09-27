import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadAvatar extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "avatar";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 1; // 1MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 3; // 3MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 4; // 4MB

    /**
     * 上传数据及保存文件
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("UTF-8");

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = request.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;


        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件的上传路径
                        System.out.println(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                        request.setAttribute("message",
                                "文件上传成功!");

                        // 数据库相关
                        Connection conn = null;
                        Statement stmt = null;

                        // JDBC 驱动名及数据库 URL
                        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
                        String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                        // String DB_URL = "jdbc:mysql://localhost:3306/servlet?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

                        // 数据库的用户名与密码，需要根据自己的设置
                        String USER = "";
                        String PASS = "";

                        try {
                            // 注册 JDBC 驱动器
                            Class.forName("com.mysql.jdbc.Driver");

                            // 打开一个连接
                            conn = DriverManager.getConnection(DB_URL, USER, PASS);

                            // 执行 SQL 查询
                            stmt = conn.createStatement();
                            String sql;
                            sql = "update users set avatar = '" + "http://localhost:8080/Questionnaire_war_exploded/avatar/" + fileName + "' where username = '" + username + "'";
                            // sql = "update users set avatar = '" + "http://qiujianhui.cn:8080/avatar/" + fileName + "' where username = '" + username + "'";
                            stmt.executeUpdate(sql);

                        } catch (SQLException se) {
                            // 处理 JDBC 错误
                            se.printStackTrace();
                        } catch (Exception e) {
                            // 处理 Class.forName 错误
                            e.printStackTrace();
                        } finally {
                            // 最后是用于关闭资源的块
                            try {
                                if (stmt != null)
                                    stmt.close();
                            } catch (SQLException se2) {
                            }
                            try {
                                if (conn != null)
                                    conn.close();
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        }
                    }
                }
            }


        } catch (Exception ex) {
            request.setAttribute("message",
                    "错误信息: " + ex.getMessage());
        }
        // 跳转到 message.jsp
        request.getServletContext().getRequestDispatcher("/index.jsp").forward(
                request, response);
    }
}
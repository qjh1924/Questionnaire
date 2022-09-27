// 注册
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.PrintWriter;
import java.sql.*;

public class SignUp extends HttpServlet {
    public SignUp() {
        super();
    }

    // 处理 POST 方法请求的方法
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String docType = "<!DOCTYPE html>\n";

        if (!password.equals(confirmPassword)) { // 检查两次输入密码是否一致
            out.println(docType +
                    "<html>\n" +
                    "<head><title>Error</title></head>\n" +
                    "<body bgcolor=\"#f0f0f0\">\n" +
                    "<h2 align=\"center\">两次密码输入不一致，请返回重新填写！</h2>\n" +
                    "<a href=\"signup.jsp\"><button>立即返回</button></a>\n" +
                    "</body></html>");
        } else if (password == "") { // 检查密码是否为空
            out.println(docType +
                    "<html>\n" +
                    "<head><title>Error</title></head>\n" +
                    "<body bgcolor=\"#f0f0f0\">\n" +
                    "<h2 align=\"center\">密码为空，请返回重新填写！</h2>\n" +
                    "<a href=\"signup.jsp\"><button>立即返回</button></a>\n" +
                    "</body></html>");
        } else { // 注册信息到数据库

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
                sql = "Insert into users (username, password) values ('" + username + "', '" + password + "')";
                stmt.executeUpdate(sql);

                out.println(docType +
                        "<html>\n" +
                        "<head><title>Success!</title></head>\n" +
                        "<body bgcolor=\"#f0f0f0\">\n" +
                        "<h2 align=\"center\">恭喜您，账号注册成功！</h2>\n" +
                        "<form method=\"post\" action=\"./UploadAvatar?username=" + username + "\" enctype=\"multipart/form-data\">\n" +
                        "您可以上传一个图像文件作为头像:\n" +
                        "<input type=\"file\" name=\"uploadFile\" />\n" +
                        "<br/><br/>\n" +
                        "<input type=\"submit\" value=\"上传\" />\n" +
                        "</form>" +
                        "</body></html>");

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

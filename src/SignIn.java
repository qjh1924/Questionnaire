// 登录
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.*;

public class SignIn extends HttpServlet {
    public SignIn() {
        super();
    }

    // Get方法
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String docType = "<!DOCTYPE html>\n";

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
            sql = "SELECT password, avatar FROM users where username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(sql);

            // 记录登录状态
            boolean flag = false;
            String avatarPath = "";

            while(rs.next()){
                String pass = rs.getString("password");
                String avatar = rs.getString("avatar");
                if(pass.equals(password)) {
                    flag = true;
                    avatarPath = avatar;
                }
            }

            if (flag) {
                Cookie user = new Cookie("currentUser",username);
                Cookie avatar = new Cookie("avatarPath",avatarPath);
                response.addCookie( user );
                response.addCookie( avatar );
                out.println(docType +
                        "<html>\n" +
                        "<head><title>Success!</title></head>\n" +
                        "<body bgcolor=\"#f0f0f0\">\n" +
                        "<h2 align=\"center\">恭喜您，" + username + "，登录成功！</h2>\n" +
                        "<a href=\"questionnaire.jsp\"><button>前往填写问卷</button></a>\n" +
                        "</body></html>");
            } else {
                out.println(docType +
                        "<html>\n" +
                        "<head><title>Error!</title></head>\n" +
                        "<body bgcolor=\"#f0f0f0\">\n" +
                        "<h2 align=\"center\">用户不存在或密码错误，登录失败！</h2>\n" +
                        "<a href=\"index.jsp\"><button>返回重新登陆</button></a>\n" +
                        "</body></html>");
            }


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

    // 处理 POST 方法请求的方法
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
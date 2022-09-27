<%--
  Created by IntelliJ IDEA.
  User: cdsyq
  Date: 2022/3/22
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.net.*" %>
<html>
<head>
    <title>在线问卷</title>
    <style>
        .avatar {
            display: block;
            width: 100px;
            height: 100px;
            border-radius: 10px;
        }
    </style>
</head>
<body>
<%
    Cookie cookie = null;
    Cookie[] cookies = null;
    // 获取 cookies 的数据,是一个数组
    cookies = request.getCookies();

    // 登录状态
    boolean flag = false;
    String username =  "";
    String avatar =  "";
    if( cookies != null ){
        for (int i = 0; i < cookies.length; i++) {
            cookie = cookies[i];

            if (cookie.getName().equals("currentUser")) {
                flag = true;
                username = cookie.getValue();
            }
            if (cookie.getName().equals("avatarPath")) {
                avatar = cookie.getValue();
            }
        }
    }
    if (flag) {
        out.println("<div>\n" +
                "<img class=\"avatar\" alt=\"avatar\" src=" + avatar + " />\n" +
                "<p>欢迎您！"+ username + "</p>\n" +
                "<p>这里是问卷内容</p>\n" +
                "</div>");
    } else {
        out.println("<div>\n" +
                "    <h2>未登录，无法查看问卷内容，请您先登录！</h2>\n" +
                "    <a href=\"index.jsp\"><button>返回登录页面</button></a>\n" +
                "</div>");
    }
%>

</body>
</html>

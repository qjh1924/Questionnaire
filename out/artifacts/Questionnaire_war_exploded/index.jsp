<%--
  Created by IntelliJ IDEA.
  User: cdsyq
  Date: 2022/3/22
  Time: 14:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>在线问券调查网</title>
  </head>
  <body>
  <h3>您好，欢迎访问在线问卷调查网，请您登录账号开始填写：</h3>
    <form action="SignIn" method="POST">
      用户名：<input type="text" name="username">
      <br /><br />
      密码：<input type="password" name="password" />
      <br /><br />
      <input type="submit" value="登录" />
    </form>
  还没有账号？<a href="signup.jsp"><button>立即注册</button></a>
  </body>
</html>

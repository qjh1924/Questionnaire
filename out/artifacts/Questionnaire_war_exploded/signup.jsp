<%--
  Created by IntelliJ IDEA.
  User: cdsyq
  Date: 2022/3/22
  Time: 14:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>在线问卷网注册</title>
</head>
<body>
<h3>您好，欢迎您注册，请填写您的登录名和密码：</h3>
<form action="SignUp" method="POST">
    用户名：<input type="text" name="username">
    <br /><br />
    密码：<input type="password" name="password" />
    <br /><br />
    确认密码：<input type="password" name="confirmPassword" />
    <br /><br />
    <input type="submit" value="注册" />
</form>
</body>
</html>

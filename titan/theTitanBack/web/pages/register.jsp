<%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 2020/2/24
  Time: 14:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册</title>
</head>
<body>
    <div style=" text-align: center; position: absolute; top: 40%; left: 40%;">
        <h1>注册</h1>
        <form method="post" action="register.action">
            账号:<input type="text" name="username">
            <br>
            密码:<input type="password" name="password">
            <br>
            再次确认密码<input type="password" name="repassword">
            <br>
            识别码:<input type="text" name="identifyText">
            <br>
            <input type="submit">
        </form>
    </div>
</body>
</html>

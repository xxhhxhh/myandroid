<%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 2020/2/24
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>欢迎您，管理员</title>
  </head>
  <body>
        <div style=" text-align: center; position: absolute; top: 40%; left: 40%;">
            <form method="post" action="login.action">
                用户名: <label>
                <input type="text" name="username">
            </label>
                <br>
                密码:<label>
                <input type="password" name="password">
            </label>
                <input type="submit">
            </form>
            <br>
            <br>
            <a href="pages/register.jsp">注册</a>
            <br>
            <a href="pages/findback.jsp">找回</a>
        </div>


  </body>
</html>

<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 2020/2/25
  Time: 11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <% HttpServletRequest servletRequest = ServletActionContext.getRequest();
        String username = servletRequest.getParameter("username") != null ?
            servletRequest.getParameter("username") : "";
//        String adminUser = servletRequest.getParameter("adminusername") != null ?
//                servletRequest.getParameter("adminusername") : "";
        DateFormat dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
    %>
    <title><%=username%>的信息页面</title>
    <script src="../jquery-3.4.1.min.js"></script>
    <script>
        /*获取随记简单数据*/
        function getSimpleSuiJi(username)
        {
            var nowItem = 0;
            var table = document.getElementById("tableSuiJi");
            $.ajax(
                {
                    url : "getSimpleSuiJi.action",
                    dataType : "json",
                    data :
                        {
                            "username" : username,
                            "nowItem" : nowItem
                        },
                    success:function (data)
                    {
                        var data1 = JSON.parse(data['result']);

                        for(var key in data1)
                        {
                            var data2 = data1[key];
                            var tableRow = table.insertRow();
                            var tableCell = tableRow.insertCell();
                            tableCell.innerHTML = "<a href=javascript:toPostSuiJi('" + username + "'," + data2['suiji_id']
                                + ")>" + data2['suiji_id'] + "</a>";
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data2['looks_number'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data2['comment_number'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data2['goods'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data2['location'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data2['the_date'];
                        }
                    }
                }
            )
        }
        /*获取文章简单数据*/
        function getSimpleArticle(username)
        {
            var nowItem = 0;
            var table = document.getElementById("tableArticle");
            $.ajax(
                {
                    url : "getSimpleArticle.action",
                    dataType : "json",
                    data :
                        {
                            "username" : username,
                            "nowItem" : nowItem
                        },
                    success:function (data)
                    {
                        var data1 = JSON.parse(data['result']);
                        console.log(data1);
                        for(var key in data1)
                        {
                            var data2 = data1[key];
                            var tableRow = table.insertRow();
                            var tableCell = tableRow.insertCell();
                            tableCell.innerHTML = "<a href=javascript:toPostArticle('" + username + "'," + data2['article_id']
                                + ")>" + data2['article_id'] + "</a>";
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data2['looks_number'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data2['comment_number'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data2['goods'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data2['location'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data2['the_date'];
                        }

                    }
                }
            )
        }
        /*获取用户关系*/
        function getRelated(username)
        {
            var table = document.getElementById("userRelated");

            $.ajax(
                {
                    url : "getRelated.action",
                    dataType : "json",
                    type : "post",
                    data :
                        {
                            "username" : username
                        },
                    success:function (data)
                    {
                        var data1  = data['result'];
                        var data2 = JSON.parse(data1);

                        for(var key in data2)
                        {
                            var data3 = data2[key];
                            var tableRow = table.insertRow();
                            var tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data3['username'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = "<a href=javascript:toPost('" + data3['to_username'] +
                            "')>" + data3['to_username'] + "</a>";
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data3['type'];
                        }

                    },
                    error:function (data)
                    {

                    }
                }
            )

        }

        /*触发post,切换其它用户*/
        function toPost(username)
        {
            var thePost = document.createElement("form");
            thePost.action = "one_user.jsp";
            var theUsernamePut = document.createElement("textarea");
            theUsernamePut.name = "username";
            theUsernamePut.value = username;
            thePost.appendChild(theUsernamePut);
            thePost.method = "post";
            document.body.appendChild(thePost);
            thePost.submit();
            document.body.removeChild(thePost);

        }

        /*触发post，查看随记详细信息*/
        function toPostSuiJi(username, suiji_id)
        {
            var thePost = document.createElement("form");
            thePost.action = "../suiji/suiji.jsp";
            var theUsernamePut = document.createElement("textarea");
            theUsernamePut.name = "username";
            theUsernamePut.value = username;
            thePost.appendChild(theUsernamePut);
            theUsernamePut = document.createElement("textarea");
            theUsernamePut.name = "suiji_id";
            theUsernamePut.value = suiji_id;
            <%--theUsernamePut = document.createElement("textarea");--%>
            <%--theUsernamePut.name = "adminuser";--%>
            <%--theUsernamePut.value = <%=adminUser%>;--%>
            thePost.appendChild(theUsernamePut);
            thePost.method = "post";
            document.body.appendChild(thePost);
            thePost.submit();
            document.body.removeChild(thePost);
        }
        /*触发post,查看文章详细信息*/
        function toPostArticle(username, article_id)
        {
            var thePost = document.createElement("form");
            thePost.action = "../article/article.jsp";
            var theUsernamePut = document.createElement("textarea");
            theUsernamePut.name = "username";
            theUsernamePut.value = username;
            thePost.appendChild(theUsernamePut);
            theUsernamePut = document.createElement("textarea");
            theUsernamePut.name = "article_id";
            theUsernamePut.value = article_id;
            <%--theUsernamePut = document.createElement("textarea");--%>
            <%--theUsernamePut.name = "adminuser";--%>
            <%--theUsernamePut.value = <%=adminUser%>;--%>
            thePost.appendChild(theUsernamePut);
            thePost.method = "post";
            document.body.appendChild(thePost);
            thePost.submit();
            document.body.removeChild(thePost);
        }
    </script>
</head>
<body>
    <div style="border: solid 1px black; text-align: center">
        <div style="float: left"><%=dateFormat.format(date).toString()%></div>
        <div style="float: left">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
        <div style="float: right"><%=username%></div>
    </div>
    <br>
    <br>
    <div style="display: flex; align-content: center; float: left;">
        <table id="tableSuiJi" style="border: black 2px solid; text-align: center">
            <tr>
                <td>随记标识</td>
                <td>查看数量</td>
                <td>评论数量</td>
                <td>赞数</td>
                <td>发表位置</td>
                <td>发表日期</td>
            </tr>
        </table>
    </div>
    <script>getSimpleSuiJi(<%=username%>)</script>
    <div style="display: flex; align-content: center; float: left">
        <table id="userRelated" style=" border: black solid 2px; text-align: center">
            <tr>
                <td>用户名</td>
                <td>被关系用户</td>
                <td>关系(0关注、1拉黑)</td>
            </tr>
        </table>
    </div>
    <script>getRelated(<%=username%>)</script>
    <div style="display: flex; align-content: center; float: right">
        <table id="tableArticle" style="border: black 2px solid; text-align: center;">
            <tr>
                <td>文章标识</td>
                <td>文章标题</td>
                <td>查看数量</td>
                <td>评论数量</td>
                <td>赞数</td>
                <td>发表位置</td>
                <td>发表日期</td>
            </tr>
        </table>
    </div>
    <script>getSimpleArticle(<%=username%>)</script>
</body>
</html>

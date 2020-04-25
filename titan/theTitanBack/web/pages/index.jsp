<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="java.text.DateFormat" %><%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 2020/2/24
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>主页</title>
    <script src="jquery-3.4.1.min.js" type="text/javascript" ></script>
    <script>
        /*获取数据*/
        function getData()
        {
            var nowItem = 0;
            var table = document.getElementById("userInfoShow");

            $.ajax(
                {
                   url : "getUserMessage.action",
                   dataType : "json",
                   type : "post",
                   data :
                       {
                           "nowItem" : nowItem
                       },
                    success:function (data)
                    {
                        var data1 = data['data'];
                        var data2 = JSON.parse(data1);
                        for(var key in data2)
                        {
                            nowItem++;
                            var tableRow = table.insertRow();
                            var data3 = data2[key];
                            var tableCell = tableRow.insertCell();
                            tableCell.innerHTML = "<a href='javascript:toPost(" + data3['username'] +")'>" + data3['username'] + "</a>";
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data3['user_eyi'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data3['user_suiji'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data3['user_article'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data3['user_fans'];
                            tableCell = tableRow.insertCell();
                            tableCell.innerHTML = data3['user_focus'];
                        }
                    },
                    error:function ()
                    {

                    }
                });
        }

        /*触发post*/
        function toPost(username)
        {
            var thePost = document.createElement("form");
            thePost.action = "user/one_user.jsp";
            var theUsernamePut = document.createElement("textarea");
            theUsernamePut.name = "username";
            theUsernamePut.value = username;
            <%--theUsernamePut = document.createElement("textarea");--%>
            <%--theUsernamePut.name = "adminusername";--%>
            <%--theUsernamePut.value = <s:property value="username"/>;--%>
            thePost.appendChild(theUsernamePut);
            thePost.method = "post";
            document.body.appendChild(thePost);
            thePost.submit();
            document.body.removeChild(thePost);

        }
    </script>
</head>
    <body>

        <% DateFormat dateFormat = DateFormat.getDateInstance();
            Date date = new Date();
            String username = ServletActionContext.getRequest().getParameter("username");%>
            <div style="border: solid 1px black; text-align: center">
                <div style="float: left"><%=dateFormat.format(date).toString()%></div>
                <div style="float: left">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float: right"><%=username%></div>
            </div>
            <br>
        <br>
        <div style=" position: absolute;
             top: 50%;left: 50%;-webkit-transform: translate(-50%, -50%);-moz-transform: translate(-50%, -50%);
             -ms-transform: translate(-50%, -50%);-o-transform: translate(-50%, -50%);transform: translate(-50%, -50%); ">
            <table id="userInfoShow" style="border: black solid 2px; text-align: center;">
               <tr>
                   <th>用户名</th>
                   <th>用户恶意</th>
                   <th>用户随记数量</th>
                   <th>用户文章数量</th>
                   <th>用户粉丝</th>
                   <th>用户关注</th>
               </tr>
            </table>
        </div>
        <script>getData()</script>
    </body>
</html>

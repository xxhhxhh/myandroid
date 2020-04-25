<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.apache.struts2.ServletActionContext" %><%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 2020/2/24
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <% DateFormat dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        String username = servletRequest.getParameter("username") != null ?
                servletRequest.getParameter("username") : "";
//        String adminuser = servletRequest.getParameter("adminuser") != null ?
//                servletRequest.getParameter("adminuser") : "";
        int article_id = servletRequest.getParameter("article_id") != null ?
                Integer.parseInt(servletRequest.getParameter("article_id")) : 0;
    %>
    <title><%=username%>的文章,编号<%=article_id%></title>
    <script src="../jquery-3.4.1.min.js"></script>
    <script>

        /*获取数据*/
        function getData()
        {
            var id = <%=article_id%>;
            $.ajax(
                {
                    url : "oneArticle.action",
                    dataType : "json",
                    type : "post",
                    data :
                        {
                            "article_id" : id,
                            "type" : 5
                        },
                    success:function (data)
                    {
                        makeLabels(JSON.parse(data['label']));
                        document.getElementById("title").innerText = data['articleTitle'];
                        document.getElementById("showHTML").src = data['theUrl'];
                    }
                }
            )
        }

        /*设置标签*/
        function makeLabels(labels)
        {
            var showLabels = document.getElementById("showLabels");
            for(var key in labels)
            {
                var adiv = document.createElement("div");
                adiv.innerText = labels[key];
                adiv.style.float = "left";
                adiv.style.marginLeft = "80px";
                showLabels.appendChild(adiv);
            }
        }

        /*编辑设定*/
        function editAble(editable)
        {
            var a = document.getElementById("main");
            a.contentEditable = editable;
            var b = document.getElementById("showLabels");
            b.contentEditable = false;
            if(editable)
            {
                var c = window.frames['showHTML'].contentDocument.getElementById("main");
                c.contentEditable = true;
                var d = c.getElementsByTagName("div");
                for(var i = 0; i < d.length; i++)
                {
                    d[i].contentEditable = true;
                }
                alert("进入编辑模式");
            }
            else
            {
                var c = window.frames['showHTML'].contentDocument.getElementById("main");
                c.contentEditable = false;
                var d = c.getElementsByTagName("div");
                for(var i = 0; i < d.length; i++)
                {
                    d[i].contentEditable = false;
                }
                alert("进入预览模式");
            }
        }

        /*获取整体网页*/
        function getWholeHTML()
        {
            try
            {
                return window.frames['showHTML'].contentDocument.documentElement;
            }
            catch (e)
            {
                return "";
            }

        }

        /*保存数据*/
        function save()
        {
            try
            {
                $.ajax(
                    {
                        url : "saveArticle.action",
                        dataType: "json",
                        type : "post",
                        data :
                            {
                                "mainMessage" : getWholeHTML(),
                                "username" : <%=username%>,
                                "article_id" : <%=article_id%>,
                                "mode" : 1,
                                "articleTitle" : document.getElementById("title").innerText
                            },
                        success:function (data)
                        {
                            console.log(data);
                            location.href = "../index.jsp";
                        },
                        error:function ()
                        {
                            location.href = "../index.jsp";
                        }
                    }
                )
            }
            catch (e)
            {
                location.href = "../index.jsp";
            }

        }
        getData();
    </script>
    <title><%=username%>的随记，编号:<%=article_id%></title>

</head>
<body>
    <div style="border: solid 1px black; text-align: center">
        <div style="float: left"><%=dateFormat.format(date).toString()%></div>
        <div style="float: left">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;文章编号:<%=article_id%></div>
        <div style="float: right"><%=username%></div>
    </div>
    <br>
    <br>
    <!--选项栏-->
    <div style="border: solid 1px black; ">
        <button style="float: right; margin-left: 80px;" onclick="editAble(true)">编辑</button>
        <button style="float: right; margin-left: 80px;" onclick="editAble(false)">预览</button>
        <button style="float: right; margin-left: 80px;" onclick="save()">保存</button>
    </div>
    <br><br><br>
    <!--总体-->
    <div id="main">
        <!--标题-->
        <div style="text-align: center; border: black 1px solid; size: 45px;" id="title">
        </div>
        <br><br>
        <!--标签-->
        <div id="showLabels" style="border: black 1px solid">
            <div style=" float: left; margin-left: 80px;">标签栏:</div>
        </div>
        <br>
        <br>
        <div style="width: 100%; height: auto;">
            <!--网页内容-->
            <iframe id="showHTML" style="border: black 1px solid; width: 100%; height: 100%;">

            </iframe>
        </div>
    </div>
</body>
</html>

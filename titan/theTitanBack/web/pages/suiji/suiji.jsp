<%@ page import="java.io.Serializable" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
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
        String username = servletRequest.getParameter("username");
        int suiji_id = servletRequest.getParameter("suiji_id") != null ?
            Integer.parseInt(servletRequest.getParameter("suiji_id")) : 0;
    %>
    <title><%=username%>的随记,编号<%=suiji_id%></title>
    <script src="../jquery-3.4.1.min.js"></script>
    <script>

        var location1;

        /*获取数据*/
        function getData()
        {
            var id = <%=suiji_id%>;
            $.ajax(
                {
                    url : "oneSuiJiMain.action",
                    dataType : "json",
                    type : "post",
                    data :
                        {
                            "suiji_id" : id,
                            "type" : 5
                        },
                    success:function (data)
                    {
                        try
                        {
                            makeLabels(JSON.parse(data['labels']));
                        }
                        catch (e)
                        {
                            console.log(e);
                        }

                        document.getElementById("mainMessage").innerText = data['mainMessage'];
                        location1 = data['location'];
                        var photos = data['haveFile'];
                        for(var i = 0; i < photos; i++)
                        {
                            $.ajax(
                                {
                                    url : "oneMedia.action",
                                    dataType: "json",
                                    type : "post",
                                    data :
                                        {
                                            "suiji_id" : <%=suiji_id%>,
                                            "group_id" : i
                                        },
                                    success:function (data)
                                    {
                                        var a = document.createElement("img");
                                        a.src = "data:image/png;base64," + data['result'];
                                        a.style.float = "left";
                                        a.style.marginLeft = "50px";
                                        a.setAttribute("onclick", "hiddenPhoto(this)");
                                        document.getElementById("showPhotos").appendChild(a);
                                    }
                                }
                            )
                        }

                    }
                }
            )
        }

        /*隐藏图片*/
        function hiddenPhoto(view)
        {
            view.style.display = "none";
        }

        /*删除图片*/
        function deletePhotos()
        {
            var a = document.getElementById("showPhotos");
            var b = a.getElementsByTagName("img");
            for(var i = 0; i < b.length; i++)
            {
                if(b[i].style.display === "none")
                {
                    a.removeChild(b[i]);
                }
            }
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
                alert("进入编辑模式");
            }
            else
            {
                alert("进入预览模式");
            }
        }

        /*获取图片内容*/
        function getPhotos()
        {
            var array = [];

            var a = document.getElementById("showPhotos");
            var b = a.getElementsByTagName("img");
            for(var i = 0; i < b.length; i++)
            {
                array.push(b[i].src.split(",")[1]);
            }

            return array;
        }

        /*保存*/
        function save()
        {
            $.ajax(
                {
                    url : "saveSuiJi.action",
                    dataType : "json",
                    type : "post",
                    data :
                        {
                            "suiji_id" : <%=suiji_id%>,
                            "mode" : 1,
                            "fileStrings" : getPhotos(),
                            "mainMessage" : document.getElementById("mainMessage").innerText,
                            "username" : <%=username%>,
                            "location" : location1,
                            "fileType" : "0"
                        },
                    success:function ()
                    {
                        location.href = "../index.jsp";
                    },
                    error:function ()
                    {
                        location.href = "../index.jsp";
                    }
                }
            )
        }

        /*撤销图片隐藏*/
        function reShow()
        {
            var a = document.getElementById("showPhotos");
            var b = a.getElementsByTagName("img");
            for(var i = 0; i < b.length; i++)
            {
                b[i].style.display = "inline";
            }
        }
        getData();
    </script>
</head>
<body>
    <div style="border: solid 1px black; text-align: center">
        <div style="float: left"><%=dateFormat.format(date).toString()%></div>
        <div style="float: left">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;随记编号:<%=suiji_id%></div>
        <div style="float: right"><%=username%></div>
    </div>
    <br>
    <br>
    <!--选项栏-->
    <div style="border: solid 1px black; ">
        <button style="float: right; margin-left: 80px;" onclick="editAble(true)">编辑</button>
        <button style="float: right; margin-left: 80px;" onclick="editAble(false)">预览</button>
        <button style="float: right; margin-left: 80px;" onclick="save()">保存</button>
        <button style="float: right; margin-left: 80px;" onclick="reShow()">撤销</button>
    </div>
    <br><br><br>
    <!--总体-->
    <div id="main">
        <!--主要内容-->
        <div style="text-align: center; border: black 1px solid; size: 45px;" id="mainMessage">
        </div>
        <br><br>
        <!--标签-->
        <div id="showLabels" style="border: black 1px solid">
            <div style=" float: left; margin-left: 80px;">标签栏:</div>
        </div>
        <br>
        <br>
        <div style="border: black 1px solid; text-align: center">
            鼠标主键单击图片即可删除违规图片，单击撤销键撤销
        </div>
        <br>
        <br>
        <div style="width: 100%; height: auto;" id="showPhotos">

        </div>
    </div>
</body>
</html>

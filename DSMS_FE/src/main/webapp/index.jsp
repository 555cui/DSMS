<%--
  Created by IntelliJ IDEA.
  User: cui
  Date: 2018/4/23
  Time: 1:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>dsms test</title>
    <!-- UC强制全屏 -->
    <meta name="full-screen" content="yes">
    <!-- QQ强制全屏 -->
    <meta name="x5-fullscreen" content="true">
    <!-- IOS启用 WebApp 全屏模式 -->
    <meta name="apple-mobile-web-app-capable" content="yes" />
</head>
<body>
    <h2>hello world</h2>
    <form action="/DSMS_FE/upload/test" enctype="multipart/form-data" method="post">
        <input type="file" name="file"/>
        <button type="submit">ok</button>
    </form>
</body>
</html>

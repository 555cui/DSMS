<%--
  Created by IntelliJ IDEA.
  User: cui
  Date: 2018/5/5
  Time: 4:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>testing</title>
</head>
<body>
    <div>
        <div>
            <div>
                <video id="video" muted playsinline>
                    <source src="http://www.w3school.com.cn/example/html5/mov_bbb.mp4" type="video/mp4">
                    <source src="http://www.w3school.com.cn/example/html5/mov_bbb.ogg" type="video/ogg">
                    <source src="http://www.w3school.com.cn/example/html5/mov_bbb.webm" type="video/webm">
                </video>
            </div>
            <div>test test test</div>
            <script type="text/javascript">
                var video = document.getElementById('video');
                video.controls=false;
                video.onended=function(ev){
                    video.play();
                };
                video.play();
            </script>
        </div>
    </div>
</body>
</html>

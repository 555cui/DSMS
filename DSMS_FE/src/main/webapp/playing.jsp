<%--
  Created by IntelliJ IDEA.
  User: cui
  Date: 2018/5/7
  Time: 22:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="initial-scale=1,user-scalable=no,minimal-ui" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="full-screen" content="yes" />
    <meta name="x5-fullscreen" content="true" />
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"
            integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
            crossorigin="anonymous"></script>
    <title>播放端口</title>
</head>
<body style="margin: 0">
    <canvas id="display"></canvas>
    <script type="text/javascript">
        var canvas = $('#display')[0];
        function draw(template){
            canvas.width = template.width;
            canvas.height = template.height;

            var pan = canvas.getContext("2d");

            pan.fillStyle=template.backgroundColor;
            pan.fillRect(0, 0, canvas.width, canvas.height);

            for (var element of template.element){
                var video = document.createElement('video');
                video.hidden = true;
                video.muted = true;
                video.playsinline = true;
                video.autoplay = true;
                video.loop = true;
                video.src = element.media.url;
                document.body.appendChild(video);
            }
        }

        $.ajax({
            url: "/program/e814889b-8a29-4b27-9081-13e9837575e8",
            method: 'get',
            success: function( result ) {
                draw(result.data.template);
            }
        });
    </script>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: 连仕杰
  Date: 2023/4/25
  Time: 20:53
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="author" content="Liam">
    <title>To HDFS</title>
    <link rel="stylesheet" href="../css/styleA.css">
</head>
<body>
<!-- 为 ECharts 准备一个定义了宽高的 DOM -->
<jsp:include page="navigationBar.jsp"></jsp:include>

<div>
    <div class="divs" id="left" style="width:30%;float:left;">
        <form class="forms" action="../MySqlToHDFS" method="post">
            <h1 class="h">To HDFS</h1><br>
            <h2 class="h">Dir:</h2><input class="inputs" type="text" name="target_dir"><br>
            <h2 class="h">Map:</h2><input class="inputs" type="number" name="map_num"><br>
            <h2 class="h">Fields:</h2><input class="inputs" type="text" name="fields_terminated"><br>
            <h2 class="h">Lines:</h2><input class="inputs" type="text" name="lines_terminated"><br>
            <h2 class="h">Custom:</h2><input class="inputs" type="text" name="custom_parameters"><br>
            <input type="submit" value="run"><br><br><br>
        </form>
    </div>
    <div class="divs" id="right" style="width:70%;float:left;">
        <div id="main" style="width: 100%;">
            <img src="../image/background1.jpg" style="width: 100%">
        </div>
    </div>
</div>

<script type="text/javascript">

    let query = window.location.search.substring(1);
    let pair = query.split("=");
    if (pair[1] === 'success') {
        let acc = JSON.parse(JSON.stringify(${Acc}));
        let list = acc.rows;
        alert(list)
    } else if (pair[1] === 'error') {
        alert('error');
    }

</script>
</body>
</html>


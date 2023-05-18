<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="袁蕾">
    <title>Movie View</title>

    <!-- custom css file link  -->
    <link rel="stylesheet" href="../css/styleA.css">

</head>
<body>

<!-- header section starts  -->

<header>
    <div class="header-2">

        <div id="menu-bar"></div>

        <nav class="navbar">
            <a href="index.jsp">home</a>
            <a href="sqoop_mysql_to_hdfs.jsp">ToHDFS</a>
            <a href="sqoop_mysql_to_hive.jsp">ToHive</a>
            <a href="flume_mysql_to_hdfs.jsp">UpMySQL</a>
            <a href="flume_window_to_hdfs.jsp">UpLog</a>
        </nav>

        <div class="dropdown">
            <button class="dropbtn">${user.getName()}</button>
            <div class="dropdown-content">
                <a href="../Exit">Exit</a>
            </div>
        </div>

    </div>

</header>
<script language="JavaScript">
    console.log(`${user}.name`)
</script>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: 连仕杰
  Date: 2023/4/25
  Time: 20:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<jsp:include page="navigationBar.jsp"></jsp:include>
<div>
    <div class="divs" id="left" style="width:30%;float:left;">
        <form class="forms" action="../FMySqlToHDFS" method="post">
            <h1 class="h">To HDFS</h1><br>
            <h2 class="h">Fields:</h2><input class="inputs" type="text" name="fields_terminated"><br>
            <input type="submit" value="run"><br><br><br>
        </form>
    </div>
    <div class="divs" id="right" style="width:70%;float:left;">
        <div id="main" style="width: 100%;">
            <img src="../image/background4.jpg" style="width: 100%">
        </div>
    </div>
</div>

<script src="../js/echarts.min.js"></script>
<script type="text/javascript">

    let query = window.location.search.substring(1);
    let pair = query.split("=");
    if (pair[1] === 'true') {
        let count = ${Count};
        let time = ${Time};
        let size = ${Size};
        let retry = ${Retry};

        console.log(count, time, size, retry)

        let dom = document.getElementById('main');
        let myChart = echarts.init(dom, null, {
            width: 800,
            height: 600
        });

        option = {
            legend: {},
            tooltip: {},
            axisLine: {
                lineStyle: {
                    color: [[0.2, '#93CDDD'], [0.4, '#c23531'], [0.6, '#63869e'], [0.8, '#B3A2C7'], [1, '#91c7ae']]
                }
            },
            series: [
                {
                    name: 'Number of events sent',
                    type: 'gauge',
                    progress: {
                        show: true
                    },
                    detail: {
                        valueAnimation: true,
                        formatter: '{value}'
                    },
                    min:0,
                    max:10000,
                    data: [
                        {
                            value: count,
                            name: 'Count'
                        }
                    ],
                    radius: '53%',
                    center: ['25%', '30%'],
                },
                {
                    name: 'Time spent',
                    type: 'gauge',
                    progress: {
                        show: true
                    },
                    detail: {
                        valueAnimation: true,
                        formatter: '{value}s'
                    },

                    min:0,
                    max:100,
                    data: [
                        {
                            value: time.toFixed(2),
                            name: 'Time'
                        }
                    ],
                    radius: '53%',
                    center: ['75%', '30%'],
                },
                {
                    name: 'Amount of data transmitted',
                    type: 'gauge',
                    progress: {
                        show: true
                    },
                    detail: {
                        valueAnimation: true,
                        formatter: '{value}MB'
                    },
                    min:0,
                    max:50,
                    data: [
                        {
                            value: size.toFixed(2),
                            name: 'Size'
                        }
                    ],
                    radius: '53%',
                    center: ['25%', '75%'],
                },
                {
                    name: 'Number of Http requests sent',
                    type: 'gauge',
                    progress: {
                        show: true
                    },
                    detail: {
                        valueAnimation: true,
                        formatter: '{value}'
                    },
                    min:0,
                    max:20,
                    data: [
                        {
                            value: retry,
                            name: 'Http'
                        }
                    ],
                    radius: '53%',
                    center: ['75%', '75%'],
                }
            ]
        };

        if (option && typeof option === "object") {
            myChart.setOption(option, true);
        }
    } else if (pair[1] === 'error') {
        alert('error');
    }

</script>
</body>
</html>

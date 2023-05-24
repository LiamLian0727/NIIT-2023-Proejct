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
    <style>
        .btn {
            height: 50px;
            width: 170px;
        }

        .btn button {
            height: 100%;
            width: 100%;
            border: none;
            outline: none;
            background: #949de0;
            /*按钮字体颜色*/
            color: #fff;
            border-radius: 5px;
            font-size: 18px;
            letter-spacing: 1px;
            font-weight: 500;
            text-transform: uppercase;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn button:hover {
            background: #213b82;
        }

        .file-name {
            width: 145px;
            position: relative;
            background: #949de0;
            color: #fff;
            padding: 8px 12px;
            border-radius: 5px;
            margin-top: 15px;
            display: none;
        }

        .file-name:before {
            position: absolute;
            content: '';
            height: 15px;
            width: 15px;
            background: #949de0;
            left: 65px;
            top: 2%;
            /*尖尖气泡*/
            transform: translateY(-50%) rotate(45deg);
        }

        .submitbutton {
            display: block;
            position: relative;
            cursor: pointer;
            border-radius: 10px;
            margin-top: 5px;
            padding: 10px 20px;
            text-align: center;
            font-size: 25px;
            font-weight: 500;
            letter-spacing: 2px;
            border: none;
            color: #fff;
            background-color: #949de0;
        }

        .submitbutton:hover {
            background: #7867cf;
        }
    </style>
</head>
<body>
<jsp:include page="navigationBar.jsp"></jsp:include>
<div>
    <div class="divs" id="left" style="width:30%;float:left;">
        <form class="forms" action="../FWindowToHDFS" method="post">
            <h1 class="h">To HDFS</h1><br>
            <h2 class="h">File:</h2>
            <input id="defaultbtn" type="file" hidden onchange="change()" name="FToHDFS"/>
            <div class="btn">
                <button onclick="active()" id="viewbtn">Choose a file</button>
            </div>
            <div class="file-name" id="fname">No file chosen</div>
            <input class="submitbutton" type="submit" value="run"><br><br><br>
        </form>
    </div>
    <div class="divs" id="right" style="width:70%;float:left;">
        <div id="main" style="width: 100%;">
            <img src="../image/background.jpg" style="width: 100%">
        </div>
    </div>
</div>

<script src="../js/echarts.min.js"></script>
<script type="text/javascript">

    const defaultBtn = document.getElementById("defaultbtn");
    const customBtn = document.getElementById("viewbtn");
    const fileName = document.getElementById("fname");

    function active() {
        defaultBtn.click();
    }

    function change() {
        if (defaultBtn.value) {
            let value = defaultBtn.value.toString().split("\\");
            let nameValue = value[value.length - 1];
            fileName.style.display = "inline";
            fileName.style.display = "block";
            fileName.textContent = nameValue;
        } else {
            fileName.style.display = "inline";
            fileName.style.display = "block";
            fileName.textContent = "No file chosen";
        }
    }

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

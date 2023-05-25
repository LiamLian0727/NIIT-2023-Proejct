NIIT 2023 Proejct B3 Group1
===
![Java1.8](https://img.shields.io/badge/Java-1.8-blue.svg)
![Hadoop 3.3.0](https://img.shields.io/badge/Hadoop-3.3.0-green.svg)
![Sqoop 1.4.6](https://img.shields.io/badge/Sqoop-1.4.6-red.svg)
![Flume 1.9.0](https://img.shields.io/badge/Flume-1.9.0-red.svg)
### 项目成员: 
>连仕杰
 袁蕾
 殷明
 李广飞

### How to begin : 
>
**First: get our code**
```bash
git -clone https://gitee.com/lian-shijie/Group1.git
```
**Second: strat hadoop system**
```bash
start-all.sh
```
**Third: strat flume agent**
```bash
cd flume
flume-ng agent -n agent -f flume/http-conf.properties
```
The default maximum heap memory size when flume starts is 20M, and OOM problems are easy to occur when the amount of data in the actual environment is large, which is added in the flume-env.sh under the basic configuration file conf of flume
```bash
export JAVA_OPTS="-Xms2048m -Xmx2048m -Xss256k -Xmn1g -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:-UseGCOverheadLimit"
```
And in the flume startup script flume-ng, modify 

```bash
JAVA_OPTS="-Xmx20m" to JAVA_OPTS="-Xmx2048m"
```

Here we jump the threshold of heap memory to 2G, which can be adjusted according to the specific hardware situation in the actual production environment


***Make by 2023/4/26***
 






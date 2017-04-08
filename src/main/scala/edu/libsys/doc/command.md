## 启动过程
### 1.启动hadoop集群
Master执行：
```
/usr/local/hadoop/sbin/start-dfs.sh && /usr/local/hadoop/sbin/start-yarn.sh
```
查看：
```markdown
192.168.1.124:8088
```
### 2.启动spark集群
Master执行：
```markdown
/usr/local/spark/sbin/start-all.sh
```
查看：
```markdown
192.168.1.124:8080
```
### 3.程序及数据文件路径：
程序：Master:/home/spark/spark-stats.jar
文件：hdfs:///home/spark/data/*

### 4.提交作业
```markdown
/usr/local/spark/bin/spark-submit \
    --class edu.libsys.Main \
    --master yarn \
    --deploy-mode cluster \ 
    --executor-memory 3G \
    --queue default \
    /home/spark/book-stats-1.0.jar \
    hdfs:///user/data \
    hdfs:///user/result
```
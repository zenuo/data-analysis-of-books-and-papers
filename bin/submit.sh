#!/bin/bash
#提交作业脚本

#切换工作路径
cd /home/spark

#需要执行某个步骤时，请将其他步骤的命令注释。
#第一步骤
rm -rf /home/spark/data/result && nohup /usr/local/spark/bin/spark-submit --class edu.libsys.stats.GetEdgeAndVertices --master local --executor-memory 58G --total-executor-cores 6 --num-executors 100 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 --conf spark.default.parallelism=1000 /home/spark/book-stats-1.0.jar /home/spark/data/txt/half /home/spark/data/result > MainStats.log 2>&1 &

#第二步骤
#nohup /usr/local/spark/bin/spark-submit --class edu.libsys.stats.GetRelationship --master local --executor-memory 58G --total-executor-cores 6 --num-executors 100 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 --conf spark.default.parallelism=1000 /home/spark/book-stats-1.0.jar > MainStats.log 2>&1 &

#第三步骤
#nohup /usr/local/spark/bin/spark-submit --class edu.libsys.stats.GetInDegrees --master local --executor-memory 58G --total-executor-cores 6 --num-executors 100 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 --conf spark.default.parallelism=1000 /home/spark/book-stats-1.0.jar > MainStats.log 2>&1 &

#第四步骤
#nohup /usr/local/spark/bin/spark-submit --class edu.libsys.Main --master local --executor-memory 58G --total-executor-cores 6 --num-executors 100 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 --conf spark.default.parallelism=1000 /home/spark/book-stats-1.0.jar > MainStats.log 2>&1 &

#提交后打印日志，可不注释
tail -n0 -f MainStats.log
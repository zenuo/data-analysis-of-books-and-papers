package edu.libsys

import edu.libsys.conf.Conf
import edu.libsys.util._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Main {
  /**
    * 根据节点的入度文件计算节点权重
    *
    * @param args 控制台参数
    */
  def main(args: Array[String]): Unit = {
    //屏蔽不必要的日志输出
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    //判断文件参数是否正确
    if (args.length != 2) {
      println("-------------------------------ERROR-------------------------------")
      println("Valid program arguments:")
      println("1.PATH_TO_OBJECT_DATA_DIRECTORY")
      println("2.PATH_TO_RESULT_DATA_DIRECTORY")
      println("Usage:\n/usr/local/spark/bin/spark-submit --class edu.libsys.Main --master local --executor-memory 52G --total-executor-cores 6 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 /home/spark/book-stats-1.0.jar /home/spark/data/graph/obj/inDegrees.obj /home/spark/result")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      return
    }

    /**
      * spark会话
      */
    val spark: SparkSession = SparkSession
      .builder()
      .appName("MainStats")
      .getOrCreate()
    //use the Kryo library to serialize objects
    spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val sc: SparkContext = spark.sparkContext

    //文件路径
    //结果数据文件保存路径
    val booksResultPath: String = args(1) + "/books"
    val papersResultPath: String = args(1) + "/papers"

    //加载入度文件
    val inDegreesRDD = sc.objectFile(args.head)
    //图书节点
    val books: RDD[String] = inDegreesRDD
      .filter(VertexUtil.getVertexType(_) == 0)
      .map(VertexUtil.vertexToString(_, 0))
      .repartition(Conf.numPartitions)
    //论文节点
    val papers: RDD[String] = inDegreesRDD
      .filter(VertexUtil.getVertexType(_) == 1)
      .map(VertexUtil.vertexToString(_, 1))
      .repartition(Conf.numPartitions)


    //保存到文本文件
    books.saveAsTextFile(booksResultPath)
    papers.saveAsTextFile(papersResultPath)


    //停止
    sc.stop()
    spark.stop()
  }
}

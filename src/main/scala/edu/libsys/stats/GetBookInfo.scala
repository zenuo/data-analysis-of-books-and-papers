package edu.libsys.stats

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object GetBookInfo {
  def main(args: Array[String]): Unit = {
    //屏蔽不必要的日志输出
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    //判断文件参数是否正确
    if (args.length != 2) {
      println("-------------------------------ERROR-------------------------------")

      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      return
    }

    /**
      * spark会话
      */
    val spark: SparkSession = SparkSession
      .builder()
      .appName("GetBookInfo")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext


    sc.stop()
    spark.stop()
  }
}

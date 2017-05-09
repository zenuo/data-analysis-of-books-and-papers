package edu.libsys.stats

import edu.libsys.conf.Conf
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * 获得图书ID、中图法分类名对应关系
  */
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

    val book_id_CLCId: String = args(0) + "/book_id_CLCId.txt"
    val cls_no_name: String = args(0) + "/cls_no_name.txt"
    val book_id: String = args(0) + "/book_id.txt"
    val resultPath = args(1)

    val delimiter01 = ","

    //bookIdCLCIdTupleList
    val bookIdCLCIdTupleList: RDD[(String, Int)] = sc.textFile(book_id_CLCId).map(line => {
      val tokens = line.split(delimiter01)
        .map(_.trim)
      //提高匹配度，暂时不使用中图法分类名解析方法，否则匹配量太多，导致质量下降
      //结果类似(H152.5/34,1)
      tokens(1) -> tokens(0).toInt
    })

    //bookCLCIdCLCNameTupleList
    val bookCLCIdCLCNameTupleList: RDD[(String, String)] = sc.textFile(cls_no_name).map(line => {
      val tokens = line.split(delimiter01)
        .map(_.trim)
      //类似(S325,品种的整理与保存)
      tokens(0) -> tokens(1)
    })

    //部分图书ID、中图法分类名对应关系
    val partOfBookCLCNameIdRDD: RDD[(Int, String)] = bookCLCIdCLCNameTupleList
      .join(bookIdCLCIdTupleList, Conf.numTasks)
      .map(tuple => {
        (tuple._2._2, tuple._2._1)
      })

    //全部图书的ID
    val bookIdRDD: RDD[(Int, Int)] = sc.textFile(book_id)
      .map(
        line => {
          (line.toInt, 0)
        }
      )

    //全部部分图书ID、中图法分类名对应关系
    val bookIdCLCNameRDD: RDD[(Int, String)] = bookIdRDD
      .leftOuterJoin(partOfBookCLCNameIdRDD)
      .map(
        triad => {
          (triad._1, triad._2._2.getOrElse("暂无"))
        }
      )

    val bookIdCLCNameStringRDD: RDD[String] = bookIdCLCNameRDD
      .map(
        tuple => {
          s"""${tuple._1},'${tuple._2}'"""
        }
      )

    bookIdCLCNameStringRDD.saveAsTextFile(resultPath)
    /*
    182316,"经济"
    1209714,"素描、速写技法"
    627804,"暂无"
     */
    sc.stop()
    spark.stop()
  }
}

package edu.libsys.stats

import edu.libsys.conf.Conf
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object GetTextFilesOfPaperInfo {
  /**
    * 获得论文ID与作者、领域名称、关键词的对关系
    *
    * @param args 命令行参数
    */
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

    //spark会话
    val spark: SparkSession = SparkSession
      .builder()
      .appName("GetPaperInfo")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext

    val paper_id_paperId: String = args(0) + "/paper_id_paperId.txt"
    val paper_paperID_author: String = args(0) + "/paper_paperId_author.txt"
    val paper_paperId_field: String = args(0) + "/paper_paperId_field.txt"
    val paper_paperId_indexTerm: String = args(0) + "/paper_paperId_indexTerm.txt"
    val resultPath = args(1)

    val delimiter01 = ","

    val paperIdRDD: RDD[(Int, Int)] = sc
      .textFile(paper_id_paperId)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        tokens(0).toInt -> 0
      })

    val paperIdPaperIDTupleList: RDD[(String, Int)] = sc
      .textFile(paper_id_paperId)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        tokens(1) -> tokens(0).toInt
      })

    val paperAuthorPaperIDTupleList = sc
      .textFile(paper_paperID_author)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        tokens(0) -> tokens(1)
      })

    val paperPaperIdFieldTupleList: RDD[(String, String)] = sc
      .textFile(paper_paperId_field)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        tokens(0) -> tokens(1)
      })

    val paperPaperIdIndexTermTupleList = sc
      .textFile(paper_paperId_indexTerm)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        tokens(0) -> tokens(1)
      })

    //未聚合，部分
    val partOfPaperAuthorRDD: RDD[(Int, String)] = paperAuthorPaperIDTupleList
      .join(paperIdPaperIDTupleList, Conf.numTasks)
      .map(tuple => {
        tuple._2._2 -> tuple._2._1
      })
    val partOfPaperFieldRDD: RDD[(Int, String)] = paperPaperIdFieldTupleList
      .join(paperIdPaperIDTupleList, Conf.numTasks)
      .map(tuple => {
        tuple._2._2 -> tuple._2._1
      })
    val partOfPaperIndexTermRDD: RDD[(Int, String)] = paperPaperIdIndexTermTupleList
      .join(paperIdPaperIDTupleList, Conf.numTasks)
      .map(tuple => {
        tuple._2._2 -> tuple._2._1
      })

    //聚合，部分
    val reducedPartOfPaperAuthorRDD: RDD[(Int, String)] = partOfPaperAuthorRDD
      .reduceByKey(_ + "," + _)
    val reducedPartOfPaperFieldIdRDD: RDD[(Int, String)] = partOfPaperFieldRDD
      .reduceByKey(_ + "," + _)
    val reducedPartOfPaperIndexTermRDD: RDD[(Int, String)] = partOfPaperIndexTermRDD
      .reduceByKey(_ + "," + _)

    //全体
    //第一步，整合作者
    val temp01: RDD[(Int, String)] = paperIdRDD
      .leftOuterJoin(reducedPartOfPaperAuthorRDD)
      .map(triple => {
        triple._1 -> s"""'${triple._2._2.getOrElse("暂无")}'"""
      })
    //第二步，整合领域名词
    val temp02: RDD[(Int, String)] = temp01
      .leftOuterJoin(reducedPartOfPaperFieldIdRDD)
      .map(triple => {
        triple._1 -> s"""${triple._2._1}#@'${triple._2._2.getOrElse("暂无")}'"""
      })
    //第三步，整合关键词
    val paperInfoRDD: RDD[(Int, String)] = temp02
      .leftOuterJoin(reducedPartOfPaperIndexTermRDD)
      .map(triple => {
        triple._1 -> s"""${triple._2._1}#@'${triple._2._2.getOrElse("暂无")}'"""
      })

    val paperInfoRDDString: RDD[String] = paperInfoRDD
      .map(tuple => {
        s"${tuple._1}#@${tuple._2}"
      })

    paperInfoRDDString.saveAsTextFile(resultPath)

    sc.stop()
    spark.stop()
  }
}

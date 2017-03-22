package edu.libsys

import edu.libsys.stats._
import org.apache.spark.sql.SparkSession

object Main {

  //创建会话
  val spark = SparkSession
    .builder()
    .appName("MainStats")
    .getOrCreate()

  //main method
  def main(args: Array[String]): Unit = {
    //判断文件参数是否正确
    if (args.length != 2) {
      println("-------------------------------ERROR-------------------------------")
      println("Valid program arguments:")
      println("1.PATH_TO_DATA_DIRECTORY")
      println("2.PATH_TO_RESULT_DIRECTORY")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      sys.exit()
    }

    //文件路径
    val book_id_author = args(0) + "/book_id_author.txt"
    val book_id_CLCId = args(0) + "/book_id_CLCId.txt"
    val cls_no_name = args(0) + "/cls_no_name.txt"
    val paper_id_paperId = args(0) + "/paper_id_paperId.txt"
    val paper_paperID_author = args(0) + "/paper_paperID_author.txt"
    val paper_paperId_field = args(0) + "/paper_paperId_field.txt"
    val paper_paperId_indexTerm = args(0) + "/paper_paperId_indexTerm.txt"
    val result_file = args(1)

    //根据作者分析论文与图书联系
    val bookAuthorIdRDD = GetBookAuthorIdRDD.work(book_id_author)
    val paperAuthorIdRDD = GetPaperAuthorIdRDD.work(paper_id_paperId, paper_paperID_author)
    val paperBookRelationshipListByAuthor = paperAuthorIdRDD.join(bookAuthorIdRDD).map(tuple => {
      //(Int, Int)
      tuple._2
    })
    //缓存
    paperBookRelationshipListByAuthor.cache()

    //根据论文关键词和图书中图分类名分析论文与图书联系
    val bookCLCNameIdRDD = GetBookCLCNameIdRDD.work(book_id_CLCId, cls_no_name)
    val paperIndexTermIdRDD = GetPaperIndexTermIdRDD.work(paper_id_paperId, paper_paperId_indexTerm)
    val paperBookRelationshipListByIndexTermAndCLCName = paperIndexTermIdRDD.join(bookCLCNameIdRDD).map(tuple => {
      //(Int, Int)
      tuple._2
    })
    //缓存
    paperBookRelationshipListByIndexTermAndCLCName.cache()

    //根据论文领域名和图书中图分类名分析论文与图书联系
    val paperFieldIdRDD = GetPaperFieldIdRDD.work(paper_id_paperId, paper_paperId_field)
    val paperBookRelationshipListByFieldAndCLCName = paperFieldIdRDD.join(bookCLCNameIdRDD).map(tuple => {
      //(Int, Int)
      tuple._2
    })
    //缓存
    paperBookRelationshipListByFieldAndCLCName.cache()

    //合并后去重
    val paperBookRelationship = paperBookRelationshipListByAuthor
      .union(paperBookRelationshipListByIndexTermAndCLCName)
      .union(paperBookRelationshipListByFieldAndCLCName)
      .distinct()

    println(paperBookRelationship.count())

    //保存文本文件
    paperBookRelationship.saveAsTextFile(result_file)

    //停止
    spark.stop()
  }
}

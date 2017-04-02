package edu.libsys

import edu.libsys.stats._
import edu.libsys.util.{Filter, StringUtils}
import org.apache.spark.sql.SparkSession

object Main {

  //创建会话
  val spark: SparkSession = SparkSession
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
    //来源数据文件路径
    val book_id_author = args(0) + "/book_id_author.txt"
    val book_id_CLCId = args(0) + "/book_id_CLCId.txt"
    val cls_no_name = args(0) + "/cls_no_name.txt"
    val paper_id_paperId = args(0) + "/paper_id_paperId.txt"
    val paper_paperID_author = args(0) + "/paper_paperID_author.txt"
    val paper_paperId_field = args(0) + "/paper_paperId_field.txt"
    val paper_paperId_indexTerm = args(0) + "/paper_paperId_indexTerm.txt"
    //结果数据文件保存路径
    val result_file_bookBookRelationshipByAuthorRDD = args(1) + "/BookBookRelationshipByAuthor"
    val result_file_bookBookRelationshipByCLCIdRDD = args(1) + "/BookBookRelationshipByCLCId"
    val result_file_paperPaperRelationshipByAuthorRDD = args(1) + "/PaperPaperRelationshipByAuthor"
    val result_file_paperPaperRelationshipByFieldRDD = args(1) + "/PaperPaperRelationshipByField"
    val result_file_paperPaperRelationshipByIndexTermRDD = args(1) + "/PaperPaperRelationshipByIndexTerm"
    val result_file_paperBookRelationshipByAuthorRDD = args(1) + "/PaperBookRelationshipByAuthor"
    val result_file_paperBookRelationshipByFieldAndCLCNameRDD = args(1) + "/PaperBookRelationshipByFieldAndCLCName"
    val result_file_paperBookRelationshipByIndexTermAndCLCNameRDD = args(1) + "/PaperBookRelationshipByIndexTermAndCLCName"

    //根据作者，论文与图书关联
    val bookAuthorIdRDD = GetBookAuthorIdRDD.work(book_id_author).distinct()

    val paperAuthorIdRDD = GetPaperAuthorIdRDD.work(paper_id_paperId, paper_paperID_author).distinct()
    val paperBookRelationshipByAuthorRDD = paperAuthorIdRDD
      .join(bookAuthorIdRDD)
      .map(tuple => {
        //tuple._2
        //转为字符串，以便导入neo4j
        StringUtils.doubleTupleToString(tuple._2)
      })
    //根据作者，图书与图书关联
    val bookBookRelationshipByAuthorRDD = bookAuthorIdRDD
      .join(bookAuthorIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        //转为字符串，以便导入neo4j
        StringUtils.doubleTupleToString(tuple._2)
      })
    //根据作者，论文与论文关联
    val paperPaperRelationshipByAuthorRDD = paperAuthorIdRDD
      .join(paperAuthorIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        //tuple._2
        //转为字符串，以便导入neo4j
        StringUtils.doubleTupleToString(tuple._2)
      })

    //根据论文关键词和图书中图分类名分析论文与图书关联
    val bookCLCNameIdRDD = GetBookCLCNameIdRDD.work(book_id_CLCId, cls_no_name).distinct()
    val paperIndexTermIdRDD = GetPaperIndexTermIdRDD.work(paper_id_paperId, paper_paperId_indexTerm).distinct()
    val paperBookRelationshipByIndexTermAndCLCNameRDD = paperIndexTermIdRDD
      .join(bookCLCNameIdRDD)
      .map(tuple => {
        //tuple._2
        //转为字符串，以便导入neo4j
        StringUtils.doubleTupleToString(tuple._2)
      })

    //根据论文关键词，论文与论文关联
    val paperPaperRelationshipByIndexTermRDD = paperIndexTermIdRDD
      .join(paperIndexTermIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        //tuple._2
        //转为字符串，以便导入neo4j
        StringUtils.doubleTupleToString(tuple._2)
      })

    //根据论文领域名称和图书中图分类名分析论文与图书关联
    val paperFieldIdRDD = GetPaperFieldIdRDD.work(paper_id_paperId, paper_paperId_field).distinct()
    val paperBookRelationshipByFieldAndCLCNameRDD = paperFieldIdRDD
      .join(bookCLCNameIdRDD)
      .map(tuple => {
        //(Int, Int)
        //tuple._2
        //转为字符串，以便导入neo4j
        StringUtils.doubleTupleToString(tuple._2)
      })

    //根据论文领域名称，论文与论文关联
    val paperPaperRelationshipByFieldRDD = paperFieldIdRDD
      .join(paperFieldIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        //tuple._2
        //转为字符串，以便导入neo4j
        StringUtils.doubleTupleToString(tuple._2)
      })

    //根据中图分类号，图书与图书关联
    val bookCLCIdIdRDD = stats.GetBookCLCIdIdRDD.work(book_id_CLCId)
    val bookBookRelationshipByCLCIdRDD = bookCLCIdIdRDD
      .join(bookCLCIdIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        StringUtils.doubleTupleToString(tuple._2)
      })

    //保存数据
    bookBookRelationshipByAuthorRDD.saveAsTextFile(result_file_bookBookRelationshipByAuthorRDD)
    bookBookRelationshipByCLCIdRDD.saveAsTextFile(result_file_bookBookRelationshipByCLCIdRDD)
    paperPaperRelationshipByAuthorRDD.saveAsTextFile(result_file_paperPaperRelationshipByAuthorRDD)
    paperPaperRelationshipByFieldRDD.saveAsTextFile(result_file_paperPaperRelationshipByFieldRDD)
    paperPaperRelationshipByIndexTermRDD.saveAsTextFile(result_file_paperPaperRelationshipByIndexTermRDD)
    paperBookRelationshipByAuthorRDD.saveAsTextFile(result_file_paperBookRelationshipByAuthorRDD)
    paperBookRelationshipByFieldAndCLCNameRDD.saveAsTextFile(result_file_paperBookRelationshipByFieldAndCLCNameRDD)
    paperBookRelationshipByIndexTermAndCLCNameRDD.saveAsTextFile(result_file_paperBookRelationshipByIndexTermAndCLCNameRDD)

    //停止
    spark.stop()
  }
}

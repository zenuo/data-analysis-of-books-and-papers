package edu.libsys

import edu.libsys.stats._
import edu.libsys.util.Filter
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
    val book_id_author = args(0) + "/book_id_author.txt"
    val book_id_CLCId = args(0) + "/book_id_CLCId.txt"
    val cls_no_name = args(0) + "/cls_no_name.txt"
    val paper_id_paperId = args(0) + "/paper_id_paperId.txt"
    val paper_paperID_author = args(0) + "/paper_paperID_author.txt"
    val paper_paperId_field = args(0) + "/paper_paperId_field.txt"
    val paper_paperId_indexTerm = args(0) + "/paper_paperId_indexTerm.txt"
    val result_file_PaperBookRelationship = args(1) + "_PaperBookRelationship"
    val result_file_BookBookRelationship = args(1) + "_BookBookRelationship"
    val result_file_PaperPaperRelationship = args(1) + "_PaperPaperRelationship"

    //根据作者分析论文与图书联系
    val bookAuthorIdRDD = GetBookAuthorIdRDD.work(book_id_author).distinct()
    val paperAuthorIdRDD = GetPaperAuthorIdRDD.work(paper_id_paperId, paper_paperID_author).distinct()
    val paperBookRelationshipListByAuthor = paperAuthorIdRDD
      .join(bookAuthorIdRDD)
      .map(tuple => {
        //(Int, Int)
        tuple._2
      })
    //根据作者，图书与图书关联
    val joinedBookBookRelationshipByAuthorRDD = bookAuthorIdRDD
      .join(bookAuthorIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        tuple._2
      })
    //根据作者，论文与论文关联
    val joinedPaperPaperRelationshipByAuthorRDD = paperAuthorIdRDD
      .join(paperAuthorIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        tuple._2
      })

    //根据论文关键词和图书中图分类名分析论文与图书联系
    val bookCLCNameIdRDD = GetBookCLCNameIdRDD.work(book_id_CLCId, cls_no_name).distinct()
    val paperIndexTermIdRDD = GetPaperIndexTermIdRDD.work(paper_id_paperId, paper_paperId_indexTerm).distinct()
    val joinedPaperBookRelationshipListByIndexTermAndCLCName = paperIndexTermIdRDD
      .join(bookCLCNameIdRDD)
      .map(tuple => {
        //(Int, Int)
        tuple._2
      })
    //根据中图分类名，图书与图书关联
    val joinedBookBookRelationshipByCLCNameRDD = bookCLCNameIdRDD
      .join(bookCLCNameIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        tuple._2
      })
    //根据论文关键词，论文与论文关联
    val joinedPaperPaperRelationshipByIndexTermRDD = paperIndexTermIdRDD
      .join(paperIndexTermIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        tuple._2
      })

    //根据论文领域名和图书中图分类名分析论文与图书联系
    val paperFieldIdRDD = GetPaperFieldIdRDD.work(paper_id_paperId, paper_paperId_field).distinct()
    val joinedPaperBookRelationshipListByFieldAndCLCName = paperFieldIdRDD.join(bookCLCNameIdRDD)
      .map(tuple => {
        //(Int, Int)
        tuple._2
      })
    //根据论文关键词，论文与论文关联
    val joinedPaperPaperRelationshipByFieldRDD = paperFieldIdRDD
      .join(paperFieldIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        tuple._2
      })

    //图书与图书关联
    val bookBookRelationshipRDD = joinedBookBookRelationshipByAuthorRDD.distinct()
      .union(joinedBookBookRelationshipByCLCNameRDD.distinct())
      .distinct()
    //保存
    bookBookRelationshipRDD.saveAsTextFile(result_file_BookBookRelationship)

    //论文与论文关联
    val paperPaperRelationshipRDD = joinedPaperPaperRelationshipByAuthorRDD.distinct()
      .union(joinedPaperPaperRelationshipByIndexTermRDD.distinct())
      .union(joinedPaperPaperRelationshipByFieldRDD.distinct())
      .distinct()
    //保存
    paperPaperRelationshipRDD.saveAsTextFile(result_file_PaperPaperRelationship)

    //论文与图书关联
    val paperBookRelationshipRDD = paperBookRelationshipListByAuthor.distinct()
      .union(joinedPaperBookRelationshipListByIndexTermAndCLCName.distinct())
      .union(joinedPaperBookRelationshipListByFieldAndCLCName.distinct())
      .distinct()
    //保存
    paperBookRelationshipRDD.saveAsTextFile(result_file_PaperBookRelationship)

    //停止
    spark.stop()
  }
}

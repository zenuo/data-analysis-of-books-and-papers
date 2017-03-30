package edu.libsys.util

import edu.libsys.stats._
import org.apache.spark.sql.SparkSession

object Test {
  val spark: SparkSession = SparkSession
    .builder()
    .appName("PaperRelationship")
    .getOrCreate()

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
    val result_file = args(1) + "_PaperPaperRelationship"

    val paperAuthorIdRDD = GetPaperAuthorIdRDD.work(paper_id_paperId, paper_paperID_author).distinct()
    val joinedPaperPaperRelationshipByAuthorRDD = paperAuthorIdRDD
      .join(paperAuthorIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        tuple._2
      })

    val paperIndexTermIdRDD = GetPaperIndexTermIdRDD.work(paper_id_paperId, paper_paperId_indexTerm).distinct()
    val joinedPaperPaperRelationshipByIndexTermRDD = paperIndexTermIdRDD
      .join(paperIndexTermIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        tuple._2
      })

    val paperFieldIdRDD = GetPaperFieldIdRDD.work(paper_id_paperId, paper_paperId_field).distinct()
    val joinedPaperPaperRelationshipByFieldRDD = paperFieldIdRDD
      .join(paperFieldIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        tuple._2
      })

    val paperPaperRelationshipRDD = joinedPaperPaperRelationshipByAuthorRDD
      .union(joinedPaperPaperRelationshipByIndexTermRDD)
      .union(joinedPaperPaperRelationshipByFieldRDD)

    //保存
    paperPaperRelationshipRDD.saveAsTextFile(result_file)

    spark.stop()
  }
}

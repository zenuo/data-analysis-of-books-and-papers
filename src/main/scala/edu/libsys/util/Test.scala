package edu.libsys.util

import edu.libsys.stats.{GetBookVertices, GetPaperVertices}
import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.sql.SparkSession

object Test {

  //创建会话
  val spark: SparkSession = SparkSession
    .builder()
    .appName("MainStats")
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
      spark.stop()
      sys.exit(1)
    }

    //文件路径
    //来源数据文件路径
    val book_id_author = args(0) + "/book_id_author.txt"
    val book_id_CLCId = args(0) + "/book_id_CLCId.txt"
    val cls_no_name = args(0) + "/cls_no_name.txt"
    val paper_id_paperId = args(0) + "/paper_id_paperId.txt"
    val paper_paperID_author = args(0) + "/paper_paperId_author.txt"
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

    val bookVerticesRDD = GetBookVertices.work(book_id_author)
    val paperVerticesRDD = GetPaperVertices.work(paper_id_paperId)

    val verticesRDD = bookVerticesRDD.union(paperVerticesRDD)

    val paperBookrelationshipByAuthor = spark.sparkContext.textFile("/home/spark/Project/data/csv/PaperBookRelationshipByAuthor.csv")
      .map(line => {
        val delimiter01 = ","
        val tokens = line.replace("\"", "")
          .split(delimiter01)
          .map(_.trim)
        Edge(tokens(0).toLong + 1000000000, tokens(1).toLong, 2)
      })
    val paperPaperRelationshipByAuthor = spark.sparkContext.textFile("/home/spark/Project/data/csv/PaperPaperRelationshipByAuthor.csv")
      .map(line => {
        val delimiter01 = ","
        val tokens = line.replace("\"", "")
          .split(delimiter01)
          .map(_.trim)
        Edge(tokens(0).toLong + 1000000000, tokens(1).toLong + 1000000000, 1)
      })

    val edgesRDD = paperBookrelationshipByAuthor.union(paperPaperRelationshipByAuthor)
    val graph = Graph(verticesRDD, edgesRDD)

    graph.cache()


    spark.stop()
  }
}

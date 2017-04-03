package edu.libsys

import edu.libsys.stats._
import edu.libsys.util.{EdgeUtil, Filter}
import org.apache.spark.graphx.Graph
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
    val paper_paperID_author = args(0) + "/paper_paperId_author.txt"
    val paper_paperId_field = args(0) + "/paper_paperId_field.txt"
    val paper_paperId_indexTerm = args(0) + "/paper_paperId_indexTerm.txt"
    //结果数据文件保存路径


    //图书与论文在作者上的联系 3
    val bookAuthorIdRDD = GetBookAuthorIdRDD.work(book_id_author).distinct()
    val paperAuthorIdRDD = GetPaperAuthorIdRDD.work(paper_id_paperId, paper_paperID_author).distinct()
    val paperBookRelationshipByAuthorRDD = paperAuthorIdRDD
      .join(bookAuthorIdRDD)
      .map(tuple => {
        //Edge(tuple._2._1.toLong, tuple._2._1.toLong, 3)
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 3)
      })
    //图书与图书在作者上的联系 3
    val bookBookRelationshipByAuthorRDD = bookAuthorIdRDD
      .join(bookAuthorIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        //Edge(tuple._2._1.toLong, tuple._2._2.toLong, 3)
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 3)
      })
    //论文与论文在作者上的联系 3
    val paperPaperRelationshipByAuthorRDD = paperAuthorIdRDD
      .join(paperAuthorIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        //Edge(tuple._2._1.toLong, tuple._2._2.toLong, 3)
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 3)
      })

    //图书的中图法分类名与论文的关键词的联系 2
    val bookCLCNameIdRDD = GetBookCLCNameIdRDD.work(book_id_CLCId, cls_no_name).distinct()
    val paperIndexTermIdRDD = GetPaperIndexTermIdRDD.work(paper_id_paperId, paper_paperId_indexTerm).distinct()
    val paperBookRelationshipByIndexTermAndCLCNameRDD = paperIndexTermIdRDD
      .join(bookCLCNameIdRDD)
      .map(tuple => {
        //Edge(tuple._2._1.toLong, tuple._2._2.toLong, 2)
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 2)
      })

    //论文与论文在关键词上的联系 2
    val paperPaperRelationshipByIndexTermRDD = paperIndexTermIdRDD
      .join(paperIndexTermIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        //Edge(tuple._2._1.toLong, tuple._2._2.toLong, 2)
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 2)
      })

    //图书的中图法分类名与论文的领域名称的联系 1
    val paperFieldIdRDD = GetPaperFieldIdRDD.work(paper_id_paperId, paper_paperId_field).distinct()
    val paperBookRelationshipByFieldAndCLCNameRDD = paperFieldIdRDD
      .join(bookCLCNameIdRDD)
      .map(tuple => {
        //Edge(tuple._2._1.toLong, tuple._2._2.toLong, 1)
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 1)
      })

    //论文与论文在领域名称上的联系 1
    val paperPaperRelationshipByFieldRDD = paperFieldIdRDD
      .join(paperFieldIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        //Edge(tuple._2._1.toLong, tuple._2._2.toLong, 1)
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 1)
      })

    //图书与图书在中图法分类号上的联系 1
    val bookCLCIdIdRDD = stats.GetBookCLCIdIdRDD.work(book_id_CLCId)
    val bookBookRelationshipByCLCIdRDD = bookCLCIdIdRDD
      .join(bookCLCIdIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        //Edge(tuple._2._1.toLong, tuple._2._2.toLong, 1)
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 1)
      })

    //获得联系为建图作准备
    val bookBookRelationship = bookBookRelationshipByAuthorRDD
      .union(bookBookRelationshipByCLCIdRDD)
    val paperPaperRelationship = paperPaperRelationshipByAuthorRDD
      .union(paperPaperRelationshipByFieldRDD)
      .union(paperPaperRelationshipByIndexTermRDD)
    val paperBookRelationship = paperBookRelationshipByAuthorRDD
      .union(paperBookRelationshipByFieldAndCLCNameRDD)
      .union(paperBookRelationshipByIndexTermAndCLCNameRDD)

    //获得顶点为建图作准备
    //图书
    val bookVertices = GetBookVertices.work(book_id_author)
    //论文
    val paperVertices = GetPaperVertices.work(paper_id_paperId)

    //建图
    //图书与图书的联系
    val bookBookGraph = Graph(bookVertices, bookBookRelationship)

    //论文与论文的联系
    val paperPaperGraph = Graph(paperVertices, paperPaperRelationship)

    //论文与图书的联系
    val paperBookGraph = Graph(bookVertices.union(paperVertices), paperBookRelationship)

    //按各联系权重合并重复边
    //合并重复边后的图书与图书的联系
    val mergedEdgesBookBookGraph: Graph[Int, Int] = bookBookGraph.groupEdges(merge = (edgeWeight01, edgeWeight02) => edgeWeight01 + edgeWeight02)

    //合并重复边后的论文与论文的联系
    val mergedEdgesPaperPaperGraph: Graph[Int, Int] = paperPaperGraph.groupEdges(merge = (edgeWeight01, edgeWeight02) => edgeWeight01 + edgeWeight02)

    //合并重复边后的论文与图书的联系
    val mergedEdgesPaperBookGraph: Graph[Int, Int] = paperBookGraph.groupEdges(merge = (edgeWeight01, edgeWeight02) => edgeWeight01 + edgeWeight02)

    println("bookBookGraph: " + bookBookGraph.edges.count)
    //bookBookGraph: 547226
    println("paperPaperGraph: " + paperPaperGraph.edges.count)
    //paperPaperGraph: 46472
    println("paperBookGraph: " + paperBookGraph.edges.count)
    //paperBookGraph: 1771

    println("mergedEdgesBookBookGraph :" + mergedEdgesBookBookGraph.edges.count)
    //mergedEdgesBookBookGraph :547226
    println("mergedEdgesPaperPaperGraph :" + mergedEdgesPaperPaperGraph.edges.count)
    //mergedEdgesPaperPaperGraph :46336
    println("mergedEdgesPaperBookGraph :" + mergedEdgesPaperBookGraph.edges.count)
    //mergedEdgesPaperBookGraph :1771

    mergedEdgesBookBookGraph.vertices.take(10).foreach(println)
    mergedEdgesBookBookGraph.edges.take(10).foreach(println)

    //停止
    spark.stop()
  }
}

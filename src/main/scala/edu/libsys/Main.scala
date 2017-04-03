package edu.libsys

import edu.libsys.stats._
import edu.libsys.util.{EdgeUtil, Filter}
import org.apache.spark.graphx.{Graph, VertexRDD}
import org.apache.spark.rdd.RDD
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
    val verticesResultPath = args(1) + "/vertices"
    val edgeResultPath = args(1) + "/edges"

    //图书与论文在作者上的联系 3
    val bookAuthorIdRDD = GetBookAuthorIdRDD.work(book_id_author).distinct()
    val paperAuthorIdRDD = GetPaperAuthorIdRDD.work(paper_id_paperId, paper_paperID_author).distinct()
    val paperBookRelationshipByAuthorRDD = paperAuthorIdRDD
      .join(bookAuthorIdRDD)
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 3)
      })
    //图书与图书在作者上的联系 3
    val bookBookRelationshipByAuthorRDD = bookAuthorIdRDD
      .join(bookAuthorIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 3)
      })
    //论文与论文在作者上的联系 3
    val paperPaperRelationshipByAuthorRDD = paperAuthorIdRDD
      .join(paperAuthorIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 3)
      })

    //图书的中图法分类名与论文的关键词的联系 2
    val bookCLCNameIdRDD = GetBookCLCNameIdRDD.work(book_id_CLCId, cls_no_name).distinct()
    val paperIndexTermIdRDD = GetPaperIndexTermIdRDD.work(paper_id_paperId, paper_paperId_indexTerm).distinct()
    val paperBookRelationshipByIndexTermAndCLCNameRDD = paperIndexTermIdRDD
      .join(bookCLCNameIdRDD)
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 2)
      })

    //论文与论文在关键词上的联系 2
    val paperPaperRelationshipByIndexTermRDD = paperIndexTermIdRDD
      .join(paperIndexTermIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 2)
      })

    //图书的中图法分类名与论文的领域名称的联系 1
    val paperFieldIdRDD = GetPaperFieldIdRDD.work(paper_id_paperId, paper_paperId_field).distinct()
    val paperBookRelationshipByFieldAndCLCNameRDD = paperFieldIdRDD
      .join(bookCLCNameIdRDD)
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 1)
      })

    //论文与论文在领域名称上的联系 1
    val paperPaperRelationshipByFieldRDD = paperFieldIdRDD
      .join(paperFieldIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 1)
      })

    //图书与图书在中图法分类号上的联系 1
    val bookCLCIdIdRDD = stats.GetBookCLCIdIdRDD.work(book_id_CLCId)
    val bookBookRelationshipByCLCIdRDD = bookCLCIdIdRDD
      .join(bookCLCIdIdRDD)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, 1)
      })

    //获得所有联系
    val relationship = bookBookRelationshipByAuthorRDD
      .union(bookBookRelationshipByCLCIdRDD)
      .union(paperPaperRelationshipByAuthorRDD)
      .union(paperPaperRelationshipByFieldRDD)
      .union(paperPaperRelationshipByIndexTermRDD)
      .union(paperBookRelationshipByAuthorRDD)
      .union(paperBookRelationshipByFieldAndCLCNameRDD)
      .union(paperBookRelationshipByIndexTermAndCLCNameRDD)

    //获得顶点为建图作准备
    //图书
    val bookVertices = GetBookVertices.work(book_id_author)
    //论文
    val paperVertices = GetPaperVertices.work(paper_id_paperId)
    //获得顶点
    val vertices = bookVertices
      .union(paperVertices)

    //建图
    val graph: Graph[Int, Int] = Graph(vertices, relationship)
    //graph vertices: 40141
    //graph edges: 595469

    //按各联系权重合并重复边
    val mergedEdgesGraph: Graph[Int, Int] = graph.groupEdges(merge = (edgeWeight01, edgeWeight02) => edgeWeight01 + edgeWeight02)
    mergedEdgesGraph.cache()

    //计算各个顶点的边的权重之和，将和作为顶点的属性，即重要性
    //注，本RDD不包括所有的顶点
    //RDD[(Int, Int)]
    val partOfVerticesWithWeight: VertexRDD[Int] = mergedEdgesGraph.aggregateMessages[Int](
      triplet => {
        //发送边的权重给dst顶点
        triplet.sendToDst(triplet.attr)
      },
      //相加
      (a, b) => a + b
    )

    //获得含有属性的所有顶点
    //RDD[(Int, Int)]
    val verticesWithWeight: VertexRDD[Int] = graph.outerJoinVertices(partOfVerticesWithWeight)((vertexId, attr, weight) => weight.getOrElse(0)).vertices

    //字符串RDD，准备输出
    val verticesStringRDD: RDD[String] = verticesWithWeight.map(vertex => {
      vertex._1.toString + "," + vertex._2.toString
    })

    val edgesStringRDD: RDD[String] = mergedEdgesGraph.edges.map(edge => {
      edge.srcId.toString + "," + edge.dstId.toString + "," + edge.attr
    })

    //verticesStringRDD.saveAsTextFile(verticesResultPath)
    //edgesStringRDD.saveAsTextFile(edgeResultPath)

    println(mergedEdgesGraph.edges.count())

    //停止
    spark.stop()
  }
}

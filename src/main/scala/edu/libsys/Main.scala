package edu.libsys

import edu.libsys.conf.Conf
import edu.libsys.util._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Main {
  /**
    * 主方法
    *
    * @param args 控制台参数
    */
  def main(args: Array[String]): Unit = {
    //屏蔽不必要的日志输出
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    //判断文件参数是否正确
    if (args.length != 2) {
      println("-------------------------------ERROR-------------------------------")
      println("Valid program arguments:")
      println("1.PATH_TO_SOURCE_DATA_DIRECTORY")
      println("2.PATH_TO_RESULT_DATA_DIRECTORY")
      println("Usage:\n/usr/local/spark/bin/spark-submit --class edu.libsys.Main --master local --executor-memory 52G --total-executor-cores 6 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 /home/spark/book-stats-1.0.jar /home/spark/data /home/spark/result")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      return
    }

    /**
      * spark会话
      */
    val spark: SparkSession = SparkSession
      .builder()
      .appName("MainStats")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext

    //文件路径
    //来源数据文件路径
    val book_id_author: String = args(0) + "/book_id_author.txt"
    val book_id_CLCId: String = args(0) + "/book_id_CLCId.txt"
    val cls_no_name: String = args(0) + "/cls_no_name.txt"
    val paper_id_paperId: String = args(0) + "/paper_id_paperId.txt"
    val paper_paperID_author: String = args(0) + "/paper_paperId_author.txt"
    val paper_paperId_field: String = args(0) + "/paper_paperId_field.txt"
    val paper_paperId_indexTerm: String = args(0) + "/paper_paperId_indexTerm.txt"
    //结果数据文件保存路径
    val booksResultPath: String = args(1) + "/books"
    val papersResultPath: String = args(1) + "/papers"
    val bookBookRelationshipsResultPath: String = args(1) + "/bookBookRelationships"
    val paperPaperRelationshipsResultPath: String = args(1) + "/paperPaperRelationships"
    val bookPaperRelationshipsResultPath: String = args(1) + "/bookPaperRelationships"

    //图书与论文在作者上的联系
    val bookAuthorIdRDD: RDD[(String, Int)] =
      GetBookAuthorIdRDD.work(book_id_author, sc).distinct()
    val paperAuthorIdRDD: RDD[(String, Int)] =
      GetPaperAuthorIdRDD.work(paper_id_paperId, paper_paperID_author, sc).distinct()
    val paperBookRelationshipByAuthor: RDD[Edge[Int]] = paperAuthorIdRDD
      .join(bookAuthorIdRDD, Conf.numTasks)
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })
    //图书与图书在作者上的联系
    val bookBookRelationshipByAuthor: RDD[Edge[Int]] = bookAuthorIdRDD
      .join(bookAuthorIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })
    //论文与论文在作者上的联系
    val paperPaperRelationshipByAuthor: RDD[Edge[Int]] = paperAuthorIdRDD
      .join(paperAuthorIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })

    //图书的中图法分类名与论文的关键词的联系
    val bookCLCNameIdRDD: RDD[(String, Int)] =
      GetBookCLCNameIdRDD.work(book_id_CLCId, cls_no_name, sc).distinct()
    val paperIndexTermIdRDD: RDD[(String, Int)] =
      GetPaperIndexTermIdRDD.work(paper_id_paperId, paper_paperId_indexTerm, sc).distinct()
    val paperBookRelationshipByIndexTermAndCLCName: RDD[Edge[Int]] = paperIndexTermIdRDD
      .join(bookCLCNameIdRDD, Conf.numTasks)
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })

    //论文与论文在关键词上的联系
    val paperPaperRelationshipByIndexTerm: RDD[Edge[Int]] = paperIndexTermIdRDD
      .join(paperIndexTermIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })

    //图书的中图法分类名与论文的领域名称的联系
    val paperFieldIdRDD: RDD[(String, Int)] =
      GetPaperFieldIdRDD.work(paper_id_paperId, paper_paperId_field, sc).distinct()
    val paperBookRelationshipByFieldAndCLCName: RDD[Edge[Int]] =
      paperFieldIdRDD
        .join(bookCLCNameIdRDD, Conf.numTasks)
        .map(tuple => {
          EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
        })

    //论文与论文在领域名称上的联系
    val paperPaperRelationshipByField: RDD[Edge[Int]] =
      paperFieldIdRDD
        .join(paperFieldIdRDD, Conf.numTasks)
        .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
        .map(tuple => {
          EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
        })

    //图书与图书在中图法分类号上的联系
    val bookCLCIdIdRDD: RDD[(String, Int)] =
      GetBookCLCIdIdRDD.work(book_id_CLCId, sc)

    val bookBookRelationshipByCLCId: RDD[Edge[Int]] = bookCLCIdIdRDD
      .join(bookCLCIdIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })
    /*
    println("*********************************************************************************")
    //图书与图书在作者上的联系
    val bookBookRelationshipByAuthorcount = bookBookRelationshipByAuthor.count()
    println("图书与图书在作者上的联系(bookBookRelationshipByAuthor)：")
    println("共计：" + bookBookRelationshipByAuthorcount + "条；")
    println("前十条：")
    bookBookRelationshipByAuthor.take(10).foreach(println)

    println("*********************************************************************************")
    //图书与图书在中图法分类号上的联系
    val bookBookRelationshipByCLCIdCount = bookBookRelationshipByCLCId.count()
    println("图书与图书在中图法分类号上的联系(bookBookRelationshipByCLCId)：")
    println("共计：" + bookBookRelationshipByCLCIdCount + "条；")
    println("前十条：")
    bookBookRelationshipByCLCId.take(10).foreach(println)

    println("*********************************************************************************")
    //论文与论文在作者上的联系
    val paperPaperRelationshipByAuthorCount = paperPaperRelationshipByAuthor.count()
    println("论文与论文在作者上的联系(paperPaperRelationshipByAuthor)：")
    println("共计：" + paperPaperRelationshipByAuthorCount + "条；")
    println("前十条：")
    paperPaperRelationshipByAuthor.take(10).foreach(println)

    println("*********************************************************************************")
    //论文与论文在领域名称上的联系
    val paperPaperRelationshipByFieldCount = paperPaperRelationshipByField.count()
    println("论文与论文在领域名称上的联系(paperPaperRelationshipByField)：")
    println("共计：" + paperPaperRelationshipByFieldCount + "条；")
    println("前十条：")
    paperPaperRelationshipByField.take(10).foreach(println)

    println("*********************************************************************************")
    //论文与论文在关键词上的联系
    val paperPaperRelationshipByIndexTermCount = paperPaperRelationshipByIndexTerm.count()
    println("论文与论文在关键词上的联系(paperPaperRelationshipByIndexTerm)：")
    println("共计：" + paperPaperRelationshipByIndexTermCount + "条；")
    println("前十条：")
    paperPaperRelationshipByIndexTerm.take(10).foreach(println)

    println("*********************************************************************************")
    //图书与论文在作者上的联系
    val paperBookRelationshipByAuthorCount = paperBookRelationshipByAuthor.count()
    println("图书与论文在作者上的联系(paperBookRelationshipByAuthor)：")
    println("共计：" + paperBookRelationshipByAuthorCount + "条；")
    println("前十条：")
    paperBookRelationshipByAuthor.take(10).foreach(println)

    println("*********************************************************************************")
    //图书的中图法分类名与论文的领域名称的联系
    val paperBookRelationshipByFieldAndCLCNameCount = paperBookRelationshipByFieldAndCLCName.count()
    println("图书的中图法分类名与论文的领域名称的联系(paperBookRelationshipByFieldAndCLCName)：")
    println("共计：" + paperBookRelationshipByFieldAndCLCNameCount + "条；")
    println("前十条：")
    paperBookRelationshipByFieldAndCLCName.take(10).foreach(println)

    println("*********************************************************************************")
    //图书的中图法分类名与论文的关键词的联系
    val paperBookRelationshipByIndexTermAndCLCNameCount = paperBookRelationshipByIndexTermAndCLCName.count()
    println("图书的中图法分类名与论文的关键词的联系(paperBookRelationshipByIndexTermAndCLCName)：")
    println("共计：" + paperBookRelationshipByIndexTermAndCLCNameCount + "条；")
    println("前十条：")
    paperBookRelationshipByIndexTermAndCLCName.take(10).foreach(println)
    */

    //获得顶点为建图作准备
    //图书
    val bookVertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
    GetBookVertices.work(book_id_author, sc)
    //论文
    val paperVertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      GetPaperVertices.work(paper_id_paperId, sc)
    //获得顶点
    val vertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] = bookVertices
      .union(paperVertices)

    //println("*********************************************************************************")
    //val verticesCount = vertices.count()
    //println("图的顶点(vertices)：")
    //println("共计：" + verticesCount + "个；")
    //println("前十个：")
    //vertices.take(10).foreach(println)

    //建图，并按边的计数合并边
    val bookBookGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, bookBookRelationshipByAuthor)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    val bookBookGraphByCLCId: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, bookBookRelationshipByCLCId)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    val paperPaperGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperPaperRelationshipByAuthor)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    val paperPaperGraphByField: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperPaperRelationshipByField)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    val paperPaperGraphByIndexTerm: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperPaperRelationshipByIndexTerm)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    val bookPaperGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperBookRelationshipByAuthor)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    val bookPaperGraphByFieldAndCLCName: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperBookRelationshipByFieldAndCLCName)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    val bookPaperGraphByIndexTermAndCLCName: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperBookRelationshipByIndexTermAndCLCName)
        .groupEdges(merge = (count1, count2) => count1 + count2)

    //计算各个顶点的入度
    val inDegreesOfBookBookGraphByAuthor: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      bookBookGraphByAuthor
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (inDegreeTuple._2, 0, 0, 0, 0, 0, 0, 0))
        })
    val inDegreesOfBookBookGraphByCLCId: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      bookBookGraphByCLCId
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, inDegreeTuple._2, 0, 0, 0, 0, 0, 0))
        })
    val inDegreesOfPaperPaperGraphByAuthor: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      paperPaperGraphByAuthor
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, inDegreeTuple._2, 0, 0, 0, 0, 0))
        })
    /*
    val inDegreesOfPaperPaperGraphByField: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      paperPaperGraphByField
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, 0, inDegreeTuple._2, 0, 0, 0, 0))
        })
    */
    val inDegreesOfPaperPaperGraphByIndexTerm: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      paperPaperGraphByIndexTerm
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, 0, 0, inDegreeTuple._2, 0, 0, 0))
        })
    val inDegreesOfPaperBookGraphByAuthor: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      bookPaperGraphByAuthor
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, 0, 0, 0, inDegreeTuple._2, 0, 0))
        })
    val inDegreesOfPaperBookGraphByFieldAndCLCName: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      bookPaperGraphByFieldAndCLCName
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, 0, 0, 0, 0, inDegreeTuple._2, 0))
        })
    val inDegreesOfPaperBookGraphByIndexTermAndCLCName: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      bookPaperGraphByIndexTermAndCLCName
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, 0, 0, 0, 0, 0, inDegreeTuple._2))
        })

    //以所有顶点为顶点集合，建一个边集合为空的图
    val edges: RDD[Edge[Int]] = sc.parallelize(Seq(), 1)
    val graph: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] = Graph(vertices, edges)

    //收集顶点在“图书与图书在作者上的联系”上的入度
    val tempGraph01: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      graph.joinVertices(inDegreesOfBookBookGraphByAuthor) {
        (_, all, part) =>
          (part._1, 0, 0, 0, 0, 0, 0, 0)
      }

    val tempGraph02: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph01.joinVertices(inDegreesOfBookBookGraphByCLCId) {
        (_, part, all) =>
          (all._1, part._2, 0, 0, 0, 0, 0, 0)
      }

    val tempGraph03: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph02.joinVertices(inDegreesOfPaperPaperGraphByAuthor) {
        (_, all, part) =>
          (all._1, all._2, part._3, 0, 0, 0, 0, 0)
      }
    /*
    val tempGraph04: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph03.joinVertices(inDegreesOfPaperPaperGraphByField) {
        (_, all, part) =>
          (all._1, all._2, all._3, part._4, 0, 0, 0, 0)
      }
    */
    val tempGraph05: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph03.joinVertices(inDegreesOfPaperPaperGraphByIndexTerm) {
        (_, all, part) =>
          (all._1, all._2, all._3, all._4, part._5, 0, 0, 0)
      }
    val tempGraph06: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph05.joinVertices(inDegreesOfPaperBookGraphByAuthor) {
        (_, all, part) =>
          (all._1, all._2, all._3, all._4, all._5, part._6, 0, 0)
      }
    val tempGraph07: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph06.joinVertices(inDegreesOfPaperBookGraphByFieldAndCLCName) {
        (_, all, part) =>
          (all._1, all._2, all._3, all._4, all._5, all._6, part._7, 0)
      }
    val tempGraph08: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph07.joinVertices(inDegreesOfPaperBookGraphByIndexTermAndCLCName) {
        (_, all, part) =>
          (all._1, all._2, all._3, all._4, all._5, all._6, all._7, part._8)
      }


    //图书节点
    val books: RDD[String] = tempGraph08.vertices
      .filter(VertexUtil.GetVertexType(_) == 0)
      .map(VertexUtil.VertexToString(_, 0))
    //论文节点
    val papers: RDD[String] = tempGraph08.vertices
      .filter(VertexUtil.GetVertexType(_) == 1)
      .map(VertexUtil.VertexToString(_, 1))

    //图书与图书关联
    val bookBookRelationships = bookBookGraphByAuthor.edges
      .union(bookBookGraphByCLCId.edges)
      .filter(EdgeUtil.GetEdgeType(_) == 0)
      .map(EdgeUtil.EdgeToString(_, 0))
    //文与论文关联
    val paperPaperRelationships = paperPaperGraphByAuthor.edges
      .union(paperPaperGraphByIndexTerm.edges)
      .filter(EdgeUtil.GetEdgeType(_) == 1)
      .map(EdgeUtil.EdgeToString(_, 1))
    //图书与论文关联
    val bookPaperRelationships = bookPaperGraphByAuthor.edges
      .union(bookPaperGraphByFieldAndCLCName.edges)
      .union(bookPaperGraphByIndexTermAndCLCName.edges)
      .filter(EdgeUtil.GetEdgeType(_) == 2)
      .map(EdgeUtil.EdgeToString(_, 2))

    //保存到文本文件
    books.saveAsTextFile(booksResultPath)
    papers.saveAsTextFile(papersResultPath)
    bookBookRelationships.saveAsTextFile(bookBookRelationshipsResultPath)
    paperPaperRelationships.saveAsTextFile(paperPaperRelationshipsResultPath)
    bookPaperRelationships.saveAsTextFile(bookPaperRelationshipsResultPath)

    //停止
    sc.stop()
    spark.stop()
  }
}

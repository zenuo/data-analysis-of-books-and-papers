package edu.libsys.stats

import edu.libsys.conf.Conf
import edu.libsys.util.{EdgeUtil, Filter, GetPaperFieldIdRDD}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * 因为论文与论文在领域名词的关联数量很多，所以在此单独计算，但是本项目最终没有用本关联数据，故本文件可不看。
  */
object GetTextFileOfPaperPaperGraphByField {
  def main(args: Array[String]): Unit = {
    //屏蔽不必要的日志输出
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    //判断文件参数是否正确
    if (args.length != 2) {
      println("-------------------------------ERROR-------------------------------")
      println("Valid program arguments:")
      println("1.PATH_TO_SOURCE_DATA_DIRECTORY")
      println("2.PATH_TO_RESULT_DATA_DIRECTORY")
      println("Usage:\n/usr/local/spark/bin/spark-submit --class edu.libsys.stats.GetTextFileOfPaperPaperGraphByField --master local --executor-memory 52G --total-executor-cores 6 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 /home/spark/book-stats-1.0.jar /home/spark/data /home/spark/result")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      return
    }

    /**
      * spark会话
      */
    val spark: SparkSession = SparkSession
      .builder()
      .appName("GetTextFileOfPaperPaperGraphByField")
      .getOrCreate()
    //use the Kryo library to serialize objects
    spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc: SparkContext = spark.sparkContext

    val paper_id_paperId: String = args(0) + "/paper_id_paperId.txt"
    val paper_paperId_field: String = args(0) + "/paper_paperId_field.txt"

    val vertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] = sc.objectFile("/home/spark/data/graph/obj/vertices.obj")

    val paperFieldIdRDD: RDD[(String, Int)] =
      GetPaperFieldIdRDD.work(paper_id_paperId, paper_paperId_field, sc).distinct()

    //论文与论文在领域名称上的联系
    val paperPaperRelationshipByField: RDD[Edge[Int]] =
      paperFieldIdRDD
        .join(paperFieldIdRDD, Conf.numTasks)
        .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
        .map(tuple => {
          EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
        })

    val paperPaperGraphByField: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperPaperRelationshipByField)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    paperPaperGraphByField.edges.saveAsTextFile(args(1) + "/edgesOfPaperPaperGraphByField")

    sc.stop()
    spark.stop()
  }
}

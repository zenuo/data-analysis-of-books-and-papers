package edu.libsys.test

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object ReduceByKeyTest {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("ReduceByKeyTest")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext

    val list: RDD[(Int, String)] = sc.parallelize(Seq((1, "tom"), (1, "tom"), (1, "tom"), (1, "tom"), (2, "jim"), (2, "joy")), 1)

    val reducedRDD = list.reduceByKey(_ + "," + _)
    println(reducedRDD.count())
    reducedRDD.take(100).foreach(println)
    /*
    (1,tom,tom,tom,tom)
    (2,jim,joy)
     */
    sc.stop()
    spark.stop()
  }
}

package edu.libsys.test

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object KryoSerializationTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("KryoSerializationTest")
      .getOrCreate()

    spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val sc: SparkContext = spark.sparkContext

    val list: RDD[(Int, String)] = sc.parallelize(Seq((1, "tom"), (1, "tom"), (1, "tom"), (1, "tom"), (2, "jim"), (2, "joy")), 1)

    val reducedRDD: RDD[(Int, String)] = list.reduceByKey(_ + "," + _)
    reducedRDD.saveAsObjectFile("/home/yuanzhen/result")

    val rddFromObjectFile: RDD[(Int, String)] = sc.objectFile("/home/yuanzhen/result/part-00000", 1)
    rddFromObjectFile.collect().foreach(println)
    sc.stop()
    spark.stop()
  }
}

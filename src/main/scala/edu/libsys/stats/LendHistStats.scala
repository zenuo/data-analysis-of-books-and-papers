package edu.libsys.stats

import edu.libsys.data.dao.{ItemDao, LendRecordDao}
import edu.libsys.entity.Item
import org.apache.spark.sql.SparkSession

/*LEND_HIST.csv数据样例
“借阅者ID”，“借出时间”，“书籍编号”，“中图编号”
"99321117","2000-09-0209:54:56","0000040935","N49/81:1"
"13740057","2000-09-1808:41:25","0000042151","I227/126:2"
"13740057","2000-09-1808:41:34","0000020247","C913.14/9"
"99131309","2000-09-1311:12:01","0000010400","B992.3/5"
 */

/*ITEM.txt数据样例
财产号，书籍编号，借出次数
99000328,0000006280,0
97003188,0000006291,21
20029430,0000006298,0
00058147,0000006303,0
 */

object LendHistStats {

  //文件相关信息
  val LendHistFilePath = "/home/spark/Project/data/csv/LEND_HIST.csv.test"
  val ItemFilePath = "/home/spark/Project/data/txt/ITEM.txt-test"
  val delimiter = ","

  //主方法
  def main(args: Array[String]): Unit = {
    //create spark session
    val spark = SparkSession
      .builder()
      .appName("LendRecordStats")
      .getOrCreate()

    //创建一个LendRecordDao实例，以便后续的LendRecord的存储。
    val lendRecordDao = new LendRecordDao()
    val itemDao = new ItemDao()

    //将文件解析为如（财产号，（借阅者ID，借出时间））的元组列表。
    val lendHistList = spark.sparkContext.textFile(LendHistFilePath).map(line => {
      val pieces = line.replace("\"", "").split(delimiter)
      //返回元组
      (pieces(0), (pieces(2), pieces(1)))
    })
    //缓存
    lendHistList.cache()

    //将文件转为item对象并存入数据库，然后返回如（财产号，书籍编号）的元组列表。
    val itemList = spark.sparkContext.textFile(ItemFilePath).map(line => {
      val pieces = line.split(delimiter)
      //保存到数据库
      itemDao.addItem(new Item(pieces(1).toInt, pieces(0).toInt, pieces(2).toInt))
      //返回元组
      (pieces(0).toInt, pieces(1).toInt)
    })

    //缓存
    itemList.cache()

    /*
    //将lendHistList和itemList进行join操作，获得如（财产号，（（借阅者ID，借出时间），书籍编号））的元组列表，再将列表中的（（借阅者ID，借出时间），书籍编号）元组转换为LendHist对象，过滤后存入数据库。
    val joinedList = lendHistList.join(itemList)

    joinedList.foreach(f => {
      println(f._2._2 + ", " + f._2._1._1 + ", " + f._2._1._2)
    })
    */

    //停止
    spark.stop;
  }
}

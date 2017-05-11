package edu.libsys.conf

object Conf {
  /**
    * 论文编号偏移量
    */
  val paperIdOffset: Int = 1000000000

  /**
    * 任务数量
    */
  val numTasks: Int = 6

  /**
    * 分区数量
    */
  val numPartitions: Int = 20

  /**
    * 精确性权重
    */
  val weight1: (Int, Int, Int, Int, Int, Int, Int, Int) = (120, 4, 360, 1, 12, 1200, 90, 180)

  /**
    * 折中权重
    */
  val weight2: (Int, Int, Int, Int, Int, Int, Int, Int) = (50, 4, 50, 1, 12, 90, 90, 180)

  /**
    * 多样性权重
    */
  val weight3: (Int, Int, Int, Int, Int, Int, Int, Int) = (50, 20, 50, 20, 12, 90, 90, 180)
}

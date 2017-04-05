package edu.libsys.conf

object Conf {
  /**
    * 论文编号偏移量
    */
  val paperIdOffset: Int = 1000000000

  /**
    * 作者权重
    */
  val weightOfAuthor: Int = 3

  /**
    * 中图法分类号的权重
    */
  val weightOfCLCName: Int = 1

  /**
    * 领域名的权重
    */
  val weightOfField: Int = 1

  /**
    * 关键词的权重
    */
  val weightOfIndexTerm: Int = 2

  /**
    * 任务数量
    */
  val numTasks: Int = 6
}

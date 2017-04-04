package edu.libsys.util

object Filter {
  /**
    * 判断二元组的两个成员是否相等
    *
    * @param tuple 需要判断的二元组
    * @return Boolean
    */
  def isDoubleTupleLeftEqualsRight(tuple: Tuple2[Int, Int]): Boolean = {
    tuple._1 == tuple._2
  }
}

package edu.libsys.util

object Filter {
  def isDoubleTupleLeftEqualsRight(tuple: Tuple2[Int, Int]): Boolean = {
    tuple._1 == tuple._2
  }
}

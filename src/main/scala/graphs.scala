package com.graxp

import scala.collection.mutable.{HashMap, HashSet}

/**
  * @brief A mutable directed multigraph.
  */
trait MuDiGraph {
  def addEdge(n1: Int, n2: Int);
  def getOutgoing(n: Int): HashSet[Int];
  def getNumNodes(): Int;
}

class GraphImpl extends MuDiGraph {
  var adjMap: HashMap[Int, HashSet[Int]] = new HashMap();
  def addEdge(n1: Int, n2: Int) {
    this.adjMap.getOrElseUpdate(n1, new HashSet()) += n2.toInt;
  }
  def getOutgoing(n: Int): HashSet[Int] = {
    return this.adjMap.getOrElse(n, new HashSet());
  }
  def getNumNodes:Int = {
    return adjMap.size
  }
}

import org.apache.commons.io.{IOUtils, FileUtils}
import java.io.File

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable.{HashMap, HashSet}

import com.graxp._

import scala.io.Source

// export JAVA_OPTS="-Xmx16g -Xms1024m"
// sbt run

object graphServer {

  val configFile =
    Source.fromResource("remote_application.conf")
  val configStr = configFile.mkString
  val config = ConfigFactory.parseString(configStr)
  println(s"${config}");
  val system = ActorSystem("GraphExperimentsSystem", config)
  val remote = system.actorOf(Props[RemoteActor], name="RemoteActor")

  println("Server is ready")

  val it = FileUtils.lineIterator(new File("out.wikipedia_link_de"), "UTF-8");
  var G : MuDiGraph = new GraphImpl();

  def toInt(in: String): Option[Int] = {
    try {
        Some(Integer.parseInt(in.trim))
    } catch {
        case e: NumberFormatException => None
    }
  }

  class RemoteActor extends Actor {
    override def receive: Receive = {
      case msg: String => {
        println("remote received " + msg + " from " + sender)
        val pattern = "\\(([^ ]+) (.+)\\)".r
        msg match {
          case pattern("edges-from", args) => {
            toInt(args) match {
              case Some(n) =>
                sender ! "'(" ++ G.getOutgoing(n).mkString(" ") ++ ")"
            }
          }
        }
    }
    case _ => println("Received unknown msg ")
    }
  }

  def main(args: Array[String]) {
    println("Hell, O' World!");

    var i = 0;
    while (it.hasNext()) {

      i += 1;
      if (i % 1000000 == 0) {
        print(".");
      }

      val line = it.next();
      val edge = line split " ";

      //      println(s"${edge(0)} ${edge(1)}");
      try {
        val n1 = edge(0).toInt;
        val n2 = edge(1).toInt;
        G.addEdge(n1, n2);
      } catch {
        case e: java.lang.NumberFormatException => { println(e); }
      }
    }

    println();
    println("Done reading.")
  }
}

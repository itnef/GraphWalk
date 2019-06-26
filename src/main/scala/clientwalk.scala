import akka.actor._
import scala.concurrent.duration._
import scala.util.{ Success, Failure }
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext.Implicits.global

import com.graxp._
import scala.util.parsing.combinator._

class LocalActor extends Actor {

  val remote = context.actorSelection("akka.tcp://GraphExperimentsSystem@127.0.0.1:5150/user/RemoteActor")

  val resolveTimeout = Duration(40000, MILLISECONDS)
  val f = remote.resolveOne(resolveTimeout)
  var remoteActor: Option[akka.actor.ActorRef] = None
  var running = false

  f onComplete {
    case Success(actor) =>
      remoteActor = Some(actor)
    case Failure(e) => e.printStackTrace()
  }

  var currentNode = 1

  object ExprParser extends RegexParsers {
    override val skipWhitespace = false // API UX fail, thanks very much
    def digits: Parser[Int] = """\d+""".r ^^ { _.toInt }
    def spaceSepDigits: Parser[List[Int]] = repsep(digits, " ")
    def expr: Parser[List[Int]] = "'(" ~> spaceSepDigits <~ ")"
  }

  def receive = {
    case "START" =>
      println("Start")
      remote ! "(edges-from %d)".format(currentNode)
    case msg: String =>
      ExprParser.parseAll(ExprParser.expr, msg) match {
        case ExprParser.Success(list, _) => {
          println(list)
          if (list.size > 0) {
            val idx = scala.util.Random.nextInt(list.size)
            val next = list(idx)
            println(s"Choosing node $next")
            remote ! "(edges-from %d)".format(next)
          } else {
            println("No outgoing edges, quitting")
          }
        }
        case _ => {
          println(s"LocalActor received unparseable message: '$msg'")
        }
      }
      // should be "new OutEdgesQuery(0)" perhaps but this would require serialization code
  }
}

object clientWalk {
  def main(args: Array[String]) {
    val config = ConfigFactory.parseString("""
akka{
  actor {
    provider = "akka.remote.RemoteActorRefProvider"
  }
  remote {
    enabled-transports = ["akka.remote.netty.tcp"]
  }
}""");
    implicit val system = ActorSystem("LocalSystem", config)
    val localActor = system.actorOf(Props[LocalActor], name = "LocalActor")
    localActor ! "START"
  }
}

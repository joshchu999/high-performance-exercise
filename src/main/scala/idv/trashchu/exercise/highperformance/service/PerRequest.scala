package idv.trashchu.exercise.highperformance.service

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props, ReceiveTimeout}
import idv.trashchu.exercise.highperformance.entity._
import idv.trashchu.exercise.highperformance.service.PerRequest.{WithActorRef, WithProps}
import spray.http.StatusCode
import spray.http.StatusCodes._

import scala.concurrent.duration._
import spray.routing.RequestContext

/**
  * Created by Joshchu on 2016/7/23.
  */
trait PerRequest extends Actor with ActorLogging {

  import context._
  import idv.trashchu.exercise.highperformance.json.HighPerformanceJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  def reqContext: RequestContext
  def target: ActorRef
  def message: RequestEvent

  target ! message

  def receive = {

    case res: WalkResponseEvent =>
      reqContext.complete(OK, ResponseBody.convert(res))
      context stop self
    case res: CatchResponseEvent =>
      reqContext.complete(OK, ResponseBody.convert(res))
    case res: ListResponseEvent =>
      reqContext.complete(OK, ResponseBody.convert(res))
      context stop self
//    case v: Validation    => reqContext.complete(BadRequest, v)
//    case ReceiveTimeout   => complete(GatewayTimeout, Error("Request timeout"))
    case ReceiveTimeout   => complete(GatewayTimeout, null)
//    case result =>
  }

  def complete[T <: AnyRef](status: StatusCode, obj: T) = {
//    reqContext.complete(obj)
    context stop self
  }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case e =>
//        complete(InternalServerError, Error(e.getMessage))
        Stop
    }
}

object PerRequest {
  case class WithActorRef(reqContext: RequestContext, target: ActorRef, message: RequestEvent) extends PerRequest

  case class WithProps(reqContext: RequestContext, props: Props, message: RequestEvent) extends PerRequest {
    lazy val target = context.actorOf(props)
  }
}

trait PerRequestCreator {
  this: Actor =>

  def perRequest(reqContext: RequestContext, target: ActorRef, message: RequestEvent) =
    context.actorOf(Props(new WithActorRef(reqContext, target, message)))

  def perRequest(reqContext: RequestContext, props: Props, message: RequestEvent) =
    context.actorOf(Props(new WithProps(reqContext, props, message)))
}

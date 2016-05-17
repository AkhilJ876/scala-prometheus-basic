package com.badgeville.scala_prometheus_basic

import com.twitter.finagle.{Filter, IndividualRequestTimeoutException, RequestTimeoutException, Service}
import com.twitter.util.{Duration, Future, Timer}

/**
  * Created by AkhilJain on 5/16/16.
  */

class FinagleFilter[Request, Response](
                               timeout: Duration,
                               exception: RequestTimeoutException,
                               timer: Timer)
  extends Filter[Request, Response, Request, Response]
{
  def this(timeout: Duration, timer: Timer) =
    this(timeout, new IndividualRequestTimeoutException(timeout), timer)

  def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    val res = service(request)
    res.within(timer, timeout) rescue {
      case _: java.util.concurrent.TimeoutException =>
        println("Inside exception")
        Future.exception(exception)
    }
  }
}
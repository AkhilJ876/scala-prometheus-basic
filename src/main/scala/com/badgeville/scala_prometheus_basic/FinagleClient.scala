package com.badgeville.scala_prometheus_basic

import com.twitter.conversions.time._
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.util.DefaultTimer
import com.twitter.finagle.{Http, IndividualRequestTimeoutException}
import com.twitter.util.Await

/**
  * Created by AkhilJain on 5/16/16.
  */
object FinagleClient extends App {

  /*val client: Service[Request, Response] = Http.client.withRequestTimeout(2.microseconds).newService("www.scala-lang.org:80")
  val request = Request(Method.Get, "/")
  request.host = "www.scala-lang.org"
  val response: Future[Response] = client(request)
  Await.result(response)

    response.onSuccess { rep: Response =>
      println("GET success: " + rep)
    }

  val port = 30405
  val timeout = 1.milliseconds
  val exception = new IndividualRequestTimeoutException(timeout)
  val timer = DefaultTimer.twitter
  val timeoutFilter = new FinagleFilter[Request,Response](timeout, exception, timer)
  val service = timeoutFilter andThen new FinagleService

  val server=Http.server.serve(":"+port.toString,service)
  Await.ready(server)*/

  val port = 30405
  val timeout = 1.microseconds
  val exception = new IndividualRequestTimeoutException(timeout)
  val timer = DefaultTimer.twitter
  val timeoutFilter = new FinagleFilter[Request,Response](timeout,exception,timer)
  val requestFilter = RequestFilter()
  val service = timeoutFilter andThen requestFilter  andThen new FinagleService

  val server=Http.server.serve(":"+port.toString,service)
  Await.ready(server)
}


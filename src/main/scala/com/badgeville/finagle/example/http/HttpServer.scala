package com.badgeville.finagle.example.http

import java.net.InetSocketAddress
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.http.{Http, Request, Response, Status, Version}
import com.twitter.finagle.Service
import com.twitter.util.{Future => TFuture}
/**
  * Created by AkhilJain on 5/17/16.
  */
object HttpServer extends App {
  val myService = new UserResp
  val server: Server = ServerBuilder()
    .codec(Http())
    .bindTo(new InetSocketAddress(8081))
    .name("httpserver")
    .build(myService)
}

class Respond extends Service[Request, Response] {
  def apply(request: Request) = {
    Thread.sleep(10000)
    val requestUri = request.uri
    val response = Response(Version.Http11, Status.BadRequest)
    response.contentString = requestUri + "hello world"
    //TFuture.exception(new Exception)
    TFuture.value(response)
  }
}

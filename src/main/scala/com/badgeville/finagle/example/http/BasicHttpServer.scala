package com.badgeville.finagle.example.http

import java.net.InetSocketAddress

import com.twitter.finagle.Service
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.http._
import com.twitter.util.{Future => TFuture}

/**
  * Created by AkhilJain on 5/17/16.
  */
object BasicHttpServer extends App {
  val myService = new UserResp
  val server: Server = ServerBuilder()
    .codec(Http())
    .bindTo(new InetSocketAddress(8083))
    .name("BasicHttpServer")
    .build(myService)
}

class UserResp extends Service[Request, Response] {
  def apply(request: Request) = {
    val requestUri = request.uri
    val response = Response(Version.Http11, Status.Ok)
    response.contentString = requestUri + "Hello World"
    //TFuture.exception(new Exception)
    TFuture.value(response)
  }
}

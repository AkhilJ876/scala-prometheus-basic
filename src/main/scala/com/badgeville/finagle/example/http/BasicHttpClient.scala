package com.badgeville.finagle.example.http

import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, Future => TFuture}
import com.twitter.finagle.http.{Method, Request, Response, Status}

/**
  * Created by AkhilJain on 5/17/16.
  */
object BasicHttpClient extends App {
  val serverCall = Http.client.newService("localhost:8083", "client2server")
  val request = Request(Method.Get, "/")
  val response = serverCall(request)
  Await.result(response)
  response.onSuccess { rep: Response =>
    println("GET success: " + rep)
  }
}

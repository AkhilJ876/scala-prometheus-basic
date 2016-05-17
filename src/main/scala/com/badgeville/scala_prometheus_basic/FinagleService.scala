package com.badgeville.scala_prometheus_basic

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.{Future => tFuture}

/**
  * Created by AkhilJain on 5/16/16.
  */
class FinagleService extends Service[Request,Response] {
  def apply(request: Request):tFuture[Response]={
    val response=Response()
    Thread.sleep(100000)
    response.status=Status.Ok
    response.contentString="Hello World"
    tFuture.value(response)
  }
}

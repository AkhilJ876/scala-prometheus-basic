package com.badgeville.scala_prometheus_basic

import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.{Filter, Service}
import com.twitter.logging.Logger
import com.twitter.util.{Future=>TFuture}
import java.util.UUID
/**
  * Created by AkhilJain on 5/17/16.
  */
case class RequestFilter() extends  Filter[Request,Response,Request,Response]{
  private final val log = Logger()
  override def apply(request: Request,service: Service[Request,Response]):TFuture[Response]={
    try{
      service(request)  map {
        response => {
          (response.status.code, response.contentString) match {
            case (s, c) if s < 300 =>
              println("Hello Inside Request Filter Success")
              val r = Response(request.version, response.status)
              r.contentString=response.contentString
              r
            case (s, c) =>
              val r = Response(request.version, Status.Ok)
              println("Hello Inside Request Filter Fail")
              r.contentString = parseErrorMessage(response.status.code, response.status.reason, c)
              r
          }
        }
      }
    }catch {
      case t: Throwable =>
        val id = s"na-${UUID.randomUUID()}"
        log.error(t, s"""APP EXCEPTION - CODE $id - request: ${printRequest(request)}""")
        TFuture {
          val r = Response(request.version, Status.Ok)
          r.contentString = parseErrorMessage(500, "InternalServerError", s"Internal Server Error: Reference-ID: $id")
          r
        }
    }
  }
  def parseErrorMessage(code: Int, status: String, message: String) =
    s"""{"errors":[{"code":$code,"status":"$status","messages":["$message"]}]}"""

  def printRequest(request: Request) = {
    s"""Request(${request.method}: ${request.uri}, from ${request.remoteSocketAddress}, with parameters ${request.getParams}, with headers ${request.headerMap})"""
     }

}

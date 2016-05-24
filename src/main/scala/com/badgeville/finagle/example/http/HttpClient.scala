package com.badgeville.finagle.example.http

import java.util.UUID

import com.badgeville.finagle.filters.FinagleFilter
import com.twitter.conversions.time._
import com.twitter.finagle.http.{Request, Response, Status, Version}
import com.twitter.finagle.util.DefaultTimer
import com.twitter.finagle.{Filter, Http, IndividualRequestTimeoutException, Service}
import com.twitter.util.{Await, Future => TFuture}

/**
  * Created by AkhilJain on 5/17/16.
  */
object HttpClient extends App {
  val timeout = 1.minutes
  val exception = new IndividualRequestTimeoutException(timeout)
  val timer = DefaultTimer.twitter
  val timeoutFilter = new FinagleFilter[Request, Response](timeout, exception, timer)
  val serverCall = Http.client.newService("localhost:8081", "client2server")
  val req = timeoutFilter andThen new userRequest andThen serverCall
  val userReq = Http.server.serve("localhost:9011", req)
  Await.ready(userReq)

}

class userRequest extends Filter[Request, Response, Request, Response] {
  override def apply(request: Request, service: Service[Request, Response]): TFuture[Response] = {
    request.uri match {
      case null =>
        val response = Response(Version.Http11, Status.BadRequest)
        TFuture(response)
      case _ =>
        //Await.result(service(request))
        try {
          service(request) map {
            response =>
              {
                (response.status.code, response.contentString) match {
                  case (s, c) if s < 300 =>
                    println("Hello Inside Request Filter Success")
                    val r = Response(request.version, response.status)
                    r.contentString = response.contentString
                    r
                  case (s, c) =>
                    val r = Response(request.version, Status.Ok)
                    println("Hello Inside Request Filter Fail")
                    r.contentString = parseErrorMessage(response.status.code, response.status.reason, c)
                    r
                }
              }
          }
        } catch {
          case t: Throwable =>
            val id = s"na-${UUID.randomUUID()}"
            TFuture {
              val r = Response(request.version, Status.Ok)
              r.contentString = parseErrorMessage(500, "InternalServerError", s"Internal Server Error: Reference-ID: $id")
              r
            }
        }
    }
  }

  def parseErrorMessage(code: Int, status: String, message: String) =
    s"""{"errors":[{"code":$code,"status":"$status","messages":["$message"]}]}"""

  def printRequest(request: Request) = {
    s"""Request(${request.method}: ${request.uri}, from ${request.remoteSocketAddress}, with parameters ${request.getParams}, with headers ${request.headerMap})"""
  }
}


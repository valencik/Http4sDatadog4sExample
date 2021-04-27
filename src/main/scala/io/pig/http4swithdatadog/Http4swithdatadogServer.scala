package io.pig.http4swithdatadog

import cats.effect._
import cats.implicits._
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.server.middleware.Metrics
import org.http4s.server.Server
import scala.concurrent.ExecutionContext.global
import java.net.InetSocketAddress
import com.avast.datadog4s.api._
//import com.avast.datadog4s.api.metric._
import com.avast.datadog4s._
import com.avast.datadog4s.extension.http4s._

object Http4swithdatadogServer {

  val statsDServer = InetSocketAddress.createUnresolved("localhost", 8125)
  val config = StatsDMetricFactoryConfig(Some("http4swdog"), statsDServer)
  
  def factoryResource[F[_]: ConcurrentEffect]: Resource[F, MetricFactory[F]] = StatsDMetricFactory.make(config)

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F]): Resource[F, Server[F]] = {
    for {
      client <- BlazeClientBuilder[F](global).resource
      helloWorldAlg = HelloWorld.impl[F]
      jokeAlg = Jokes.impl[F](client)

      metricFactory <- factoryResource
      datadog <- Resource.eval(DatadogMetricsOps.builder[F](metricFactory).build())

      routes = Metrics[F](datadog)(
        Http4swithdatadogRoutes.helloWorldRoutes[F](helloWorldAlg)) <+>
        Http4swithdatadogRoutes.jokeRoutes[F](jokeAlg)

      httpApp = (routes).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      server <- BlazeServerBuilder[F](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .resource
    } yield server
  }
}

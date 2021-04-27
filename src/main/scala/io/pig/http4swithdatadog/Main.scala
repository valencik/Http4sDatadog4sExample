package io.pig.http4swithdatadog

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    Http4swithdatadogServer.stream[IO].use(_ => IO.never).as(ExitCode.Success)
}

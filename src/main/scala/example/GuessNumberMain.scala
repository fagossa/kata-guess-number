package example

import scala.util.{Random, Try}

object GuessNumberMain extends App {
  import scala.io.StdIn._
  def putString(s: String): IO[Unit] = IO(() => println(s))

  def readString(): IO[String] = IO(() => readLine())

  def readInt(): IO[Option[Int]] = IO(() => Try(readLine().toInt).toOption)

  def nextInt(max: Int): IO[Int] = IO(() => Random.nextInt(max))

  def continueGame(name: String): IO[Boolean] = {
    for {
      _ <- putString(s"Do you want to continue (y/n), $name?")
      resp <- readString()
      r <- resp match {
        case "y" => IO.pure(true)
        case "n" => IO.pure(false)
        case _ => continueGame(name)
      }
    } yield r
  }

  def gameLoop(name: String): IO[Unit] = {
    for {
      num <- nextInt(5).map(_ + 1)
      _ <- putString(s"Dear $name, please guess a number 1..5")
      maybeGuess <- readInt()
      _ <- maybeGuess match {
        case Some(guess) if guess == num =>
          putString(s"You guessed right, $name")
        case _ =>
          putString(s"You guessed wrong, $name, the number was $num")
      }
      nextLoop <- continueGame(name)
      _ <- if (nextLoop) gameLoop(name) else IO.pure()
    } yield ()
  }

  val program = for {
    _ <- putString("What is your name")
    name <- readString()
    _ <- putString(s"Hello, $name, welcome to the game!")
    _ <- gameLoop(name)
  } yield ()

  program.unsafeRun()
}

case class IO[A](unsafeRun: () => A) {
      self =>
      def map[B](f: A => B): IO[B] = IO(() => f(self.unsafeRun()))

      def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(self.unsafeRun()).unsafeRun())
}

object IO {
      def pure[A](f: => A): IO[A] = IO(() => f)
}

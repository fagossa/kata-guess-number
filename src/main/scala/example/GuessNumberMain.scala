package example

import scala.util.Random

object GuessNumberMain extends App {
  import scala.io.StdIn._
  println("What is your name")

  val name = readLine()

  println(s"Hello, $name, welcome to the game!")

  var exec = true

  while (exec) {
    val num = Random.nextInt(5) + 1

    println(s"Dear $name, please guess a number 1..5")

    val guess = readLine().toInt

    if (guess == num) println(s"You guessed right, $name")
    else println(s"You guessed wrong, $name, the number was $num")

    println(s"Do you want to continue (y/n), $name?")

    readLine() match {
      case "y" => exec = true
      case "n" => exec = false
    }
  }
}

case class IO[A](unsafeRun: () => A) {
      self =>
      def map[B](f: A => B): IO[B] = IO(() => f(self.unsafeRun()))

      def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(self.unsafeRun()).unsafeRun())
}

object IO {
      def pure[A](f: => A): IO[A] = IO(() => f)
}

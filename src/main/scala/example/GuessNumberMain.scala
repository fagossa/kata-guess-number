package example

import scala.util.{Random, Try}

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

    println(s"Do you want to continue, $name?")

    readLine() match {
      case "y" => exec = true
      case "n" => exec = false
    }
  }
}

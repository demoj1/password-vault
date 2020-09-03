package vault

object Helpers {

  implicit class Pipe[In, Out](self: In) {
    val |> = (f: In => Out) => f(self)
  }

}

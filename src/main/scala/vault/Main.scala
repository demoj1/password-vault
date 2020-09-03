package vault

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

import scala.util.Properties

object Main {
  def main(args: Array[String]): Unit = {
    val port = Properties.envOrElse("PORT", "8080") .toInt

    val server = new Server(port)
    val context = new WebAppContext()

    context.setResourceBase("src/main/webapp")
    context setContextPath "/"
    context.setInitParameter(ScalatraListener.LifeCycleKey, "ScalatraBootstrap")
    context.addEventListener(new ScalatraListener)

    server.setHandler(context)

    server.start()
    server.join()
  }
}

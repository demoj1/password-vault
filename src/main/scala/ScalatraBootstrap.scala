import javax.servlet.ServletContext
import org.scalatra._
import vault.controller.VaultController

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new VaultController, "/*")
  }
}

package vault

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.scalatra.auth.strategy.{BasicAuthStrategy, BasicAuthSupport}
import org.scalatra.ScalatraBase
import org.scalatra.auth.{ScentryConfig, ScentrySupport}

import scala.util.Properties

class OurBasicAuthStrategy(protected override val app: ScalatraBase, realm: String)
  extends BasicAuthStrategy[String](app, realm)
{
  val userName: String = Properties.envOrElse("BASIC_AUTH_USERNAME", "admin")
  val password: String = Properties.envOrElse("BASIC_AUTH_PASSWORD", "password")

  override protected def getUserId(user: String)(implicit request: HttpServletRequest, response: HttpServletResponse): String = user

  override protected def validate(userName: String, password: String)(implicit request: HttpServletRequest, response: HttpServletResponse): Option[String] = {
    if (userName == this.userName && password == this.password) Some("0")
    else None
  }
}

trait AuthenticationSupport extends ScentrySupport[String] with BasicAuthSupport[String] {
  self: ScalatraBase =>

  val realm = "Basic"

  protected def fromSession = { case id: String => id  }
  protected def toSession = { case usr: String => usr }

  protected val scentryConfig: ScentryConfiguration = new ScentryConfig {}.asInstanceOf[ScentryConfiguration]

  override protected def registerAuthStrategies = {
    scentry.register("Basic", app => new OurBasicAuthStrategy(app, realm))
  }
}
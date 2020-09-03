package vault.controller

import java.util.UUID

import org.scalatra.{InternalServerError, ScalatraServlet}
import vault.Helpers.Pipe
import vault.{AuthenticationSupport, Vault}

class VaultController extends ScalatraServlet with AuthenticationSupport {
  get("/") {
    basicAuth

    Vault.getRoot match {
      case Right(root) => views.html.index(root.items)
      case Left(e) => InternalServerError(e.getMessage)
    }
  }

  get("/new") {
    basicAuth

    views.html.edit()
  }

  post("/new") {
    basicAuth

    val res = params.toMap + ("id" -> UUID.randomUUID().toString) |> Vault.saveMapToFile

    res match {
      case Right(_) => redirect("/")
      case Left(e) => InternalServerError(e.getMessage)
    }
  }

  post("/delete/:index") {
    basicAuth

    val res = for {
      root <- Vault.removeFromList(params("index"))
      rootItems = root.items
    } yield rootItems

    res match {
      case Right(list) => redirect("/")
      case Left(e) => InternalServerError(e.getMessage)
    }
  }
}

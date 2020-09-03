package vault

import java.nio.file.{Files, Path}
import java.util.UUID
import java.util.logging.Level
import java.util.logging.Logger.getGlobal

import Helpers.Pipe
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import io.circe.yaml.parser._
import io.circe.yaml.syntax.AsYaml
import vault.dto.{Item, Root}

import scala.io.Source
import scala.util.Properties

object Vault {
  val filePath: String = Properties.envOrElse("DB_PATH", "./db.yml")

  def getRoot: Either[Error, Root] = {
    try {
      val file = filePath |> Source.fromFile
      for {
        root <- parse(file.reader)
        res <- root.as[Root]
      } yield res
    } catch {
      case _: Any =>
        getGlobal.log(Level.WARNING, "DB file not found!")

        val root = Root(List.empty)
        val emptyFile = root.asJson.asYaml.spaces2
        Files.write(Path.of(Vault.filePath), emptyFile.getBytes())

        Right(root)
    }
  }

  def saveMapToFile(map: Map[String, String]) = for {
    root <- getRoot
    jsonObject = map.asJson
    parsedItem <- jsonObject.as[Item]
    result <- updateItems(root, parsedItem :: root.items)
  } yield result

  def removeFromList(id: String) = for {
    root <- getRoot
    items = root.items
    newItems = items.filter(item => item.id != id)
    result <- updateItems(root, newItems)
  } yield result

  private def updateItems(root: Root, newItems: List[Item]) = for {
    root <- getRoot
    newRoot = root.copy(items = newItems)
    newFileData = newRoot.asJson.asYaml.spaces2
    _ = Files.write(Path.of(Vault.filePath), newFileData.getBytes())
  } yield newRoot
}

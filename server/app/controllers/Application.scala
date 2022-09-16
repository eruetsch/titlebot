package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.ws._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.libs.json.Json
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.Reads
import play.api.libs.json.JsPath
import play.api.libs.functional.syntax._
import play.api.libs.json.Writes
import scala.util.matching.Regex


@Singleton
class Application @Inject()(cc: ControllerComponents, implicit val ec: ExecutionContext, ws: WSClient) extends AbstractController(cc) {
  case class JsonInfo(name:String, icon:String)

  implicit val infoReads: Reads[JsonInfo] = (
  (JsPath \ "name").read[String] and
  (JsPath \ "icon").read[String]
  )(JsonInfo.apply _)
  
  implicit val infoWrites = new Writes[JsonInfo] {
    def writes(jsonInfo: JsonInfo) = Json.obj(
      "name" -> jsonInfo.name,
      "icon" -> jsonInfo.icon
    )
  }

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  //this would be done better with something like Jsoup
  val regExTitle = """<title>(.+)</title>""".r
  val regExLink = """<link.*?rel="((S|s)hortcut )?(I|i)con"(.+?)>""".r
  val regExIcon = """href="(.+?)"""".r

  def getURL = Action.async { implicit request  => 
    request.body.asText match {
      case Some(x) => 
        val site = "http://" + x.substring(1, x.length()-1)
        ws.url(site)
        .get().map{response => 
          val matchTitle = regExTitle findFirstMatchIn response.body
          val title = matchTitle.map(_.group(1)).getOrElse("no title on page")
          val matchIconTag = regExLink findFirstMatchIn response.body
          val iconTag = matchIconTag.map(_.group(0)).getOrElse(" no icon found on page")
          val matchIcon = regExIcon findFirstMatchIn iconTag          
          val icon = matchIcon.map(_.group(1)).getOrElse(" no icon url found in icon tag")          
          Ok(Json.parse(s"""{
              "name": "$title",
              "icon": "$site$icon"
              }"""
          ) )
        }
        
      case None => 
        println("no title submitted")
        Future.successful(Ok)
    }
  }

}

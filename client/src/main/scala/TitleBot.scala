
import org.scalajs.dom

import slinky.core._
import slinky.web.ReactDOM
import slinky.web.html._
import java.net.URL
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.Component
import org.scalajs.dom.experimental._
import play.api.libs.json.Json
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import scala.scalajs.js.Thenable.Implicits._
import scala.concurrent.ExecutionContext
import play.api.libs.json.Writes
import play.api.libs.json.JsPath
import play.api.libs.json.Reads
import play.api.libs.functional.syntax._

case class JsonInfo(name:String, icon:String)
@react class TitleBot extends Component {  
  type Props = Unit
  case class State(name: String, message: String, prevSearch:List[JsonInfo])
  
  implicit val infoReads: Reads[JsonInfo] = (
  (JsPath \ "name").read[String] and
  (JsPath \ "icon").read[String]
  )(JsonInfo.apply _)

  implicit val ec:ExecutionContext = ExecutionContext.global
  def initialState: State = State("", "", Nil)

  def render(): ReactElement = div (
    h2 ("URL Listing"),
    "Type URL here:",
    input (`type` := "text", value := state.name, onChange := (e => setState(state.copy(name = e.target.value)))),
    button ("Submit", onClick := (e => { onSubmit(); setState(state.copy(name = ""))})),
    ul(
      state.prevSearch.zipWithIndex.map{case (j, i) => li(span(img(src:=j.icon, width:= "30"), j.name), key := i.toString())}
    )
    )

  def onSubmit():Unit = {
    if (state.name.isEmpty) setState(state.copy(message = "URL is required."))
    else {
      val headers = new Headers()
      headers.set("Content-Type", "application/json")

      Fetch.fetch("/getURL", 
        new RequestInit {
          method = HttpMethod.PUT
          headers = headers 
          body = Json.toJson(state.name).toString
        }).flatMap(_.text()).map { res =>
        Json.fromJson[JsonInfo](Json.parse(res)) match {
          case JsSuccess(ret, path) => 
            setState(state.copy(prevSearch = ret :: state.prevSearch))
            println(ret)
          case e @ JsError(_) => 
            println("Fetch error " + e)
            //error(e)
        }
      } 
    }
  }
}


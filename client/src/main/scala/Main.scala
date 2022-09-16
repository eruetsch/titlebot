
import org.scalajs.dom

import slinky.core._
import slinky.web.ReactDOM
import slinky.web.html._
import org.scalajs.dom.raw.WebSocket

object Main {
  def main(args: Array[String]): Unit = {
      println("Call the react stuff.")
      ReactDOM.render(
        div(
          TitleBot(), 
        ),
          dom.document.getElementById("root")
        
      )

  }
}
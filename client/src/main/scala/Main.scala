
import org.scalajs.dom

import slinky.core._
import slinky.web.ReactDOM
import slinky.web.html._

object Main {
  def main(args: Array[String]): Unit = {
      ReactDOM.render(
        div(TitleBot()),
        dom.document.getElementById("root")
      )

  }
}
package controllers

import play.api._
import play.api.libs.ws.WS
import play.api.mvc._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def kitty = Action.async {
    WS.url("http://thecatapi.com/api/images/get?format=src&type=gif")
      .withFollowRedirects(false)
      .get()
      .map { data =>
      Ok(data.header("Location").getOrElse("")).as("text/plain")
    }
  }

}
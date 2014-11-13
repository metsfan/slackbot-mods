package controllers

import java.net.URLEncoder

import play.api._
import play.api.libs.ws.WS
import play.api.mvc._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  final val slackPostURL = "https://citymaps.slack.com/services/hooks/slackbot?token=Zl151cb5dnKoDR9VjjEq3TzN&channel=%s"

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def kitty = Action.async { implicit request =>
    println(request.rawQueryString)
    WS.url("http://thecatapi.com/api/images/get?format=src&type=gif")
      .withFollowRedirects(false)
      .get()
      .map { data =>
      postToSlackAPI(data.header("Location").getOrElse(""))
      Ok("")
    }
  }

  private def postToSlackAPI(content: String)
                            (implicit request: Request[AnyContent]): Unit = {
    val slackUrl = String.format(slackPostURL, URLEncoder.encode(request.getQueryString("channel_id").getOrElse(""), "UTF-8"))
    println("Posting to " + slackUrl)
    WS.url(slackUrl).post("Enjoy your kitty!\n" + content)
  }

}
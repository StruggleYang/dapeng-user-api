import com.today.api.user._
import com.today.api.user.request.BlackUserRequest

object Test extends App {

  override def main(args: Array[String]): Unit = {
    val res = UserServiceClient.blackUser(BlackUserRequest("1724555319@com","1724555319"))
    println(res)
  }
}

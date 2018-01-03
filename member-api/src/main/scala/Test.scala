import com.today.api.user._
import com.today.api.user.request.{ RegisterUserRequest}

object Test extends App {

  override def main(args: Array[String]): Unit = {
    val res = UserServiceClient.registerUser(RegisterUserRequest("lisi","A12345678","15750174634"))
    println(res)
  }
}

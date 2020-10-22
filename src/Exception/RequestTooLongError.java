package Exception;

public class RequestTooLongError extends Exception {
    public RequestTooLongError(){
        System.out.println("RequestTooLongError:请求序列过长，请输入正确的值");
    }
}

package api.v1.error;

@SuppressWarnings("serial")
public class BaseRequestException extends Exception{
	public BaseRequestException(String message){
		super(message);
	}
}

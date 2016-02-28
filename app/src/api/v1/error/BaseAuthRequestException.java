package api.v1.error;

public class BaseAuthRequestException extends Exception {
	public BaseAuthRequestException(String message){
		super(message);
	}
}

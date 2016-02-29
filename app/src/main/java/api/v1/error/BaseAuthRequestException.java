package api.v1.error;

@SuppressWarnings("serial")
public class BaseAuthRequestException extends Exception {
	public BaseAuthRequestException(String message){
		super(message);
	}
}

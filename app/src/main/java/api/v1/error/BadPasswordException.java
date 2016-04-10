package api.v1.error;


@SuppressWarnings("serial")
public class BadPasswordException extends Exception{

	public BadPasswordException(String message){
		super(message);
	}
}

package api.v1.error;

@SuppressWarnings("serial")
public class BadEmailException extends Exception{
	public BadEmailException(String message){
		super(message);
	}
}

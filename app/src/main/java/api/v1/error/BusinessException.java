package api.v1.error;


@SuppressWarnings("serial")
public class BusinessException extends Exception{

	public BusinessException(String message){
		super(message);
	}
}

package api.v1.error;

import java.util.zip.DataFormatException;

@SuppressWarnings("serial")
public class BadPasswordException extends DataFormatException{

	public BadPasswordException(String message){
		super(message);
	}
}

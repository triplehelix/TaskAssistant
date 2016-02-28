package api.v1.error;

import java.util.zip.DataFormatException;

@SuppressWarnings("serial")
public class BadEmailException extends DataFormatException{
	public BadEmailException(String message){
		super(message);
	}
}

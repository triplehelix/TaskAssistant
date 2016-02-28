package api.v1.error;

import java.util.zip.DataFormatException;

public class BadPasswordException extends DataFormatException{

	public BadPasswordException(String s){
		super(s);
	}
}

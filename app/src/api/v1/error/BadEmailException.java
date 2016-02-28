package api.v1.error;

import java.util.zip.DataFormatException;

public class BadEmailException extends DataFormatException{
	
	public BadEmailException(String s){
		super(s);
	}
}

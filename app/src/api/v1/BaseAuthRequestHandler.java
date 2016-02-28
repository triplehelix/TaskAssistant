package api.v1;
import javax.servlet.ServletException;

import api.v1.error.BadEmailException;
import api.v1.error.BadPasswordException;
import api.v1.error.BaseAuthRequestException;
/**
 * We probably won't use this.
 * @author kennethlyon
 *
 */
public class BaseAuthRequestHandler extends BaseRequestHandler{
	
	/**
	 * Validates that an email is well formed. Throws Exception 
	 * if it is not well formed.
	 * @param email
	 * @return
	 */
	protected String parseJsonAsEmail(String email) throws BaseAuthRequestException{
		email=email.trim();
		try{
			if(!email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$"))
				throw new BadEmailException("Not a well formed email: " + email);
		
		}catch(BadEmailException e){
			log.error("Exception while parsing request. " + e.getMessage());
			throw new BaseAuthRequestException(e.getMessage());
		}
			return email;
	}
	
	/**
	 * Validates the password as being well formed. Throws Exception.
	 * @param password
	 * @return
	 * @throws Exception
	 */
	protected String parseJsonAsPassword(String password) throws BaseAuthRequestException{
		password=password.trim();
		try{
		if(!password.matches("^[a-zA-Z]\\w{3,14}$"))
			throw new BadPasswordException("Password: '"+ password + "' not strong enough.");
	
		}catch(BadPasswordException e){
			log.error("Exception while parsing request. " + e.getMessage());
			throw new BaseAuthRequestException(e.getMessage());
		}
		return password;
	}
}

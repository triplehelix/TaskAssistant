package api.v1;

import javax.servlet.ServletException;

import api.v1.error.BadEmailException;
import api.v1.error.BadPasswordException;

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
	protected String parseJsonAsEmail(String email) {
		email=email.trim();
		try{
		if(!email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$"))
			throw new BadEmailException("Not a well formed email! " + email);
		}catch(Exception e){
			log.error("Unacceptable email exception.");
		}
		return email;
	}
	
	/**
	 * Validates the password as being well formed. Throws Exception.
	 * @param password
	 * @return
	 * @throws Exception
	 */
	protected String parseJsonAsPassword(String password) throws BadPasswordException{
		password=password.trim();				
		if(!password.matches("^[a-zA-Z]\\w{3,14}$"))
			throw new BadPasswordException("Password: '"+ password + "' not strong enough.");
		return password;
	}
}

package api.v1;

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
	protected String parseJsonAsEmail(String email) throws Exception{
		email=email.trim();
		if(!email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$"))
			throw new Exception("Not a well formed email! " + email);
		return email;
	}
	
	/**
	 * Validates the password as being well formed. Throws Exception.
	 * @param password
	 * @return
	 * @throws Exception
	 */
	protected String parseJsonAsPassword(String password) throws Exception{
		password=password.trim();				
		if(!password.matches("^[a-zA-Z]\\w{3,14}$"))
			throw new Exception("Password: '"+ password + "' not strong enough.");
		return password;
	}
}

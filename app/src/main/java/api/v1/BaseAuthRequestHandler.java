package api.v1;
import api.v1.error.BusinessException;
import api.v1.repo.UserRepository;
import api.v1.error.Error;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;


/**
 * BaseAuthRequestHandler is a subclass of BaseRequestHandler and provides functionality 
 * for APIs that require Authentication.
 * @author kennethlyon
 *
 */
public class BaseAuthRequestHandler extends BaseRequestHandler{

	/* Instantiate the user repository here. Only Auth classes need
	 * to access to the userRepository.
	 */
	protected static UserRepository userRepository;
	static{
		userRepository=new UserRepository();
	}

	/**
	 * Validates that an email is well formed. Throws Exception 
	 * if it is not well formed.
	 * @param email
	 * @return
	 */
	protected String parseJsonAsEmail(String email) throws BusinessException{
		email=email.trim();
		if(!isValidEmail(email)){
			log.error("Supplied email address: {} is not well formed.", email);
			throw new BusinessException("Email address: " + email + " is not well formed.", Error.valueOf("BAD_EMAIL_ERROR"));
		}
		return email;
	}

	/**
	 * Validates that the email is well formed. Returns T/F.
	 * @param email
	 * @return
     */
	private boolean isValidEmail(String email){
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	/**
	 * Validates the password as being well formed. Throws Exception.
	 * @param password
	 * @return
	 * @throws Exception
	 */
	protected String parseJsonAsPassword(String password) throws BusinessException{
		if(!password.matches("(?=^.{8,16}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+}{\":;'?/>.<,])(?!.*\\s).*$"))
			throw new BusinessException("Try another password. ", Error.valueOf("BAD_PASSWORD_ERROR"));
		return password;
	}
}

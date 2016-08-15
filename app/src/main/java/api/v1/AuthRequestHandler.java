package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.model.User;
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
public class AuthRequestHandler extends BaseRequestHandler{

	/* Instantiate the user repository here. Only Auth classes need
	 * to access to the userRepository.
	 */
	protected static UserRepository userRepository;
	static{
		userRepository=new UserRepository();
	}

	public UserRepository getUserRepository(){
	    return userRepository;
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
            log.error("Supplied email address: {} is not valid.", email);
            throw new BusinessException("Email address: " + email + " is invalid.", Error.valueOf("INVALID_EMAIL_ERROR"));
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
            throw new BusinessException("Try another password. ", Error.valueOf("INVALID_PASSWORD_ERROR"));
		return password;
	}

    /**
     * Use to validate the supplied password from a GetUser request.
     * @param fromClient
     * @param fromRepository
     * @return
     */
    protected void validatePassword(User fromClient, User fromRepository) throws BusinessException, SystemException {
        if(fromClient.getPassword().equals(fromRepository.getPassword()))
            return;
        else{
            log.error(fromClient.toJson());
            log.error(fromRepository.toJson());
            throw new BusinessException("Incorrect password.", Error.valueOf("INCORRECT_PASSWORD_ERROR"));
        }
    }
}

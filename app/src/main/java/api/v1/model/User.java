package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class User {
	private int id;
	private String email;
	private String password;
    protected static final Logger log = LoggerFactory.getLogger(User.class);

	/**
	 * Create a new User w/o an user id. Users created without an id
     * are assigned an id of -1.
	 */
	public User(){
        this.id=-1;
	}
	public int getId() {return id;}
	public void setId(int id){
		this.id=id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) throws BusinessException {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            this.email = email;
        } catch (AddressException ex) {
            log.error("Supplied email address: {} is not valid.", email);
            throw new BusinessException("Email address: " + email + " is invalid.", Error.valueOf("INVALID_EMAIL_ERROR"));
        }
	}

    /**
     * TODO revisit authentication & security.
     * Currently, setPassword just duplicates the code used in AuthRequestHandler to
     * validate the password. It is reused here for unit testing purposes.
     *
     * @param password
     * @throws BusinessException
     */
	public void setPassword(String password) throws BusinessException {
        if(!password.matches("(?=^.{8,16}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+}{\":;'?/>.<,])(?!.*\\s).*$")) {
            log.error("Supplied password: {} is not valid.", password);
            throw new BusinessException("Try another password. ", Error.valueOf("INVALID_PASSWORD_ERROR"));
        }
        else
	        this.password=password;
	}

    public String getPassword(){
        return this.password;
    }

    /**
     * Create a serialized JSON String of this instance
     * using GSON.
     * @return
     */
    public String toJson(){
        Gson gson=new Gson();
        return gson.toJson(this);
    }
}

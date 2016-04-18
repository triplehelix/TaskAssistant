package api.v1.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import api.v1.BaseAuthRequestHandler;
import api.v1.model.User;

/**
 * ValidateUser accepts a User login and determines if the User is a 
 * valid user.
 * @author kennethlyon
 *
 */
@WebServlet("/api/v1/auth/ValidateUser")
public class ValidateUser extends BaseAuthRequestHandler{
	/**
	 * POST
	 * request 
	 *   email
	 *   password
	 * response
	 *   success
	 *   error
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request,
					   HttpServletResponse response)throws ServletException, IOException {
		//First get the email and password.
		boolean error=false;
		String errorMsg = "no error";
		User user=new User();		
		try{
			JSONObject jsonRequest=parseRequest(request.getParameter("params"));
			String email= parseJsonAsEmail((String)jsonRequest.get("email"));
			String password= parseJsonAsEmail((String)jsonRequest.get("password"));
			user.setEmail(email);
			user.setPassword(password);
			
			/* Create a user object. Then, use the repository to 'get' that 
			 * user. If the user does not exist, an exception is thrown.
			 *
			 * TODO what kind of response does Mike need from ValidateUser.
			 *
			 * I'm guessing he needs a JSON String. So this class will provide
			 * him with one? But the only purpose this serves is to give the
			 * Client an User ID. Does he need the user ID? Well, he obviously
			 * needs the User ID.
			 */
			userRepository.get(user);
			//

		}catch(Exception e){
			log.error(e.getMessage());
			errorMsg=e.getMessage();
			error=true;
		}
		//TODO set the response output.
		sendResponse(error, errorMsg, response);
	}

	/**
	 *
	 * @return
     */
	private static String makeResponse(User u){
		return null;
	}

}
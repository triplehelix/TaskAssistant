package api.v1.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import org.json.simple.JSONObject;

import api.v1.AuthRequestHandler;
import api.v1.model.User;

/**
 * ValidateUser accepts a User login and determines if the User is a 
 * valid user.
 * @author kennethlyon
 *
 */
@WebServlet("/api/v1/auth/ValidateUser")
public class ValidateUser extends AuthRequestHandler{
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
		int errorCode=1;
		String errorMsg = "no error";
		User user=new User();
		JSONObject jsonRequest = new JSONObject();
		try{
			jsonRequest=parseRequest(request.getParameter("params"));
			String email= parseJsonAsEmail((String)jsonRequest.get("email"));
			String password= parseJsonAsEmail((String)jsonRequest.get("password"));
			user.setEmail(email);
			user.setPassword(password);
			
			/**
			 * TODO verify that there is a User with this email and password.
			 */
			userRepository.get(user);

		}catch(BusinessException e){
			log.error("An error occurred while handling a ValidateUser Request: {}.", jsonRequest.toJSONString(), e);
			log.error(e.getMessage());
			errorMsg = "Error " + e.getMessage();
			errorCode = e.getError().getCode();
			error = true;
		}catch(SystemException s){
			log.error("An error occurred while handling a ValidateUser Request: {}.", jsonRequest.toJSONString(), s);
			errorMsg = "Error " + s.getMessage();
			errorCode = s.getError().getCode();
			error = true;
		}
		sendResponse(error, errorMsg, response);
	}
}
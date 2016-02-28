package api.v1.auth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import api.v1.BaseAuthRequestHandler;
import api.v1.model.User;
import api.v1.repo.UserRepository;

/**
 * CreateUser responds to a request to create a new user. A JSONObject
 * instance must provide a string email and a string password.
 * @author kennethlyon
 *
 */
public class CreateUser extends BaseAuthRequestHandler{
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
			
			/* Internet says I am supposed to use dependency injection.
			 * Also that a static reference is the wrong way to use the 
			 * repository too. So we do this: (which makes even less sense)
			 */
			new UserRepository().add(user);

		}catch(Exception e){
			log.error(e.getMessage());
			errorMsg="Error. " + e.getMessage();
			error=true;
		}
		
		//Return success or error via JSON.
		JSONObject obj = new JSONObject();
		obj.put("error", error);
		obj.put("errorMsg", errorMsg);		
		PrintWriter out = response.getWriter();
		out.println(obj);
	}
}
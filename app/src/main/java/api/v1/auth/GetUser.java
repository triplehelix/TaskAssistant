package api.v1.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.AuthRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.User;


/**
 * This api is used to retrieve a given user. Use the class member
 * doGet(HttpServletRequest, HttpServletResponse) to retrieve this
 * user.
 *
 *  @author Ken Lyon
 */
@WebServlet("/api/v1/auth/GetUser")
public class GetUser extends AuthRequestHandler {

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
     */
	public void doGet(HttpServletRequest request, 
				HttpServletResponse response)throws ServletException, IOException {
		boolean error = false;
		String errorMsg = "no error";

		int errorCode = 0;
		JSONObject jsonRequest = new JSONObject();
        String userAsJsonString="";
		try {
			jsonRequest = parseRequest(request.getParameter("params"));
            int userId= parseJsonIntAsInt((String)jsonRequest.get("id"));
            User user = userRepository.get(new User(userId));
		/**
		 * To successfully return an user to the client, we first get the
         * user id, then a "serialized" user should be sent back to the
         * client through the HttpServletResponse.
         *
         * Example usage of com.google.code.gson 2.6.2:
		 *   Gson gson = new Gson();
         *   String objectAsJsonString = gson.toJson(someObject);
         *
         * Sending a response:
         * someJsonObject.put("name of object", objectAsJsonString);
		 */
            Gson gson = new Gson();
            userAsJsonString = gson.toJson(user);

        } catch (BusinessException b) {
			log.error("An error occurred while handling an GetUser  Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			log.error("An error occurred while handling an GetUser Request: {}.", jsonRequest.toJSONString(), s);
			errorMsg = "Error. " + s.getMessage();
			errorCode = s.getError().getCode();
			error = true;
		}

		JSONObject jsonResponse = new JSONObject();
		if (error) {
			jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
		} else {
            jsonResponse.put("success", true);
            jsonResponse.put("user", userAsJsonString);
		}
		sendMessage(jsonResponse, response);
	}
}

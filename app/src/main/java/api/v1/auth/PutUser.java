package api.v1.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.AuthRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.User;

/**
 * This api is used to update a given user. Use the class member
 * doPut(HttpServletRequest, HttpServletResponse) to update this
 * User.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/auth/PutUser")
public class PutUser extends AuthRequestHandler {

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
         */
	public void doPut(HttpServletRequest request, 
				HttpServletResponse response)throws ServletException, IOException {
		boolean error = false;
		String errorMsg = "no error";
		User user = null;
		int errorCode = 0;
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest = parseRequest(request.getParameter("params"));
            int id= parseJsonIntAsInt((String)jsonRequest.get("id"));
            user=new User(id);
		/**
         * TODO: Update this type.
         * First, we have to read the type id from the jsonRequest. Then, an instance of type must
		 * be sent to repository containing the id and all member fields that need to be modified.
		 * Finally, the client should be notified of success/failure.
         *
         * TODO The json message sent should specify which fields need to be updated.
         * This class should be able to identify which fields need to be updated.
		 */

		userRepository.update(user);
		} catch (BusinessException b) {
			log.error("An error occurred while handling an PutUser  Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			log.error("An error occurred while handling an PutUser Request: {}.", jsonRequest.toJSONString(), s);
			errorMsg = "Error. " + s.getMessage();
			errorCode = s.getError().getCode();
			error = true;
		}

		JSONObject jsonResponse = new JSONObject();
		if (error) {
			jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
		} else {
			jsonResponse.put("success", true);
		}
		sendMessage(jsonResponse, response);
	}
}

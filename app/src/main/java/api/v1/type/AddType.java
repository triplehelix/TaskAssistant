package api.v1.type;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.TypeRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Type;

/**
 * This api is used to create a new type. Use the class member
 * doPost(HttpServletRequest, HttpServletResponse) to create a
 * new type.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/type/AddType")
public class AddType extends TypeRequestHandler {

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
     */
	public void doPost(HttpServletRequest request, 
				HttpServletResponse response)throws ServletException, IOException {
		boolean error = false;
		String errorMsg = "no error";
		Type type = new Type();
		int errorCode = 0;
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest = parseRequest(request.getParameter("params"));
         /**
          * TODO: populate type object.
          * Ensure that all of the methods needed to parse for this type's
          * fields are present in the super class of this requestHandler.
          */

		typeRepository.add(type);
		} catch (BusinessException b) {
			log.error("An error occurred while handling an AddType  Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			log.error("An error occurred while handling an AddType Request: {}.", jsonRequest.toJSONString(), s);
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

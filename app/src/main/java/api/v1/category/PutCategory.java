package api.v1.category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.CategoryRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Category;

/**
 * This api is used to update a given category. Use the class member
 * doPut(HttpServletRequest, HttpServletResponse) to update this
 * category.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/category/PutCategory")
public class PutCategory extends CategoryRequestHandler {

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
		Category category = new Category();
		int errorCode = 0;
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest = parseRequest(request.getParameter("params"));
			/**
			 * TODO: Update this category.
			 * First, we have to read the category id from the jsonRequest. Then, an instance of category must
			 * be sent to repository containing the id and all member fields that need to be modified.
			 * Finally, the client should be notified of success/failure.
			 */

		categoryRepository.update(category);
		} catch (BusinessException b) {
			log.error("An error occurred while handling an PutCategory  Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			log.error("An error occurred while handling an PutCategory Request: {}.", jsonRequest.toJSONString(), s);
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

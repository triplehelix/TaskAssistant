package api.v1.category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.CategoryRequestHandler;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Category;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * This api is used to retrieve a given category. Use the class member
 * doGet(HttpServletRequest, HttpServletResponse) to retrieve this
 * category.
 *
 *  @author Ken Lyon
 */
@WebServlet("/api/v1/category/GetCategory")
public class GetCategory extends CategoryRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetCategory.class);
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
		Category category=new Category();
		int errorCode = 0;
		String json="";
		try {
			json = request.getParameter("params");
			category= (Category) getMyObject(json, category);
		    category=categoryRepository.get(category);
		} catch (BusinessException b) {
			LOGGER.error("An error occurred while handling an GetCategory  Request: {}.", json, b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			LOGGER.error("An error occurred while handling an GetCategory Request: {}.", json, s);
			errorMsg = "Error. " + s.getMessage();
			errorCode = s.getError().getCode();
			error = true;
		}

		JSONObject jsonResponse = new JSONObject();
		if (error) {
			jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
		} else {
			jsonResponse.put("success", true);
            jsonResponse.put("Category",category.toJson());
		}
		sendMessage(jsonResponse, response);
	}
}

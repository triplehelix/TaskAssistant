package api.v1.category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.CategoryRequestHandler;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.Error;
import api.v1.error.SystemException;
import api.v1.model.Schedule;
import api.v1.model.Task;
import api.v1.model.User;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import api.v1.model.Category;

/**
 * This api is used to delete a given category. Use the class member
 * doDelete(HttpServletRequest, HttpServletResponse) to delete
 * this category.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/category/DeleteCategory")
public class DeleteCategory extends CategoryRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCategory.class);
	/**
	 * Delete a particular category. A category "id" is required to specify the 
	 * category to be removed.
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
		int errorCode = 0;
        Category category = new Category();
		JSONObject jsonRequest = new JSONObject();
		try {
		    jsonRequest = parseRequest(request.getParameter("params"));
            category.setId(parseJsonIntAsInt((String)jsonRequest.get("id")));
            category=categoryRepository.get(category);

            ArrayList<Schedule> updatedSchedules=getCleanedSchedules(category);
            ArrayList<Task> updatedTasks=getCleanedTasks(category);
            User updatedUser=getCleanedUser(category);

            //Commit changes to Tasks, Schedules and User:
            for(Task task: updatedTasks)
                taskRepository.update(task);
            for(Schedule schedule: updatedSchedules)
                scheduleRepository.update(schedule);
            userRepository.update(updatedUser);
            categoryRepository.delete(category);
		} catch (BusinessException b) {
			LOGGER.error("An error occurred while handling a DeleteCategory Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			LOGGER.error("An error occurred while handling a DeleteCategory Request: {}.", jsonRequest.toJSONString(), s);
			errorMsg = "Error. " + s.getMessage();
			errorCode = s.getError().getCode();
			error = true;
		} catch (CriticalException c) {
            LOGGER.error("An error occurred while handling a DeleteCategory Request: {}.", jsonRequest.toJSONString(), c);
            errorMsg = "Error. " + c.getMessage();
            errorCode = c.getError().getCode();
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
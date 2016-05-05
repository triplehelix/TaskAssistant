package api.v1.taskList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import org.json.simple.JSONObject;
import api.v1.TaskListRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.TaskList;

/**
 * This api is used to delete a given taskList. Use the class member
 * doDelete(HttpServletRequest, HttpServletResponse) to delete
 * this taskList.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/taskList/DeleteTaskList")
public class DeleteTaskList extends TaskListRequestHandler {

	/**
	 * Delete a particular taskList. A taskList "id" is required to specify the 
	 * taskList to be removed.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doDelete(HttpServletRequest request, 
				HttpServletResponse response)throws ServletException, IOException {
		boolean error = false;
		String errorMsg = "no error";
		int errorCode = 0;
		JSONObject jsonRequest = new JSONObject();
		try {
		    jsonRequest = parseRequest(request.getParameter("params"));
		    int taskListId=parseJsonIntAsInt((String)jsonRequest.get("id"));
		    taskListRepository.delete(new TaskList(taskListId));

		} catch (BusinessException b) {
			log.error("An error occurred while handling an DeleteTaskList Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			log.error("An error occurred while handling an DeleteTaskList Request: {}.", jsonRequest.toJSONString(), s);
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
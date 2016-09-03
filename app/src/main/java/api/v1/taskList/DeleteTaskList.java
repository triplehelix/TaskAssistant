package api.v1.taskList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskRequestHandler;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import org.json.simple.JSONObject;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.TaskList;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * This api is used to delete a given taskList. Use the class member
 * doDelete(HttpServletRequest, HttpServletResponse) to delete task
 * taskList.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/taskList/DeleteTaskList")
public class DeleteTaskList extends TaskRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTaskList.class);
	/**
	 * Delete a TaskList and all of the tasks that belong to it. A
	 * taskList "id" is required to specify the taskList to be
     * removed.
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
		JSONObject jsonRequest = new JSONObject();
		try {
		    jsonRequest = parseRequest(request.getParameter("params"));
		    int taskListId=parseJsonIntAsInt((String)jsonRequest.get("id"));
			TaskList taskList=new TaskList();
			taskList.setId(taskListId);
		    taskListRepository.delete(taskList);

		} catch (BusinessException b) {
			LOGGER.error("An error occurred while handling an DeleteTaskList Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			LOGGER.error("An error occurred while handling an DeleteTaskList Request: {}.", jsonRequest.toJSONString(), s);
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

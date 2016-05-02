package api.v1.task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import org.json.simple.JSONObject;
import api.v1.TaskRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Task;

/**
 * This api is used to delete a given task. Use the class member
 * doDelete(HttpServletRequest, HttpServletResponse) to delete
 * this task.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/task/DeleteTask")
public class DeleteTask extends TaskRequestHandler {

	/**
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
		Task task = new Task();
		int errorCode = 0;
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest = parseRequest(request.getParameter("params"));
			/**
			 * TODO: Delete this task.
			 * At a minimum, the task id must be read from the json string
			 * and sent to the repository, which can handle the removal.
			 */

			taskRepository.delete(task);
		} catch (BusinessException b) {
			log.error("An error occurred while handling an DeleteTask  Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			log.error("An error occurred while handling an DeleteTask Request: {}.", jsonRequest.toJSONString(), s);
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

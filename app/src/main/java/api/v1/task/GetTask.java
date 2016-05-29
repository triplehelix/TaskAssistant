package api.v1.task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import com.google.appengine.repackaged.com.google.gson.Gson;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.TaskRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Task;

/**
 * This api is used to retrieve a given task. Use the class member
 * doGet(HttpServletRequest, HttpServletResponse) to retrieve this
 * task.
 *
 *  @author Ken Lyon
 */
@WebServlet("/api/v1/task/GetTask")
public class GetTask extends TaskRequestHandler {
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
		Task task=null;
		String taskAsJsonString="";
		try {
			/**
			 * To successfully, return an instance of task to the client, it
             * is necessary to first discover the task id, then a serialized
             * version of that instance should be sent back to the client
             * through the HttpServletResponse.
             *
			 */
            jsonRequest = parseRequest(request.getParameter("params"));
            task = taskRepository.get(new Task(parseJsonIntAsInt((String)jsonRequest.get("id"))));
            Gson gson = new Gson();
			taskAsJsonString = gson.toJson(task);

		} catch (BusinessException b) {
			log.error("An error occurred while handling an GetTask Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			log.error("An error occurred while handling an GetTask Request: {}.", jsonRequest.toJSONString(), s);
			errorMsg = "Error. " + s.getMessage();
			errorCode = s.getError().getCode();
			error = true;
		}

		JSONObject jsonResponse = new JSONObject();
		if (error) {
			jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
		} else {
			jsonResponse.put("success", true);
			jsonResponse.put("task", taskAsJsonString);
		}
		sendMessage(jsonResponse, response);
	}
}

package api.v1.task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
 * This api is used to update a given task. Use the class member
 * doPut(HttpServletRequest, HttpServletResponse) to update this
 * task.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/task/PutTask")
public class UpdateTask extends TaskRequestHandler {

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
		Task task = null;
		int errorCode = 0;
		JSONObject jsonRequest = new JSONObject();
		try {
			/**
			 * TODO: Update this task.
			 * First, we have to read the task id from the jsonRequest. Then, an instance
			 * of the new task must be sent to repository containing the id and all member
			 * fields that need to be modified. Finally, the client should be notified of
			 * success/failure.
			 */
            jsonRequest = parseRequest(request.getParameter("params"));

            // DO attempt to set task id. If a task id is not present an exception should be thrown.
			task = new Task();

			task.setId(parseJsonIntAsInt((String)jsonRequest.get("id")));

			// private String taskListId
			task.setTaskListId(parseJsonIntAsInt((String)jsonRequest.get("taskListId")));

			// private String name;
            task.setName((String)jsonRequest.get("name"));

            // private boolean important;
            task.setImportant(parseJsonBooleanAsBoolean((String)jsonRequest.get("important")));

            // private String note;
            task.setNote((String)jsonRequest.get("note"));

            // private long estimatedTime;
            task.setEstimatedTime(parseJsonLongAsLong((String)jsonRequest.get("estimatedTime")));

            // private long investedTime;
            task.setInvestedTime(parseJsonLongAsLong((String)jsonRequest.get("investedTime")));

            // private boolean urgent;
            //TODO does it make sense to set urgent?
            task.setUrgent(parseJsonBooleanAsBoolean((String)jsonRequest.get("urgent")));

            // private Date dueDate;
            task.setDueDate(parseJsonDateAsDate((String)jsonRequest.get("dueDate")));

            // private enum Status{NEW, IN_PROGRESS, DELEGATED, DEFERRED, DONE};
            // private Status status;
            task.setStatus((String)jsonRequest.get("status"));

            taskRepository.update(task);
		} catch (BusinessException b) {
			log.error("An error occurred while handling an PutTask  Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			log.error("An error occurred while handling an PutTask Request: {}.", jsonRequest.toJSONString(), s);
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

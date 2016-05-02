package api.v1.reminder;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.v1.repo.ReminderRepository;
import org.json.simple.JSONObject;


import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import api.v1.TaskRequestHandler;
import api.v1.model.Reminder;

/**
 *
 * This api is used to create a new type. Use the class member
 * doPost(HttpServletRequest, HttpServletResponse) to create a
 * new type.
 *
 * Add Reminder allows the client to add a reminder for a given
 * task. A valid reminder must specify a task by it's task_id and
 * specify a time that the reminder must be made for this task.
 *
 * @author Ken Lyon
 */
@SuppressWarnings("serial")
@WebServlet("/api/v1/task/AddReminder")
public class AddReminder extends TaskRequestHandler{

	/**
	 * Post a new Reminder object. Request must provide task_id and reminder_time. 
	 * Responds with success or error.
	 */
	public void doPost(HttpServletRequest request, 
				HttpServletResponse response)throws ServletException, IOException {
		boolean error = false;
		String errorMsg = "no error";
		int errorCode = 0;
		JSONObject jsonRequest = new JSONObject();
		Reminder reminder = null;
		// Step 1: parse taskId and reminderDate.
		try{
			jsonRequest = parseRequest(request.getParameter("params"));
			Date reminderDate = parseJsonDateAsDate((String)jsonRequest.get("reminder_time"));		
			Integer taskId =  parseJsonIntAsInt((String)jsonRequest.get("task_id"));		
			reminder = new Reminder();
			reminder.setTaskId((int)taskId);
			reminder.setReminderTime(reminderDate);
			
    	/**
		 * TODO: populate type object.
		 * Ensure that all of the methods needed to parse for this type's
		 * fields are present in the super class of this requestHandler.
		 */

			reminderRepository.add(reminder);
		} catch (BusinessException b) {
			log.error("An error occurred while handling an AddTask  Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			log.error("An error occurred while handling an AddTask Request: {}.", jsonRequest.toJSONString(), s);
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
package api.v1.task;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import api.v1.BaseAuthRequestHandler;
import api.v1.model.Reminder;

/**
 * Add Reminder allows the client to add a reminder for a given 
 * task. A valid reminder must specify a task by it's task_id and 
 * specify a time that the reminder must be made. 
 * for this task. 
 * @author kennethlyon
 *
 */
@SuppressWarnings("serial")
@WebServlet("/api/v1/task/AddReminder")
public class AddReminder extends BaseAuthRequestHandler{

	/**
	 * Post a new Reminder object. Request must provide task_id and reminder_time. 
	 * Responds with success or error.
	 */
	public void doPost(HttpServletRequest request, 
				HttpServletResponse response)throws ServletException, IOException {
		boolean error = false;
		String errorMsg = "no error";
		// Step 1: parse taskId and reminderDate.
		try{
			JSONObject jsonRequest = parseRequest(request.getParameter("params"));		
			Date reminderDate = parseJsonDateAsDate((String)jsonRequest.get("reminder_time"));		
			Integer taskId =  parseJsonIntAsInt((String)jsonRequest.get("task_id"));		
			Reminder reminder = new Reminder();
			reminder.setTaskId((int)taskId);
			reminder.setReminderTime(reminderDate);
			
			//TODO add a reminder object. 
			
		}catch(BaseRequestException e){
			errorMsg=e.getMessage();
			error=true;
		}
		//
		//sendResponse is inherited from BaseRequestHandler
		sendResponse(error, errorMsg, response);
	}
}

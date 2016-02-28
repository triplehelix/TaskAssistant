package api.v1.task;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import api.v1.BaseAuthRequestHandler;
import api.v1.model.Reminder;

/**
 * TODO refactor this class in the form of CreateUser.
 * @author kennethlyon
 *
 */
public class AddReminder extends BaseAuthRequestHandler{

	/**
	 * Request
	 *   task_id
	 *   reminder_time
	 * Response
	 *   success
	 *   error
	 */
	public void doPost(HttpServletRequest request, 
				HttpServletResponse response)throws ServletException, IOException {
		boolean error = false;
		String errorMsg = "no error";
		// Step 1: parse taskId and reminderDate.

		JSONObject jsonRequest = parseRequest(request.getParameter("params"));		
		Date reminderDate = parseJsonDateAsDate((String)jsonRequest.get("reminder_time"));		
		Integer taskId =  parseJsonIntAsInt((String)jsonRequest.get("task_id"));		

			//Step 2: determine error status
		if(null==taskId){
			error=true;	
			errorMsg="Non-Integer task id recieved.";
		}
		if(null==reminderDate){
			error=true;
			errorMsg="Malformed reminder date recieved.";
		}

		//Step 3: Conditionally create a reminder.		
		Reminder reminder = new Reminder();
		if(!error){
			reminder.setTaskId((int)taskId);
		reminder.setReminderTime(reminderDate);
		}else
			reminder=null;		

		//sendResponse is inherited from BaseRequestHandler
		sendResponse(error, errorMsg, response);
	}
}

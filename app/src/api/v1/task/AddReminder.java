package api.v1.task;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import api.v1.BaseAuthRequestHandler;
import api.v1.error.BaseRequestException;
import api.v1.model.Reminder;

/**
 * TODO refactor this class in the form of CreateUser.
 * @author kennethlyon
 *
 */
@WebServlet("/api/v1/task/AddReminder")
public class AddReminder extends BaseAuthRequestHandler{

/* The standard thing to do is have your Servlet's doXxx() method
 * (eg. doGet(), doPost(), etc.) throw a ServletException and allow 
 * the container to catch and handle it.  
 */


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
		try{
			JSONObject jsonRequest = parseRequest(request.getParameter("params"));		
			Date reminderDate = parseJsonDateAsDate((String)jsonRequest.get("reminder_time"));		
			Integer taskId =  parseJsonIntAsInt((String)jsonRequest.get("task_id"));		
			Reminder reminder = new Reminder();
			reminder.setTaskId((int)taskId);
			reminder.setReminderTime(reminderDate);
			
		}catch(BaseRequestException e){
			errorMsg=e.getMessage();
			error=true;
		}
		//
		//sendResponse is inherited from BaseRequestHandler
		sendResponse(error, errorMsg, response);
	}
}

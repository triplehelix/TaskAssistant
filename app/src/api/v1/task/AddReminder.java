package api.v1.task;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import api.v1.BaseAuthRequestHandler;

public class AddReminder extends BaseAuthRequestHandler{

	public void doPost(HttpServletRequest request, 
				HttpServletResponse receive)throws ServletException, IOException {
		
		
		/* get the task id and reminder time from  
		 * the request object.
		 */
		//Angular may call 'params' something else;
		// = request.getParameter("params");
		JSONObject jsonRequest = parseRequest(request.getParameter("params"));		
		Date reminderDate = parseJsonDateAsDate((String)jsonRequest.get("reminder_time"));
	
		//TODO create a Reminder class.
		
		/* Respond with success or error.
		 * 
		 */
		
		/*

		  Request
		    Task_id
		    Reminder_time
		  Response
		    Success
		    Error
		
		*/
	}
}

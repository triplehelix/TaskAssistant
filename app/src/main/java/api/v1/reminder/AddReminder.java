package api.v1.reminder;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.v1.TaskRequestHandler;
import org.json.simple.JSONObject;

import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import api.v1.model.Reminder;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

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
@WebServlet("/api/v1/reminder/AddReminder")
public class AddReminder extends TaskRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddReminder.class);
    /**
     * Post a new Reminder object. Request must provide task_id and reminder_time. 
     * Responds with success or error.
     */
    public void doPost(HttpServletRequest request, 
                HttpServletResponse response)throws ServletException, IOException {
        boolean error = false;
        String errorMsg = "no error";
        int errorCode = 0;
        Reminder reminder = new Reminder();
        String json="";
        try{
            json=request.getParameter("params");
            reminder=(Reminder) getMyObject(json, reminder);

            //Verify the existence of the tasks prior to updating anything.
            verifyTaskExists(reminder.getTaskId());
            reminder=reminderRepository.add(reminder);
            addReminderToTask(reminder);
        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an AddTask  Request: {}.", json, b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an AddTask Request: {}.", json, s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error) {
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
        } else {
            jsonResponse.put("success", true);
            jsonResponse.put("Reminder", reminder);
        }
        sendMessage(jsonResponse, response);
    }
}
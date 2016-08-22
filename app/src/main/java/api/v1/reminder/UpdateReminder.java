package api.v1.reminder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskRequestHandler;
import api.v1.error.CriticalException;
import api.v1.model.Task;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import java.util.Date;
import api.v1.model.Reminder;

/**
 * This api is used to update a given reminder. Use the class member
 * doPut(HttpServletRequest, HttpServletResponse) to update this
 * reminder.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/reminder/UpdateReminder")
public class UpdateReminder extends TaskRequestHandler {
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
        Reminder clientReminder = new Reminder();
        Reminder serverReminder = new Reminder();
        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            //Create and validate the client reminder.
            log.debug("Create and validate the client reminder.");
            Date reminderDate = parseJsonDateAsDate((String)jsonRequest.get("reminderTime"));
            Integer taskId =  parseJsonIntAsInt((String)jsonRequest.get("taskId"));
            Integer reminderId =  parseJsonIntAsInt((String)jsonRequest.get("id"));
            clientReminder.setId(reminderId);
            clientReminder.setTaskId(taskId);
            clientReminder.setReminderTime(reminderDate);
            verifyTaskExists(clientReminder.getTaskId());
            log.debug("So, now we have the client reminder id, " + clientReminder.getId() + " and the task id it points to " + clientReminder.getTaskId() + ".");

            // Clean references to the current reminder using the serverReminder:
            serverReminder=reminderRepository.get(clientReminder);
            log.debug("Now, we can fetch the server reminder and clean it's reference to the task. Which, by the way is " + serverReminder.getTaskId() + ".");

            removeReferences(serverReminder);
            addReminderToTask(clientReminder);
            reminderRepository.update(clientReminder);

        } catch (BusinessException b) {
            log.error("An error occurred while handling an PutReminder  Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            log.error("An error occurred while handling an PutReminder Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        } catch (CriticalException c) {
            log.error("An error occurred while handling an PutReminder Request: {}.", jsonRequest.toJSONString(), c);
            errorMsg = "Error. " + c.getMessage();
            errorCode = c.getError().getCode();
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

package api.v1.reminder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.ReminderRequestHandler;

import api.v1.error.CriticalException;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Reminder;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * This api is used to update a given reminder. Use the class member
 * doPut(HttpServletRequest, HttpServletResponse) to update this
 * reminder.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/reminder/UpdateReminder")
public class UpdateReminder extends ReminderRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateReminder.class);
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
        Reminder clientReminder=new Reminder();
        Reminder serverReminder;
        String json="";
        int errorCode = 0;
        try {
            json = request.getParameter("params");
            //Create and validate the client reminder.
            clientReminder=(Reminder) getMyObject(json, clientReminder);
            verifyTaskExists(clientReminder.getTaskId());

            // Clean references to the current reminder using the serverReminder:
            serverReminder=reminderRepository.get(clientReminder);

            removeReferences(serverReminder);
            addReminderToTask(clientReminder);
            reminderRepository.update(clientReminder);

        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an PutReminder  Request: {}.", json, b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an PutReminder Request: {}.", json, s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        } catch (CriticalException c) {
            LOGGER.error("An error occurred while handling an PutReminder Request: {}.", json, c);
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

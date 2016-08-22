package api.v1.reminder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskRequestHandler;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.SystemException;
import org.json.simple.JSONObject;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Reminder;

/**
 * This api is used to delete a given reminder. Use the class member
 * doDelete(HttpServletRequest, HttpServletResponse) to delete
 * this reminder.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/reminder/DeleteReminder")
public class DeleteReminder extends TaskRequestHandler {

    /**
     * Delete a particular reminder. A reminder "id" is required to specify the 
     * reminder to be removed.
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
        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            int reminderId=parseJsonIntAsInt((String)jsonRequest.get("id"));
            Reminder reminder=new Reminder();
            reminder.setId(reminderId);
            reminder=reminderRepository.get(reminder);
            removeReferences(reminder);
            reminderRepository.delete(reminder);
        } catch (BusinessException b) {
            log.error("An error occurred while handling a DeleteReminder Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            log.error("An error occurred while handling a DeleteReminder Request: {}.", jsonRequest.toJSONString(), s);
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

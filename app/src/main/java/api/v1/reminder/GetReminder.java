package api.v1.reminder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskRequestHandler;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Reminder;

/**
 * This api is used to retrieve a given reminder. Use the class member
 * doGet(HttpServletRequest, HttpServletResp`onse) to retrieve this
 * reminder.
 *
 *  @author Ken Lyon
 */
@WebServlet("/api/v1/reminder/GetReminder")
public class GetReminder extends TaskRequestHandler {

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
        Reminder reminder = new Reminder();
        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            /**
             * TODO: Return an instance of this reminder.
             * To successfully, return an instance of reminder to the client, it is necessary to
             * first discover the reminder id, then a serialized version of that instance should be
             * sent back to the client through the HttpServletResponse.
             */

        reminderRepository.get(reminder);
        } catch (BusinessException b) {
            log.error("An error occurred while handling an GetReminder  Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            log.error("An error occurred while handling an GetReminder Request: {}.", jsonRequest.toJSONString(), s);
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

package api.v1.reminder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskRequestHandler;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Reminder;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * This api is used to retrieve a given reminder. Use the class member
 * doGet(HttpServletRequest, HttpServletResp`onse) to retrieve this
 * reminder.
 *
 *  @author Ken Lyon
 */
@WebServlet("/api/v1/reminder/GetReminder")
public class GetReminder extends TaskRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetReminder.class);
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
        String json="";
        String format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Gson gson = new GsonBuilder().setDateFormat(format).create();
        int errorCode = 0;
        try {
            json = request.getParameter("params");
            reminder=gson.fromJson(json, Reminder.class);
            reminder=reminderRepository.get(reminder);
        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an GetReminder  Request: {}.", json, b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an GetReminder Request: {}.", json, s);
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

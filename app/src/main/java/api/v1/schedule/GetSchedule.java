package api.v1.schedule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.ScheduleRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import api.v1.model.Schedule;

/**
 * This api is used to retrieve a given schedule. Use the class member
 * doGet(HttpServletRequest, HttpServletResponse) to retrieve this
 * schedule.
 *
 *  @author Ken Lyon
 */
@WebServlet("/api/v1/schedule/GetSchedule")
public class GetSchedule extends ScheduleRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetSchedule.class);
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
        Schedule schedule = new Schedule();
        int errorCode = 0;
        String json="";
        Gson gson=getCustomGson();
        try {
            json = request.getParameter("params");
            schedule=gson.fromJson(json, Schedule.class);
            schedule=scheduleRepository.get(schedule);
        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an GetSchedule  Request: {}.", json, b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an GetSchedule Request: {}.", json, s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error) {
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
        } else {
            jsonResponse.put("success", true);
            jsonResponse.put("schedule", schedule.toJson());
        }
        sendMessage(jsonResponse, response);
    }
}

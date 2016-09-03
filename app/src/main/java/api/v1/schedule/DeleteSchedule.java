package api.v1.schedule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.ScheduleRequestHandler;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.Error;
import api.v1.error.SystemException;
import api.v1.model.Category;
import api.v1.model.Task;
import api.v1.model.User;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import java.util.ArrayList;

import api.v1.model.Schedule;

/**
 * This api is used to delete a given schedule. Use the class member
 * doDelete(HttpServletRequest, HttpServletResponse) to delete
 * this schedule.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/schedule/DeleteSchedule")
public class DeleteSchedule extends ScheduleRequestHandler {

    /**
     * Delete a particular schedule. A schedule "id" is required to specify the 
     * schedule to be removed.
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
        Schedule schedule = new Schedule();
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            schedule.setId(parseJsonIntAsInt((String)jsonRequest.get("id")));
            schedule=scheduleRepository.get(schedule);

            ArrayList<Category> updatedCategories=getCleanedCategories(schedule);
            ArrayList<Task> updatedTasks=getCleanedTasks(schedule);
            User updatedUser=getCleanedUser(schedule);

            //Commit changes to Tasks, Categories and User:
            for(Task task: updatedTasks)
                taskRepository.update(task);
            for(Category category: updatedCategories)
                categoryRepository.update(category);
            userRepository.update(updatedUser);
            scheduleRepository.delete(schedule);
        } catch (BusinessException b) {
            log.error("An error occurred while handling a DeleteSchedule Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            log.error("An error occurred while handling a DeleteSchedule Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        } catch (CriticalException c) {
            log.error("An error occurred while handling a DeleteSchedule Request: {}.", jsonRequest.toJSONString(), c);
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
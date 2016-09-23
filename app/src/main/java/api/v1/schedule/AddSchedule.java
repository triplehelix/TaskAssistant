package api.v1.schedule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.model.Category;
import api.v1.model.Task;
import api.v1.model.User;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.ScheduleRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import api.v1.model.Schedule;

/**
 * This api is used to create a new schedule. Use the class member
 * doPost(HttpServletRequest, HttpServletResponse) to create a
 * new schedule.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/schedule/AddSchedule")
public class AddSchedule extends ScheduleRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddSchedule.class);
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
            // Verify privileges.
            verifyTaskPrivileges(schedule.getUserId(), schedule.getTaskIds());
            verifyCategoryPrivileges(schedule.getUserId(), schedule.getCategoryIds());
            //Place completed category in the repository.
            schedule=scheduleRepository.add(schedule);

            ArrayList<Task> updatedTasks=getUpdatedTasks(schedule);
            ArrayList<Category> updatedCategories=getUpdatedCategories(schedule);
            User updatedUser=getUpdatedUser(schedule);
           //Commit changes to Tasks, Schedules and User:
            for(Task task: updatedTasks)
                taskRepository.update(task);
            for(Category category: updatedCategories)
                categoryRepository.update(category);
            userRepository.update(updatedUser);

        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an AddSchedule  Request: {}.", json, b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an AddSchedule Request: {}.", json, s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error) {
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
            cleanUp(schedule);
        } else {
            jsonResponse.put("success", true);
        }
        sendMessage(jsonResponse, response);
    }

    /**
     * Here we attempt to remove a schedule from the repository that
     * could not be added fully.
     * @param schedule
     */
    private void cleanUp(Schedule schedule){
        try{
            scheduleRepository.delete(schedule);
        } catch (BusinessException b) {
            LOGGER.error("Could not remove this schedule from the scheduleRepository. ",b);
        } catch (SystemException s) {
            LOGGER.error("Could not remove this schedule from the scheduleRepository. ",s);
        }
    }
}

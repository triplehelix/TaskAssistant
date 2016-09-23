package api.v1.schedule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.error.CriticalException;
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
 * This api is used to update a given schedule. Use the class member
 * doPut(HttpServletRequest, HttpServletResponse) to update this
 * schedule.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/schedule/PutSchedule")
public class UpdateSchedule extends ScheduleRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSchedule.class);
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
        Schedule clientSchedule = new Schedule();
        Schedule serverSchedule=null;
        int errorCode = 0;
        Gson gson=getCustomGson();
        String json="";
        try {
            json = request.getParameter("params");
            clientSchedule=gson.fromJson(json, Schedule.class);
            // Verify privileges.

            verifyCategoryPrivileges(clientSchedule.getUserId(), clientSchedule.getCategoryIds());
            verifyTaskPrivileges(clientSchedule.getUserId(), clientSchedule.getTaskIds());
            //Place completed category in the repository.
            serverSchedule=scheduleRepository.get(clientSchedule);

            cleanReferences(serverSchedule);
            updateReferences(clientSchedule);
        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an UpdateSchedule Request: {}.", json, b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an UpdateSchedule Request: {}.", json, s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        } catch (CriticalException c) {
            LOGGER.error("An error occurred while handling an UpdateSchedule Request: {}.", json, c);
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
    /**
     * Clean all references to the Schedule provided.
     * @param schedule
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    private void cleanReferences(Schedule schedule) throws BusinessException, SystemException, CriticalException
    {
        ArrayList<Category> updatedCategories=getCleanedCategories(schedule);
        ArrayList<Task> updatedTasks=getCleanedTasks(schedule);
        User updatedUser=getCleanedUser(schedule);

        //Commit changes to Tasks, Categories and User:
        for(Task task: updatedTasks)
            taskRepository.update(task);
        for(Category category: updatedCategories)
            categoryRepository.update(category);
        userRepository.update(updatedUser);
    }

    /**
     * Update objects referenced by schedule.
     * @param schedule
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    private void updateReferences(Schedule schedule) throws BusinessException, SystemException, CriticalException
    {
        // Create updated Tasks, Categories and User:
        ArrayList<Task> updatedTasks=getUpdatedTasks(schedule);
        ArrayList<Category> updatedCategories=getUpdatedCategories(schedule);
        User updatedUser=getUpdatedUser(schedule);
        //Commit changes to Tasks, Categories and User:

        for(Task task: updatedTasks)
            taskRepository.update(task);
        for(Category category: updatedCategories)
            categoryRepository.update(category);
        userRepository.update(updatedUser);
        scheduleRepository.update(schedule);
    }
}

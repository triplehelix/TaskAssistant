package api.v1.task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.error.CriticalException;
import api.v1.model.Category;
import api.v1.model.Schedule;
import api.v1.model.TaskList;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.TaskRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import api.v1.model.Task;

/**
 * This api is used to update an existing task.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/task/UpdateTask")
public class UpdateTask extends TaskRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateTask.class);
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

        int errorCode = 0;
        Task clientTask=new Task();
        Task serverTask;
        String json="";
        try {
            //Create a basic task object:
            json = request.getParameter("params");
            clientTask=(Task) getMyObject(json, clientTask);
            serverTask=taskRepository.get(clientTask);
            // Fetch an updated TaskList.

            TaskList taskList=getUpdatedTaskList(clientTask);
            //Verify privileges to modify Schedules and Categories.
            verifySchedulePrivileges(taskList.getUserId(), clientTask.getScheduleIds());
            verifyCategoryPrivileges(taskList.getUserId(), clientTask.getCategoryIds());

            // Clean the serverTask
            cleanReferences(serverTask);

            //Update objects with the new Task.
            updateReferences(clientTask);
        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an UpdateTask Request: {}.", json, b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an UpdateTask Request: {}.", json, s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        } catch (CriticalException c) {
            LOGGER.error("An error occurred while handling an UpdateTask Request: {}.", json, c);
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

    private void updateReferences(Task task) throws BusinessException, SystemException, CriticalException{
        TaskList taskList=getUpdatedTaskList(task);
        ArrayList<Schedule> updatedSchedules=getUpdatedSchedules(task);
        ArrayList<Category> updatedCategories=getUpdatedCategories(task);

        taskListRepository.update(taskList);
        taskRepository.update(task);
        for(Schedule schedule: updatedSchedules)
            scheduleRepository.update(schedule);
        for(Category category: updatedCategories)
            categoryRepository.update(category);
    }

    private void cleanReferences(Task task) throws BusinessException, SystemException, CriticalException{
        ArrayList<Schedule> updatedSchedules=getCleanedSchedules(task);
        ArrayList<Category> updatedCategories=getCleanedCategories(task);
        TaskList taskList=getCleanedTaskList(task);

        //Commit changes to Tasks, Schedules and User:
        for(Category category: updatedCategories)
            categoryRepository.update(category);
        for(Schedule schedule: updatedSchedules)
            scheduleRepository.update(schedule);
        taskListRepository.update(taskList);
        taskRepository.update(task);
    }
}

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
 * This api is used to fetch an existing task. Use the class member
 * doPost(HttpServletRequest, HttpServletResponse) to create a new
 * task.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/task/DeleteTask")
public class DeleteTask extends TaskRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTask.class);
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
        JSONObject jsonRequest = new JSONObject();
        Task task = new Task();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            task.setId(parseJsonIntAsInt((String)jsonRequest.get("id")));
            LOGGER.debug("Do we have to track this fucking TaskList for it's entire life?");
            task=taskRepository.get(task);

            TaskList taskList=getCleanedTaskList(task);
            LOGGER.debug("Inside doPost. TaskList: {}", taskList.toJson());
            ArrayList<Category> updatedCategories=getCleanedCategories(task);
            ArrayList<Schedule> updatedSchedules=getCleanedSchedules(task);

            //Commit changes to Schedules, Categories and TaskList:
            for(Schedule schedule: updatedSchedules)
                scheduleRepository.update(schedule);
            for(Category category: updatedCategories)
                categoryRepository.update(category);
            taskListRepository.update(taskList);
            taskRepository.delete(task);

        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling a DeleteTask Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling a DeleteTask Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        } catch (CriticalException c) {
            LOGGER.error("An error occurred while handling a DeleteTask Request: {}.", jsonRequest.toJSONString(), c);
            errorMsg = "Error. " + c.getMessage();
            errorCode = c.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error) {
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
        } else {
            jsonResponse.put("success", true);
            jsonResponse.put("task", task);
        }
        sendMessage(jsonResponse, response);
    }
}

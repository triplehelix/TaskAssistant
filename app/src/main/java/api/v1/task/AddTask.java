package api.v1.task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

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

import api.v1.model.Task;

/**
 * This api is used to create a new task. Use the class member
 * doPost(HttpServletRequest, HttpServletResponse) to create a
 * new task.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/task/AddTask")
public class AddTask extends TaskRequestHandler {

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
            //Create a basic task object:
            jsonRequest = parseRequest(request.getParameter("params"));
            task.setTaskListId(parseJsonIntAsInt((String)jsonRequest.get("taskListId")));
            task.setName((String)jsonRequest.get("name"));
            task.setImportant(parseJsonBooleanAsBoolean((String)jsonRequest.get("important")));
            task.setNote((String)jsonRequest.get("note"));
            task.setEstimatedTime(parseJsonLongAsLong((String)jsonRequest.get("estimatedTime")));
            task.setInvestedTime(parseJsonLongAsLong((String)jsonRequest.get("investedTime")));
            task.setUrgent(parseJsonBooleanAsBoolean((String)jsonRequest.get("urgent")));
            task.setDueDate(parseJsonDateAsDate((String)jsonRequest.get("dueDate")));
            task.setStatus((String)jsonRequest.get("status"));

            // Add an array of schedule and category ids.
            task.setScheduleIds(toIntegerArrayList((String)jsonRequest.get("scheduleIds")));
            task.setCategoryIds(toIntegerArrayList((String)jsonRequest.get("categoryIds")));

            // Fetch an updated TaskList.
            TaskList taskList=getUpdatedTaskList(task);

            //Verify privileges to modify Schedules and Categories.
            verifySchedulePrivileges(taskList.getUserId(), task.getScheduleIds());
            verifyCategoryPrivileges(taskList.getUserId(), task.getCategoryIds());

            ArrayList<Schedule> updatedSchedules=getUpdatedSchedules(task);
            ArrayList<Category> updatedCategories=getUpdatedCategories(task);

            taskRepository.add(task);
        } catch (BusinessException b) {
            log.error("An error occurred while handling an AddTask  Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            log.error("An error occurred while handling an AddTask Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error) {
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
            cleanUp(task);
        } else {
            jsonResponse.put("success", true);
        }
        sendMessage(jsonResponse, response);
    }

    /**
     * Here we attempt to remove a Task from the repository that
     * could not be added fully.
     * @param task
     */
    private void cleanUp(Task task){
        try{
            taskRepository.delete(task);
        } catch (BusinessException b) {
            log.error("Could not remove this category from the categoryRepository. ",b);
        } catch (SystemException s) {
            log.error("Could not remove this category from the categoryRepository. ",s);
        }
    }

}

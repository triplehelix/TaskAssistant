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

import api.v1.model.Task;

/**
 * This api is used to update an existing task.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/task/AddTask")
public class UpdateTask extends TaskRequestHandler {

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
        Task clientTask = new Task();
	    Task serverTask=null;
        try {
            //Create a basic task object:
            jsonRequest = parseRequest(request.getParameter("params"));
            clientTask.setId(parseJsonIntAsInt((String)jsonRequest.get("id")));
            clientTask.setTaskListId(parseJsonIntAsInt((String)jsonRequest.get("taskListId")));
            clientTask.setName((String)jsonRequest.get("name"));
            clientTask.setImportant(parseJsonBooleanAsBoolean((String)jsonRequest.get("important")));
            clientTask.setNote((String)jsonRequest.get("note"));
            clientTask.setEstimatedTime(parseJsonLongAsLong((String)jsonRequest.get("estimatedTime")));
            clientTask.setInvestedTime(parseJsonLongAsLong((String)jsonRequest.get("investedTime")));
            clientTask.setUrgent(parseJsonBooleanAsBoolean((String)jsonRequest.get("urgent")));
            clientTask.setDueDate(parseJsonDateAsDate((String)jsonRequest.get("dueDate")));
            clientTask.setStatus((String)jsonRequest.get("status"));
            clientTask.setScheduleIds(toIntegerArrayList((String)jsonRequest.get("scheduleIds")));
            clientTask.setCategoryIds(toIntegerArrayList((String)jsonRequest.get("categoryIds")));
	        serverTask=taskRepository.get(clientTask);
	    
            // Fetch an updated TaskList.

            TaskList taskList=getUpdatedTaskList(clientTask);
            //Verify privileges to modify Schedules and Categories.
            verifySchedulePrivileges(taskList.getUserId(), clientTask.getScheduleIds());
            verifyCategoryPrivileges(taskList.getUserId(), clientTask.getCategoryIds());


	    // Clean the serverTask
			cleanReferences(serverTask);
            updateReferences(clientTask);
        } catch (BusinessException b) {
            log.error("An error occurred while handling an UpdateTask Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            log.error("An error occurred while handling an UpdateTask Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        } catch (CriticalException c) {
            log.error("An error occurred while handling an UpdateTask Request: {}.", jsonRequest.toJSONString(), c);
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

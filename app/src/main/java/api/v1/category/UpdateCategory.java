package api.v1.category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.CategoryRequestHandler;
import api.v1.model.Schedule;
import api.v1.model.Task;
import api.v1.model.User;
import org.json.simple.JSONObject;
import api.v1.error.CriticalException;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import java.util.ArrayList;

import api.v1.model.Category;

/**
 * This api is used to update a given category. Use the class member
 * doPut(HttpServletRequest, HttpServletResponse) to update this
 * category.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/category/UpdateCategory")
public class UpdateCategory extends CategoryRequestHandler {

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
        Category clientCategory = new Category();
        Category serverCategory;
        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            //Create a Category object.
            clientCategory.setId(parseJsonIntAsInt((String)jsonRequest.get("id")));
            clientCategory.setUserId(parseJsonIntAsInt((String)jsonRequest.get("userId")));
            clientCategory.setName(((String)jsonRequest.get("name")).trim());
            clientCategory.setDescription(((String)jsonRequest.get("description")).trim());
            clientCategory.setTaskIds(toIntegerArrayList((String)jsonRequest.get("taskIds")));
            clientCategory.setScheduleIds(toIntegerArrayList((String)jsonRequest.get("scheduleIds")));
            // Verify privileges.

            verifySchedulePrivileges(clientCategory.getUserId(), clientCategory.getScheduleIds());
            verifyTaskPrivileges(clientCategory.getUserId(), clientCategory.getTaskIds());
            serverCategory=categoryRepository.get(clientCategory);

            cleanReferences(serverCategory);
            updateReferences(clientCategory);
        } catch (BusinessException b) {
            log.error("An error occurred while handling an PutCategory  Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            log.error("An error occurred while handling an PutCategory Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        } catch (CriticalException c) {
            log.error("An error occurred while handling an PutCategory Request: {}.", jsonRequest.toJSONString(), c);
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
     * Clean all references to the Category provided.
     * @param category
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    private void cleanReferences(Category category) throws BusinessException, SystemException, CriticalException
    {
        ArrayList<Schedule> updatedSchedules=getCleanedSchedules(category);
        ArrayList<Task> updatedTasks=getCleanedTasks(category);
        User updatedUser=getCleanedUser(category);

        //Commit changes to Tasks, Schedules and User:
        for(Task task: updatedTasks)
            taskRepository.update(task);
        for(Schedule schedule: updatedSchedules)
            scheduleRepository.update(schedule);
        userRepository.update(updatedUser);
    }


    /**
     * Update objects referenced by category.
     * @param category
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    private void updateReferences(Category category) throws BusinessException, SystemException, CriticalException
    {
        // Create updated Tasks, Schedules and User:
        ArrayList<Task> updatedTasks=getUpdatedTasks(category);
        ArrayList<Schedule> updatedSchedules=getUpdatedSchedules(category);
        User updatedUser=getUpdatedUser(category);
        //Commit changes to Tasks, Schedules and User:

        for(Task task: updatedTasks)
            taskRepository.update(task);
        for(Schedule schedule: updatedSchedules)
            scheduleRepository.update(schedule);
        userRepository.update(updatedUser);
        categoryRepository.update(category);
    }
}

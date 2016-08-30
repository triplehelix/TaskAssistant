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
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import java.util.ArrayList;

import api.v1.model.Category;

/**
 * This api is used to create a new category. Use the class member
 * doPost(HttpServletRequest, HttpServletResponse) to create a
 * new category.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/category/AddCategory")
public class AddCategory extends CategoryRequestHandler {

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
        Category category = new Category();
        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));

            category.setName(((String)jsonRequest.get("name")).trim());
            category.setDescription(((String)jsonRequest.get("description")).trim());
            category.setUserId(parseJsonIntAsInt((String)jsonRequest.get("userId")));
            category.setTaskIds(toIntegerArrayList((String)jsonRequest.get("taskIds")));
            category.setScheduleIds(toIntegerArrayList((String)jsonRequest.get("scheduleIds")));
            category=categoryRepository.add(category);

            // Verify privileges.
            verifyTaskPrivileges(category.getUserId(), category.getTaskIds());
            verifySchedulePrivileges(category.getUserId(), category.getScheduleIds());
            //Place completed category in the repository.

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
        } catch (BusinessException b) {
            log.error("An error occurred while handling an AddCategory  Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            log.error("An error occurred while handling an AddCategory Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error) {
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
            cleanUp(category);
        } else {
            jsonResponse.put("success", true);
            jsonResponse.put("Category", category.toJson());
        }
        sendMessage(jsonResponse, response);
    }

    /**
     * Here we attempt to remove a category from the repository that
     * could not be added fully.
     * @param category
     */
    private void cleanUp(Category category){
        try{
            categoryRepository.delete(category);
        } catch (BusinessException b) {
            log.error("Could not remove this category from the categoryRepository. ",b);
        } catch (SystemException s) {
            log.error("Could not remove this category from the categoryRepository. ",s);
        }
    }
}

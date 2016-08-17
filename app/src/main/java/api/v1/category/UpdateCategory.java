package api.v1.category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskRequestHandler;
import api.v1.model.Task;
import api.v1.model.User;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Category;

/**
 * This api is used to update a given category. Use the class member
 * doPut(HttpServletRequest, HttpServletResponse) to update this
 * category.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/category/UpdateCategory")
public class UpdateCategory extends TaskRequestHandler {

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
            //Create a Category object.
            category.setId(parseJsonIntAsInt((String)jsonRequest.get("id")));
            category.setUserId(parseJsonIntAsInt((String)jsonRequest.get("userId")));
            category.setName(((String)jsonRequest.get("name")).trim());
            category.setDescription(((String)jsonRequest.get("description")).trim());
            category.setTaskIds(toIntegerArrayList((String)jsonRequest.get("taskIds")));
            //TODO UPDATES MUST CLEAN CATEGORIES PRIOR TO MAKING ADDITIONAL CHANGES
            //TODO BETTER YET, VERIFY THE NEW STATE BEFORE CLEANING
            //Validate privleges.
            verifyTaskPrivileges(category.getUserId(), category.getTaskIds());

            //Update the CategoryRepository:
            categoryRepository.update(category);

            //Update Tasks.
            for(int i: category.getTaskIds()) {
                Task task=new Task();
                task.setId(i);
                task=taskRepository.get(task);
                task.addCategory(category);
            }

            // Update User:
             User user=new User();
            user.setId(category.getUserId());
            user=userRepository.get(user);
            user.addCategory(category);

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

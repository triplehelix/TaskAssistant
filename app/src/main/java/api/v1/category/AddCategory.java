package api.v1.category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.CategoryRequestHandler;
import api.v1.model.Task;
import api.v1.model.User;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import java.io.IOException;

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

            // Parse an ArrayList of taskIds to add to a category.
            category.setTaskIds(toIntegerArrayList((String)jsonRequest.get("taskIds")));

            // Verify privileges.
            verifyTaskPrivileges(category.getUserId(), category.getTaskIds());
            //Place completed category in the repository.
            categoryRepository.add(category);

            // Update Tasks.
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
        } else {
            jsonResponse.put("success", true);
            jsonResponse.put("Category", category.toJson());
        }
        sendMessage(jsonResponse, response);
    }
}

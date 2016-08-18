package api.v1.category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskRequestHandler;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.Error;
import api.v1.error.SystemException;
import api.v1.model.Task;
import api.v1.model.User;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.Category;

/**
 * This api is used to delete a given category. Use the class member
 * doDelete(HttpServletRequest, HttpServletResponse) to delete
 * this category.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/category/DeleteCategory")
public class DeleteCategory extends TaskRequestHandler {

	/**
	 * Delete a particular category. A category "id" is required to specify the 
	 * category to be removed.
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
        Category category = new Category();
		JSONObject jsonRequest = new JSONObject();
		try {
		    jsonRequest = parseRequest(request.getParameter("params"));
            category.setId(parseJsonIntAsInt((String)jsonRequest.get("id")));
            category=categoryRepository.get(category);
            removeReferences(category);

		    categoryRepository.delete(category);
		} catch (BusinessException b) {
			log.error("An error occurred while handling an DeleteCategory Request: {}.", jsonRequest.toJSONString(), b);
			errorMsg = "Error. " + b.getMessage();
			errorCode = b.getError().getCode();
			error = true;
		} catch (SystemException s) {
			log.error("An error occurred while handling an DeleteCategory Request: {}.", jsonRequest.toJSONString(), s);
			errorMsg = "Error. " + s.getMessage();
			errorCode = s.getError().getCode();
			error = true;
		} catch (CriticalException c) {
            log.error("An error occurred while handling an PutReminder Request: {}.", jsonRequest.toJSONString(), c);
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
     * Remove references to this Category from the User, and Tasks associated with it.
     * Here we guarantee that no alterations to internal objects are made unless it can
     * be done without error.
     * @param category
     * @throws BusinessException
     * @throws SystemException
     */
	private void removeReferences(Category category) throws BusinessException, SystemException, CriticalException{
        User user=new User();
        user.setId(category.getUserId());
        user=userRepository.get(user);

        for(int i: user.getCategoryIds())
            log.debug("Concerning the user "+user.getEmail()+ ", here is one category id: " + i);
        //log.debug("Category as JSON: " + category.toJson());
        //log.debug("User as JSON: " + user.toJson());
        if(user.getCategoryIds().contains(category.getId())) {
            log.debug("Here is the category id: " + category.getId());
            log.debug("Here are the category ids that belong to the user: " + new Gson().toJson(user.getCategoryIds()));
            user.getCategoryIds().remove((Object)category.getId());
        }
        else
            throw new CriticalException("Critical error! Cannot clean this Category. User {email="
                    + user.getEmail() + ", id=" + user.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));

        Task task=new Task();

        // Verify that all tasks expected to reference this Category actually do reference this category.
        for(int taskId: category.getTaskIds()) {
            task.setId(taskId);
            task = taskRepository.get(task);
            if (!task.getCategoryIds().contains(category.getId()))
                throw new CriticalException("Critical error! Cannot clean this Category. Task {name="
                        + task.getName() + ", id=" + task.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
        }

        // Now we actually remove references to this object.
        for(int taskId: category.getTaskIds()) {
            task.setId(taskId);
            task = taskRepository.get(task);
            if (task.getCategoryIds().contains(category.getId()))
                task.getCategoryIds().remove((Object)category.getId());
        }
    }
}

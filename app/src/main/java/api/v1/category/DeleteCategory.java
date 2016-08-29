package api.v1.category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.CategoryRequestHandler;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.Error;
import api.v1.error.SystemException;
import api.v1.model.Schedule;
import api.v1.model.Task;
import api.v1.model.User;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import java.util.ArrayList;

import api.v1.model.Category;

/**
 * This api is used to delete a given category. Use the class member
 * doDelete(HttpServletRequest, HttpServletResponse) to delete
 * this category.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/category/DeleteCategory")
public class DeleteCategory extends CategoryRequestHandler {

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
            //removeReferences(category);

            ArrayList<Schedule> updatedSchedules=getUpdatedSchedules(category);
            ArrayList<Task> updatedTasks=getUpdatedTasks(category);
            User updatedUser=getUpdatedUser(category);
            //Commit changes to Tasks, Schedules and User:
            for(Task task: updatedTasks)
               taskRepository.update(task);
            for(Schedule schedule: updatedSchedules)
                scheduleRepository.update(schedule);
            userRepository.update(updatedUser);
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
     * Fetch a User that now references the Category provided. Note that
     * this User is a deep copy and that the UserRepository has not yet
     * been updated.
     * @param category
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private User getUpdatedUser(Category category) throws BusinessException, SystemException, CriticalException{
        User user=new User();
        user.setId(category.getUserId());
        user=userRepository.get(user);
        if(user.getCategoryIds().contains(category.getId())) {
            //  log.debug("Here is the category id: " + category.getId());
            //  log.debug("Here are the category ids that belong to the user: " + new Gson().toJson(user.getCategoryIds()));
            user.getCategoryIds().remove((Object)category.getId());
        }
        else
            throw new CriticalException("Critical error! Cannot clean this Category. User {email="
                    + user.getEmail() + ", id=" + user.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
        return user;
    }

    /**
     * Fetch an ArrayList of Tasks that have had their Category ids updated.
     * Note that these Tasks are deep copies, and the Tasks in the repository
     * have not yet been updated.
     * @param category
     * @throws BusinessException
     * @throws SystemException
     */
    private ArrayList<Task> getUpdatedTasks(Category category) throws BusinessException, SystemException, CriticalException{
        ArrayList<Task> myTasks = new ArrayList<Task>();
        if(category.getTaskIds()==null)
            return myTasks;
        for(int i: category.getTaskIds()) {
            Task task=new Task();
            task.setId(i);
            task=taskRepository.get(task);
            if (task.getCategoryIds().contains(category.getId()))
                task.getCategoryIds().remove((Object)category.getId());
            else
                throw new CriticalException("Critical error! Cannot clean this Category. Task {name="
                        + task.getName() + ", id=" + task.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
            myTasks.add(task);
        }
        return myTasks;
    }

    /**
     * Fetch an ArrayList of Schedules that have had their Category ids updated.
     * Note that these Schedules are deep copies, and the Tasks in the repository
     * have not yet been updated.
     * @param category
     * @throws BusinessException
     * @throws SystemException
     */
    private ArrayList<Schedule> getUpdatedSchedules(Category category) throws BusinessException, SystemException, CriticalException{
        ArrayList<Schedule> mySchedule = new ArrayList<Schedule>();
        if(category.getScheduleIds()==null)
            return mySchedule;
        for(int i: category.getScheduleIds()) {
            Schedule schedule=new Schedule();
            schedule.setId(i);
            schedule=scheduleRepository.get(schedule);
            log.debug("Here is the category id: " + category.getId());
            log.debug("Here are the category ids that belong to the schedule: " + new Gson().toJson(schedule.getCategoryIds()));
            if (schedule.getCategoryIds().contains(category.getId())) {
                schedule.getCategoryIds().remove((Object) category.getId());
            }else
                throw new CriticalException("Critical error! Cannot clean this Category. Task {id=" + schedule.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
            mySchedule.add(schedule);
            log.debug("Has anything changed?");
            log.debug("Original Schedule: " + scheduleRepository.get(schedule).toJson());
            log.debug("Updated Schedule: " + schedule.toJson());
        }

        return mySchedule;
    }
}
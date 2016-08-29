package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import api.v1.model.Category;
import api.v1.model.Schedule;
import api.v1.model.Task;
import api.v1.model.User;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 
 * CategoryRequestHandler contains, fields and methods that are common to
 * category APIs. All category APIs inherit CategoryRequestHandler. 
 */
public class CategoryRequestHandler extends TaskRequestHandler {

    /**
     * Remove references to this Category from the User, and Tasks associated with it.
     * Here we guarantee that no alterations to internal objects are made unless it can
     * be done without error.
     * @param category
     * @throws BusinessException
     * @throws SystemException
     */
    protected void removeReferences(Category category) throws BusinessException, SystemException, CriticalException {
        User user=new User();
        user.setId(category.getUserId());
        user=userRepository.get(user);
        for(int i: user.getCategoryIds())
            log.debug("Concerning the user "+user.getEmail()+ ", here is one category id: " + i);
        //log.debug("Category as JSON: " + category.toJson());
        //log.debug("User as JSON: " + user.toJson());
        if(user.getCategoryIds().contains(category.getId())) {
            //  log.debug("Here is the category id: " + category.getId());
            //  log.debug("Here are the category ids that belong to the user: " + new Gson().toJson(user.getCategoryIds()));
            user.getCategoryIds().remove((Object)category.getId());
        }
        else
            throw new CriticalException("Critical error! Cannot clean this Category. User {email="
                    + user.getEmail() + ", id=" + user.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));

        Task task = new Task();
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

    /**
     * Remove references to the supplied category id from an ArrayList of Schedules.
     * @param categoryId
     * @param schedules
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    protected void cleanSchedules(int categoryId, ArrayList<Schedule> schedules) throws BusinessException, SystemException, CriticalException {
        if(schedules==null)
            return;
        for(Schedule schedule: schedules) {
            if (schedule.getCategoryIds().contains(categoryId)) {
                schedule.getCategoryIds().remove((Object) categoryId);
            }else {
                log.error("The category id {" + categoryId +"} is not referenced by the Schedule: " + schedule.toJson());
                throw new CriticalException("Critical error! Cannot clean this Category. Task {id=" + schedule.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
            }
        }
    }

    /**
     * * Remove references to the supplied category id from an ArrayList of Tasks.
     * @param categoryId
     * @param tasks
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    protected void cleanTasks(int categoryId, ArrayList<Task> tasks) throws BusinessException, SystemException, CriticalException {
        if(tasks==null)
            return;
        for(Task task: tasks) {
            if (task.getCategoryIds().contains(categoryId)) {
                task.getCategoryIds().remove((Object) categoryId);
            }else {
                log.error("The category id {" + categoryId +"} is not referenced by the Task: " + task.toJson());
                throw new CriticalException("Critical error! Cannot clean this Category. Task {id=" + task.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
            }
        }
    }

    /**
     * Remove the reference to the category id from the provided User.
     * @param categoryId
     * @param user
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    protected void cleanUser(int categoryId, User user) throws BusinessException, SystemException, CriticalException {
        if(user.getCategoryIds().contains(categoryId)) {
            user.getCategoryIds().remove((Object)categoryId);
        }
        else
            throw new CriticalException("Critical error! Cannot clean this Category. User {email="
                    + user.getEmail() + ", id=" + user.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
    }
}

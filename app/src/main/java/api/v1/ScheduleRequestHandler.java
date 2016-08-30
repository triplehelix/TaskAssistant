package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import api.v1.model.Schedule;
import api.v1.model.Category;
import api.v1.model.Task;
import api.v1.model.User;

import java.util.ArrayList;

/**
 * ScheduleRequestHandler contains, fields and methods that are common to
 * schedule APIs. All schedule APIs inherit ScheduleRequestHandler. 
 */
public class ScheduleRequestHandler extends TaskRequestHandler {
    /**
     * Remove references to the supplied schedule id from an ArrayList of Categories.
     * @param scheduleId
     * @param categories
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    protected void cleanCategories(int scheduleId, ArrayList<Category> categories) throws BusinessException, SystemException, CriticalException {
        if(categories==null)
            return;
        for(Category category: categories) {
            if (category.getScheduleIds().contains(scheduleId)) {
                category.getScheduleIds().remove((Object) scheduleId);
            }else {
                log.error("The schedule id {" + scheduleId +"} is not referenced by the Category: " + category.toJson());
                throw new CriticalException("Critical error! Cannot clean this Schedule. Task {id=" + category.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
            }
        }
    }

    /**
     * * Remove references to the supplied schedule id from an ArrayList of Tasks.
     * @param scheduleId
     * @param tasks
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    protected void cleanTasks(int scheduleId, ArrayList<Task> tasks) throws BusinessException, SystemException, CriticalException {
        if(tasks==null)
            return;
        for(Task task: tasks) {
            if (task.getScheduleIds().contains(scheduleId)) {
                task.getScheduleIds().remove((Object) scheduleId);
            }else {
                log.error("The schedule id {" + scheduleId +"} is not referenced by the Task: " + task.toJson());
                throw new CriticalException("Critical error! Cannot clean this Schedule. Task {id=" + task.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
            }
        }
    }

    /**
     * Remove the reference to the schedule id from the provided User.
     * @param scheduleId
     * @param user
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    protected void cleanUser(int scheduleId, User user) throws BusinessException, SystemException, CriticalException {
        if(user.getScheduleIds().contains(scheduleId)) {
            user.getScheduleIds().remove((Object)scheduleId);
        }
        else
            throw new CriticalException("Critical error! Cannot clean this Schedule. User {email="
                    + user.getEmail() + ", id=" + user.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
    }

    /**
     * Fetch a User that now references the Schedule provided. Note that
     * this User is a deep copy and that the UserRepository has not yet
     * been updated.
     * @param schedule
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    protected User getUpdatedUser(Schedule schedule) throws BusinessException, SystemException{
        User user=new User();
        user.setId(schedule.getUserId());
        user=userRepository.get(user);
        user.addSchedule(schedule);
        return user;
    }

    /**
     * Fetch an ArrayList of Tasks that have had their Schedule ids updated.
     * Note that these Tasks are deep copies, and the Tasks in the repository
     * have not yet been updated.
     * @param schedule
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Task> getUpdatedTasks(Schedule schedule) throws BusinessException, SystemException{
        ArrayList<Task> myTasks = new ArrayList<Task>();
        if(schedule.getTaskIds()==null)
            return myTasks;
        for(int i: schedule.getTaskIds()) {
            Task task=new Task();
            task.setId(i);
            task=taskRepository.get(task);
            task.addSchedule(schedule);
            myTasks.add(task);
        }
        return myTasks;
    }

    /**
     * Fetch an ArrayList of Categories that have had their Schedule ids updated.
     * Note that these Categories are deep copies, and the Tasks in the repository
     * have not yet been updated.
     * @param schedule
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Category> getUpdatedCategories(Schedule schedule) throws BusinessException, SystemException{
        ArrayList<Category> myCategory = new ArrayList<Category>();
        if(schedule.getCategoryIds()==null)
            return myCategory;
        for(int i: schedule.getCategoryIds()) {
            Category category=new Category();
            category.setId(i);
            category=categoryRepository.get(category);
            category.addSchedule(schedule);
            myCategory.add(category);
        }
        return myCategory;
    }
    /**
     * Fetch a User that no longer references the Schedule provided. Note
     * that this User is a deep copy and that the UserRepository has not
     * yet been updated.
     * @param schedule
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    protected User getCleanedUser(Schedule schedule) throws BusinessException, SystemException, CriticalException{
        User user=new User();
        user.setId(schedule.getUserId());
        user=userRepository.get(user);
        cleanUser(schedule.getId(), user);
        return user;
    }

    /**
     * Fetch an ArrayList of Tasks that no longer reference this Schedule.
     * Note that these Tasks are deep copies, and the Tasks in the repository
     * have not yet been updated.
     * @param schedule
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Task> getCleanedTasks(Schedule schedule) throws BusinessException, SystemException, CriticalException{
        ArrayList<Task> myTasks = new ArrayList<Task>();
        if(schedule.getTaskIds()==null)
            return myTasks;
        for(int i: schedule.getTaskIds()) {
            Task task=new Task();
            task.setId(i);
            myTasks.add(taskRepository.get(task));
        }
        cleanTasks(schedule.getId(), myTasks);
        return myTasks;
    }

    /**
     * Fetch an ArrayList of Categories that no longer reference this Schedule.
     * Note that these Categories are deep copies, and the Categories in the
     * repository have not yet been updated.
     *
     * @param schedule
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Category> getCleanedCategories(Schedule schedule) throws BusinessException, SystemException, CriticalException{
        ArrayList<Category> myCategories = new ArrayList<Category>();
        if(schedule.getCategoryIds()==null)
            return myCategories;

        for(int i: schedule.getCategoryIds()) {
            Category category=new Category();
            category.setId(i);
            myCategories.add(categoryRepository.get(category));
        }
        cleanCategories(schedule.getId(), myCategories);
        return myCategories;
    }
}

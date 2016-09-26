package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import api.v1.model.Category;
import api.v1.model.Schedule;
import api.v1.model.Task;
import api.v1.model.User;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;

/**
 * CategoryRequestHandler contains, fields and methods that are common to
 * category APIs. All category APIs inherit CategoryRequestHandler. 
 */
public class CategoryRequestHandler extends AuthRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRequestHandler.class);
    /**
     * Remove references to the supplied category id from an ArrayList of Schedules.
     * @param categoryId
     * @param schedules
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    private void cleanSchedules(int categoryId, ArrayList<Schedule> schedules) throws BusinessException, SystemException, CriticalException {
        if(schedules==null)
            return;
        for(Schedule schedule: schedules) {
            if (schedule.getCategoryIds().contains(categoryId)) {
                schedule.getCategoryIds().remove((Object) categoryId);
            }else {
                LOGGER.error("The category id {" + categoryId +"} is not referenced by the Schedule: " + schedule.toJson());
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
    private void cleanTasks(int categoryId, ArrayList<Task> tasks) throws BusinessException, SystemException, CriticalException {
        if(tasks==null)
            return;
        for(Task task: tasks) {
            if (task.getCategoryIds().contains(categoryId)) {
                task.getCategoryIds().remove((Object) categoryId);
            }else {
                LOGGER.error("The category id {" + categoryId +"} is not referenced by the Task: " + task.toJson());
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
    private void cleanUser(int categoryId, User user) throws BusinessException, SystemException, CriticalException {
        if(user.getCategoryIds().contains(categoryId)) {
            user.getCategoryIds().remove((Object)categoryId);
        }
        else
            throw new CriticalException("Critical error! Cannot clean this Category. User {email="
                    + user.getEmail() + ", id=" + user.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
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
    protected User getUpdatedUser(Category category) throws BusinessException, SystemException{
        User user=new User();
        user.setId(category.getUserId());
        user=userRepository.get(user);
        user.addCategory(category);
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
    protected ArrayList<Task> getUpdatedTasks(Category category) throws BusinessException, SystemException{
        ArrayList<Task> myTasks = new ArrayList<Task>();
        if(category.getTaskIds()==null)
            return myTasks;
        for(int i: category.getTaskIds()) {
            Task task=new Task();
            task.setId(i);
            task=taskRepository.get(task);
            task.addCategory(category);
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
    protected ArrayList<Schedule> getUpdatedSchedules(Category category) throws BusinessException, SystemException{
        ArrayList<Schedule> mySchedule = new ArrayList<Schedule>();
        if(category.getScheduleIds()==null)
            return mySchedule;
        for(int i: category.getScheduleIds()) {
            Schedule schedule=new Schedule();
            schedule.setId(i);
            schedule=scheduleRepository.get(schedule);
            schedule.addCategory(category);
            mySchedule.add(schedule);
        }
        return mySchedule;
    }
    /**
     * Fetch a User that no longer references the Category provided. Note
     * that this User is a deep copy and that the UserRepository has not
     * yet been updated.
     * @param category
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    protected User getCleanedUser(Category category) throws BusinessException, SystemException, CriticalException{
        User user=new User();
        user.setId(category.getUserId());
        user=userRepository.get(user);
        cleanUser(category.getId(), user);
        return user;
    }

    /**
     * Fetch an ArrayList of Tasks that no longer reference this Category.
     * Note that these Tasks are deep copies, and the Tasks in the repository
     * have not yet been updated.
     * @param category
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Task> getCleanedTasks(Category category) throws BusinessException, SystemException, CriticalException{
        ArrayList<Task> myTasks = new ArrayList<Task>();
        if(category.getTaskIds()==null)
            return myTasks;
        for(int i: category.getTaskIds()) {
            Task task=new Task();
            task.setId(i);
            myTasks.add(taskRepository.get(task));
        }
        cleanTasks(category.getId(), myTasks);
        return myTasks;
    }

    /**
     * Fetch an ArrayList of Schedules that no longer reference this Category.
     * Note that these Schedules are deep copies, and the Schedules in the
     * repository have not yet been updated.
     *
     * @param category
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Schedule> getCleanedSchedules(Category category) throws BusinessException, SystemException, CriticalException{
        ArrayList<Schedule> mySchedules = new ArrayList<Schedule>();
        if(category.getScheduleIds()==null)
            return mySchedules;

        for(int i: category.getScheduleIds()) {
            Schedule schedule=new Schedule();
            schedule.setId(i);
            mySchedules.add(scheduleRepository.get(schedule));
        }
        cleanSchedules(category.getId(), mySchedules);
        return mySchedules;
    }

}

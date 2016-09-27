package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.Error;
import api.v1.error.SystemException;
import api.v1.model.*;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;

/**
 * TaskRequestHandler contains, fields and methods that are common to
 * task APIs. All task APIs inherit TaskRequestHandler. 
 */
public class TaskRequestHandler extends AuthRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRequestHandler.class);

    /**
     * Return an ArrayList of Reminders that belong to this Task.
     * @param task
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Reminder> getReminders(Task task) throws BusinessException, SystemException {
        LOGGER.debug("getReminders: Here is the Task we will use to discover Reminders {} {}", task.getId(), task.getName());
        ArrayList<Reminder> reminders=new ArrayList<>();
        if(task.getReminderIds()==null || task.getReminderIds().size()==0)
            return reminders;
        Reminder reminder=new Reminder();
        for(Integer i: task.getReminderIds()) {
            //Reminder reminder=new Reminder();
            LOGGER.debug("Here is the Integer we must use: {}", i);
            reminder.setId(i);
            LOGGER.debug("Here is the reference reminder {}", reminder.toJson());
            reminder = reminderRepository.get(reminder);
            LOGGER.debug("Here is the real reminder {}", reminder.toJson());
            reminders.add(reminder);
            LOGGER.debug("getReminders: Here is a reminder we are going to return: {}", reminder.toJson());
        }

        LOGGER.debug("Finally, lets look at the reminders we are about to return {}", new Gson().toJson(reminders));
        return reminders;
    }

    /**
     * Fetch and update a TaskList object so that it now points to the specified
     * task.
     * @param task
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    protected TaskList getUpdatedTaskList(Task task) throws BusinessException, SystemException {
        TaskList taskList=new TaskList();
        taskList.setId(task.getTaskListId());
        taskList=taskListRepository.get(taskList);
        taskList.addTask(task);
        return taskList;
    }

    /**
     * Fetch an ArrayList of Schedules that have had their Task ids updated.
     * Note that these Schedules are deep copies, and the Tasks in the repository
     * have not yet been updated.
     * @param task
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Schedule> getUpdatedSchedules(Task task) throws BusinessException, SystemException{
        ArrayList<Schedule> mySchedule = new ArrayList<Schedule>();
        if(task.getScheduleIds()==null)
            return mySchedule;
        for(int i: task.getScheduleIds()) {
            Schedule schedule=new Schedule();
            schedule.setId(i);
            schedule=scheduleRepository.get(schedule);
            schedule.addTask(task);
            mySchedule.add(schedule);
        }
        return mySchedule;
    }
    /**
     * Fetch an ArrayList of Categories that have had their Task ids updated. Note
     * that these Categoroes are deep copies, and the Tasks in the repository have
     * not yet been updated.
     * @param task
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Category> getUpdatedCategories(Task task) throws BusinessException, SystemException{
        ArrayList<Category> myCategories = new ArrayList<Category>();
        if(task.getCategoryIds()==null)
            return myCategories;
        for(int i: task.getCategoryIds()) {
            Category category=new Category();
            category.setId(i);
            category=categoryRepository.get(category);
            category.addTask(task);
            myCategories.add(category);
        }
        return myCategories;
    }

    /**
     * Fetch a TaskList that no longer references the Task provided. Note
     * that this TaskList is a deep copy and that the TaskListRepository has not
     * yet been updated.
     * @param task
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    protected TaskList getCleanedTaskList(Task task) throws BusinessException, SystemException, CriticalException{
        TaskList taskList=new TaskList();
        taskList.setId(task.getTaskListId());
        taskList=taskListRepository.get(taskList);
        cleanTaskList(task.getId(), taskList);
        return taskList;
    }
    /**
     * Remove the reference to the task id from the provided TaskList.
     * @param taskId
     * @param taskList
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    private void cleanTaskList(int taskId, TaskList taskList) throws BusinessException, SystemException, CriticalException {
        if(taskList.getTaskIds().contains(taskId)) {
            taskList.getTaskIds().remove((Object)taskId);
        }
        else
            throw new CriticalException("Critical error! Cannot clean this Task. TaskList {id=" + taskList.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
    }

    /**
     * Fetch an ArrayList of Categories that no longer reference this Task.
     * Note that these Categories are deep copies, and the categories in the
     * repository have not yet been updated.
     *
     * @param task
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Category> getCleanedCategories(Task task) throws BusinessException, SystemException, CriticalException{
        ArrayList<Category> myCategories = new ArrayList<Category>();
        if(task.getCategoryIds()==null)
            return myCategories;
        for(int i: task.getCategoryIds()) {
            Category category=new Category();
            category.setId(i);
            myCategories.add(categoryRepository.get(category));
        }
        cleanCategories(task.getId(), myCategories);
        return myCategories;
    }
    /**
     * Remove references to the supplied task id from an ArrayList of Categories.
     * @param taskId
     * @param categories
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    private void cleanCategories(int taskId, ArrayList<Category> categories) throws BusinessException, SystemException, CriticalException {
        if(categories==null)
            return;
        for(Category category: categories) {
            if (category.getTaskIds().contains(taskId)) {
                category.getTaskIds().remove((Object) taskId);
            }else {
                LOGGER.error("The task id {" + taskId +"} is not referenced by the Category: " + category.toJson());
                throw new CriticalException("Critical error! Cannot clean this Task. Task {id=" + category.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
            }
        }
    }

    /**
     * Fetch an ArrayList of Schedules that no longer reference this Task.
     * Note that these Schedules are deep copies, and the Schedules in the
     * repository have not yet been updated.
     *
     * @param task
     * @throws BusinessException
     * @throws SystemException
     */
    protected ArrayList<Schedule> getCleanedSchedules(Task task) throws BusinessException, SystemException, CriticalException{
        ArrayList<Schedule> mySchedules = new ArrayList<Schedule>();
        if(task.getScheduleIds()==null)
            return mySchedules;
        for(int i: task.getScheduleIds()) {
            Schedule schedule=new Schedule();
            schedule.setId(i);
            mySchedules.add(scheduleRepository.get(schedule));
        }
        cleanSchedules(task.getId(), mySchedules);
        return mySchedules;
    }
    /**
     * Remove references to the supplied task id from an ArrayList of Schedules.
     * @param taskId
     * @param schedules
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    private void cleanSchedules(int taskId, ArrayList<Schedule> schedules) throws BusinessException, SystemException, CriticalException {
        if(schedules==null)
            return;
        for(Schedule schedule: schedules) {
            if (schedule.getTaskIds().contains(taskId)) {
                schedule.getTaskIds().remove((Object) taskId);
            }else {
                LOGGER.error("The task id {" + taskId +"} is not referenced by the Schedule: " + schedule.toJson());
                throw new CriticalException("Critical error! Cannot clean this Task. Task {id=" + schedule.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
            }
        }
    }
}

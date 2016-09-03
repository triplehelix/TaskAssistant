package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.Error;
import api.v1.error.SystemException;
import api.v1.model.*;
import api.v1.repo.*;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.util.ArrayList;

/**
 * TaskRequestHandler contains, fields and methods that are common to
 * task APIs. All task APIs inherit TaskRequestHandler. 
 */
public class TaskRequestHandler extends AuthRequestHandler {

    /**
     * Verify that a specified TaskList actually exists. This
     * is used by AddTask and UpdateTask to prevent orphaned
     * reminders.
     */
    protected static void verifyTaskExists(int taskId) throws BusinessException, SystemException {
        Task task = new Task();
        task.setId(taskId);
        taskRepository.get(task);
    }

    /**
     * Adds a new reference to this Reminder to the Task this Reminder belongs to.
     * @param reminder
     * @throws BusinessException
     * @throws SystemException
     */
    protected void addReminderToTask(Reminder reminder) throws BusinessException, SystemException {
        Task task=new Task();
        task.setId(reminder.getTaskId());
        task=taskRepository.get(task);
        task.addReminder(reminder);
    }

    /**
     * Remove the reference to this Reminder from the Task this Reminder belongs to.
     * @param reminder
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    protected void removeReferences(Reminder reminder) throws BusinessException, SystemException, CriticalException {
        Task task=new Task();
        task.setId(reminder.getTaskId());
        task=taskRepository.get(task);
        if(task.getReminderIds().contains(reminder.getId()))
            task.getReminderIds().remove((Object)reminder.getId());
        else
            throw new CriticalException("Critical error! Cannot clean this Category. Task {name="
                    + task.getName() + ", id=" + task.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
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
        log.debug("Best I can tell, this should be really informative. {}", taskList.toJson());
        cleanTaskList(task.getId(), taskList);
        return taskList;
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
    protected void cleanSchedules(int taskId, ArrayList<Schedule> schedules) throws BusinessException, SystemException, CriticalException {
        if(schedules==null)
            return;
        for(Schedule schedule: schedules) {
            if (schedule.getTaskIds().contains(taskId)) {
                schedule.getTaskIds().remove((Object) taskId);
            }else {
                log.error("The task id {" + taskId +"} is not referenced by the Schedule: " + schedule.toJson());
                throw new CriticalException("Critical error! Cannot clean this Task. Task {id=" + schedule.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
            }
        }
    }


    /**
     * Remove references to the supplied task id from an ArrayList of Categories.
     * @param taskId
     * @param categories
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    protected void cleanCategories(int taskId, ArrayList<Category> categories) throws BusinessException, SystemException, CriticalException {
        if(categories==null)
            return;
        for(Category category: categories) {
            if (category.getTaskIds().contains(taskId)) {
                category.getTaskIds().remove((Object) taskId);
            }else {
                log.error("The task id {" + taskId +"} is not referenced by the Category: " + category.toJson());
                throw new CriticalException("Critical error! Cannot clean this Task. Task {id=" + category.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
            }
        }
    }
    /**
     * Remove the reference to the task id from the provided TaskList.
     * @param taskId
     * @param taskList
     * @throws BusinessException
     * @throws SystemException
     * @throws CriticalException
     */
    protected void cleanTaskList(int taskId, TaskList taskList) throws BusinessException, SystemException, CriticalException {
        log.debug("Debugging NPE. Here in 'cleanTaskList(int, TaskList), we have the values {}, and {}", taskId, taskList.toJson());
        if(taskList.getTaskIds().contains(taskId)) {
            taskList.getTaskIds().remove((Object)taskId);
        }
        else
            throw new CriticalException("Critical error! Cannot clean this Task. TaskList {id=" + taskList.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
    }

}

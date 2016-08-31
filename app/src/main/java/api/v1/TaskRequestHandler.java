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

    protected ArrayList<Schedule> getUpdatedSchedules(Task task) throws BusinessException, SystemException {
        return null;
    }

    protected ArrayList<Category> getUpdatedCategories(Task task) throws BusinessException, SystemException{
        return null;
    }

    protected TaskList getCleanedTaskList(Task task) throws BusinessException, SystemException, CriticalException{
        return null;
    }

    protected ArrayList<Category> getCleanedCategories(Task task) throws BusinessException, SystemException, CriticalException{
        return null;
    }

    protected ArrayList<Schedule> getCleanedSchedules(Task task) throws BusinessException, SystemException, CriticalException{
        return null;
    }
}

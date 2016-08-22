package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.Error;
import api.v1.error.SystemException;
import api.v1.model.*;
import api.v1.repo.CategoryRepository;
import api.v1.repo.ReminderRepository;
import api.v1.repo.TaskRepository;
import api.v1.repo.TaskListRepository;

import java.util.ArrayList;

/**
 * TaskRequestHandler contains, fields and methods that are common to
 * task APIs. All task APIs inherit TaskRequestHandler. 
 */
public class TaskRequestHandler extends BaseRequestHandler {
    protected static TaskRepository taskRepository;
    protected static TaskListRepository taskListRepository;
    protected static ReminderRepository reminderRepository;
    protected static CategoryRepository categoryRepository;

    static {
        taskRepository = new TaskRepository();
        taskListRepository = new TaskListRepository();
        reminderRepository = new ReminderRepository();
        categoryRepository = new CategoryRepository();
    }

    public static TaskRepository getTaskRepository(){
        return taskRepository;
    }
    public static TaskListRepository getTaskListRepository() {
        return taskListRepository;
    }
    public static ReminderRepository getReminderRepository() {
        return reminderRepository;
    }
    public static CategoryRepository getCategoryRepository(){
        return categoryRepository;
    }

    /**
     * Verify that a specified TaskList actually exists. This
     * is used by AddTask and UpdateTask to prevent orphaned
     * tasks.
     */
    protected static void verifyTaskListExists(int taskListId) throws BusinessException, SystemException{
        TaskList taskList=new TaskList();
        taskList.setId(taskListId);
        taskListRepository.get(taskList);
    }

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
     * Verify that each taskId supplied belongs to a TaskList that belongs to the 
     * supplied User id.
     * @param userId
     * @param taskIds
     */
    protected void verifyTaskPrivileges(int userId, ArrayList<Integer> taskIds)
            throws BusinessException, SystemException {
        for(Integer i: taskIds){
            // First fetch the Task specified in the taskIds list.
            Task task=new Task();
            task.setId(i);
            task=taskRepository.get(task);

            //Next fetch the TaskList that owns this Task.
            TaskList taskList=new TaskList();
            taskList.setId(task.getTaskListId());
            taskList=taskListRepository.get(taskList);

            log.debug("TaskList owner id: " + taskList.getUserId());
            log.debug("User id: " + userId);
            //Finally, verify that ownership of the TaskList.
            if(taskList.getUserId()==userId)
                return;
            else{
                User user = new User();
                user.setId(userId);
                user=userRepository.get(user);
                String message= "The user {\"email\": " + user.getEmail() + ", \"id\":" + user.getId()
                       + "} does not have permission to access the Task " + task.toJson() + "\n or the TaskList " + taskList.toJson() + ".";
                throw new BusinessException(message, Error.valueOf("OBJECT_OWNERSHIP_ERROR"));
            }
        }
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
     * Remove references to this Category from the User, and Tasks associated with it.
     * Here we guarantee that no alterations to internal objects are made unless it can
     * be done without error.
     * @param category
     * @throws BusinessException
     * @throws SystemException
     */
	protected void removeReferences(Category category) throws BusinessException, SystemException, CriticalException{
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

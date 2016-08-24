package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.Error;
import api.v1.error.SystemException;
import api.v1.model.*;
import api.v1.repo.*;

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
    protected static ScheduleRepository scheduleRepository;

    static {
        taskRepository = new TaskRepository();
        taskListRepository = new TaskListRepository();
        reminderRepository = new ReminderRepository();
        categoryRepository = new CategoryRepository();
        scheduleRepository = new ScheduleRepository();
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
    public static ScheduleRepository getScheduleRepository() { return scheduleRepository; }

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
}

package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.Error;
import api.v1.error.SystemException;
import api.v1.model.Task;
import api.v1.model.TaskList;
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
     * Verify that each taskId supplied can be modified by the TaskListId.
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

            //Finally, verify that ownership of the TaskList.
            if(taskList.getId()==userId)
                return;
            else
                throw new BusinessException("The user " + userId + " does not have permission to access this task. "
                        , Error.valueOf("OBJECT_OWNERSHIP_ERROR"));
        }
    }
}

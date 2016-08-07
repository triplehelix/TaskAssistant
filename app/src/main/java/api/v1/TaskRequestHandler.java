package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.model.Task;
import api.v1.model.TaskList;
import api.v1.repo.CategoryRepository;
import api.v1.repo.ReminderRepository;
import api.v1.repo.TaskRepository;
import api.v1.repo.TaskListRepository;
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
}

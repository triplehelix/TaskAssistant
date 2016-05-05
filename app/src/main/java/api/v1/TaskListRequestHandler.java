package api.v1;
import api.v1.repo.TaskListRepository;

/**
 * TaskListRequestHandler contains, fields and methods that are common to
 * taskList APIs. All taskList APIs inherit TaskListRequestHandler. 
 */
public class TaskListRequestHandler extends BaseRequestHandler {

    protected static TaskListRepository taskListRepository;

    static {
        taskListRepository = new TaskListRepository();
    }
}

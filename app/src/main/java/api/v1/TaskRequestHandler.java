package api.v1;
import api.v1.repo.TaskRepository;

/**
 * The TaskRequestHandler Class provides tools to parse the input
 * expected for a new task. These methods are to be visible to the
 * api class "AddTask", "PutTask", etc...
 */
public class TaskRequestHandler extends BaseRequestHandler{

	protected static TaskRepository taskRepository;
    static {
        taskRepository = new TaskRepository();
    }
}

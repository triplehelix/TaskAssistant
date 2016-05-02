package api.v1;
import api.v1.model.Reminder;
import api.v1.repo.ReminderRepository;
import api.v1.repo.TaskRepository;

import java.util.Date;
import javax.servlet.ServletException;

/**
 * The BaseTaskHadlerClass provides tools to parse the input expected
 * for a new task. These methods are to be visible to the api class
 * "AddTask".
 */
public class TaskRequestHandler extends BaseRequestHandler{

	protected static TaskRepository taskRepository;
    protected static ReminderRepository reminderRepository;
    static {
        taskRepository=new TaskRepository();
        reminderRepository= new ReminderRepository();
    }

	/**
	 * Return an integer representation of a string id.
	 * @param stringId
	 * @return
	 */

}


package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.Error;
import api.v1.error.SystemException;
import api.v1.model.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;

/**
 * TaskRequestHandler contains, fields and methods that are common to
 * task APIs. All task APIs inherit TaskRequestHandler. 
 */
public class ReminderRequestHandler extends TaskRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderRequestHandler.class);
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
}

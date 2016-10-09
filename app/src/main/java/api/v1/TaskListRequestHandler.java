package api.v1;


import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.SystemException;
import api.v1.model.Category;
import api.v1.model.Reminder;
import api.v1.model.Schedule;
import api.v1.model.Task;
import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * TaskListRequestHandler contains, fields and methods that are common to
 * calendar APIs. All calendar APIs inherit TaskListRequestHandler. 
 */
public class TaskListRequestHandler extends TaskRequestHandler {
    private static Logger LOGGER = LoggerFactory.getLogger(TaskListRequestHandler.class);
    /**
     * Remove references to these tasks from Categories and Schedules. Delete
     * these tasks and their reminders.
     * @param taskIds
     */
    protected void cleanTasks(ArrayList<Integer> taskIds) throws BusinessException, CriticalException, SystemException {
        for(Integer i: taskIds) {
            Task task=new Task();
            task.setId(i);
            task=taskRepository.get(task);
            ArrayList<Category> updatedCategories = getCleanedCategories(task);
            ArrayList<Schedule> updatedSchedules = getCleanedSchedules(task);
            ArrayList<Reminder> updatedReminders = getReminders(task);
            for(Schedule schedule: updatedSchedules)
                scheduleRepository.update(schedule);
            for(Category category: updatedCategories)
                categoryRepository.update(category);
            for(Reminder reminder: updatedReminders) {
                //LOGGER.debug("cleanTasks: Here is a reminder we are about to delete {} ", reminder.toJson());
                reminderRepository.delete(reminder);
            }
            taskRepository.delete(task);
        }
    }
}

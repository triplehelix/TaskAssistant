package api.v1;


import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.SystemException;
import api.v1.model.Category;
import api.v1.model.Schedule;
import api.v1.model.Task;

import java.util.ArrayList;

/**
 * TaskListRequestHandler contains, fields and methods that are common to
 * calendar APIs. All calendar APIs inherit TaskListRequestHandler. 
 */
public class TaskListRequestHandler extends TaskRequestHandler {

    /**
     * Remove references to these tasks from Categories and Schedules.
     * @param taskIds
     */
    protected void cleanTasks(ArrayList<Integer> taskIds) throws BusinessException, CriticalException, SystemException {
        for(Integer i: taskIds) {
            Task task=new Task();
            task.setId(i);
            task=taskRepository.get(task);
            ArrayList<Category> updatedCategories = getCleanedCategories(task);
            ArrayList<Schedule> updatedSchedules = getCleanedSchedules(task);
            for(Schedule schedule: updatedSchedules)
                scheduleRepository.update(schedule);
            for(Category category: updatedCategories)
                categoryRepository.update(category);
            taskRepository.delete(task);
        }
    }

    protected void findDeletedTasks(){

    }
}

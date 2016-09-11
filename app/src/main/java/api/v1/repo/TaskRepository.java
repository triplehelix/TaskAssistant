package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;

import java.util.ArrayList;
import java.util.HashMap;
import api.v1.model.Task;

import api.v1.model.TaskList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * This is the ProtoTaskRepository. This class does not yet
 * interact with a database.
 */
public class TaskRepository implements Repository<Task>{
    private static Logger LOGGER = LoggerFactory.getLogger(TaskRepository.class);
    private HashMap<Integer, Task> taskMap;


    /**
     * Create a new instance of a repository.
     */
    public TaskRepository(){
        taskMap=new HashMap<Integer, Task>();
    }

    /**
     * First discover a task id that has not been used. Then copy the incoming
     * task fields into the new task.
     * @param t
     * @throws BusinessException
     * @throws SystemException
     */
    public Task add(Task t) throws BusinessException, SystemException{
        int taskId=0;
        while(taskMap.containsKey(taskId))
            taskId++;
        t.setId(taskId);
        taskMap.put(taskId, t);
        return new Task(t);
    }

    /**
     * @param t
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Task get(Task t)throws BusinessException, SystemException{
        if(taskMap.containsKey(t.getId()))
            return new Task(taskMap.get(t.getId()));
        else
            throw new BusinessException(" Task not found. ", Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     *
     * @param t
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Task t) throws BusinessException, SystemException{
        if (taskMap.containsKey(t.getId())) {
            taskMap.remove(t.getId());
            taskMap.put(t.getId(), t);
        } else
            throw new BusinessException(" Task not found. ID=" + t.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
	}

    /**
     * Deletes the provided task.
     *
     * @param t
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Task t) throws BusinessException, SystemException{
        if(taskMap.containsKey(t.getId())){
            taskMap.remove(t.getId());
        }
        else
            throw new BusinessException(" Task not found. ", Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     * TODO: Return an arrayList of Tasks that belong to the provided TaskList.
     * @return
     */
    public ArrayList<Task> getListOfTasks(TaskList taskList) throws BusinessException, SystemException{
        LOGGER.info("We are now looking for tasks with the TaskList id: " + taskList.getId());
        ArrayList<Task>listOfTaskIds=new ArrayList<Task>();
        for(Task task:taskMap.values())
            if(task.getTaskListId()==taskList.getId())
                listOfTaskIds.add(task);
        if(listOfTaskIds.size()==0)
            throw new BusinessException("No Tasks found for specified TaskList (id="
                    + taskList.getId() + ")."
                    , Error.valueOf("NO_SUCH_OBJECT_ERROR"));
        return listOfTaskIds;
    }
}

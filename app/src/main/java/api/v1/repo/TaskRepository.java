package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
//import java.sql.SQLException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Task;

/**
 *
 * This is the ProtoTaskRepository. This class does not yet
 * interact with a database.
 */
public class TaskRepository implements Repository<Task>{

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
    public void add(Task t) throws BusinessException, SystemException{
	// First, we make sure that the task DNE. Else throw BusinessException
        int taskId=0;
        while(taskMap.containsKey(taskId))
            taskId++;
        Task newTask=new Task(taskId);
        newTask.clone(t);
        taskMap.put(newTask.getId(), newTask);
    }

    /**
     * @param t
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Task get(Task t)throws BusinessException, SystemException{
        if(taskMap.containsKey(t.getId()))
            return taskMap.get(t.getId());
        else
            throw new BusinessException(" Task not found. ", Error.valueOf("NO_SUCH_TASK_ERROR"));
    }


    /**
     *
     * @param t
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Task t) throws BusinessException, SystemException{
        // First, delete the task:
        this.delete(t);
        // Then add the new task:
        this.add(t);
	}

    /**
     * Deletes the provided task.
     *
     * @param t
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Task t) throws BusinessException, SystemException{
	    taskMap.remove(taskMap.get(t.getId()));
    }
}

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
     *
     * @param t
     * @throws BusinessException
     * @throws SystemException
     */

    public void add(Task t) throws BusinessException, SystemException{
	// First, we make sure that the task DNE. Else throw BusinessException
        if(taskDNE(t))
            taskMap.put(taskMap.size(), t);
    }

/**
     * @param t
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Task get(Task t)throws BusinessException, SystemException{
        if(taskMap.containsKey(t))
            return taskMap.get(t);
        else
            throw new BusinessException(" Task not found. ", Error.valueOf("")); //TODO specifty error.
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
        // Then add the new u:
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


    public TaskRepository(){
        taskMap=new HashMap<Integer, Task>();
    }

    private boolean taskDNE(Task t){
        if(taskMap.containsKey(t))
            return false;
        else
            return true;
    }
}

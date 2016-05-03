package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
//import java.sql.SQLException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.TaskList;

/**
 *
 * This is the ProtoTaskListRepository. This class does not yet
 * interact with a database.
 */
public class TaskListRepository implements Repository<TaskList>{

     private HashMap<Integer, TaskList> taskListMap;

    /**
     *
     * @param tl
     * @throws BusinessException
     * @throws SystemException
     */

    public void add(TaskList tl) throws BusinessException, SystemException{
	// First, we make sure that the taskList DNE. Else throw BusinessException
        if(taskListDNE(tl))
            taskListMap.put(taskListMap.size(), tl);
    }

/**
     * @param tl
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public TaskList get(TaskList tl)throws BusinessException, SystemException{
        if(taskListMap.containsKey(tl))
            return taskListMap.get(tl);
        else
            throw new BusinessException(" TaskList not found. ", Error.valueOf("")); //TODO specifty error.
    }


    /**
     *
     * @param tl
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(TaskList tl) throws BusinessException, SystemException{
        // First, delete the taskList:
        this.delete(tl);
        // Then add the new u:
        this.add(tl);
	}

    /**
     * Deletes the provided taskList.
     *
     * @param tl
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(TaskList tl) throws BusinessException, SystemException{
	    taskListMap.remove(taskListMap.get(tl.getId()));
    }


    public TaskListRepository(){
        taskListMap=new HashMap<Integer, TaskList>();
    }

    private boolean taskListDNE(TaskList tl){
        if(taskListMap.containsKey(tl))
            return false;
        else
            return true;
    }
}

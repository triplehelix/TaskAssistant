package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.TaskList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * This is the ProtoTaskListRepository. This class does not yet
 * interact with a database.
 */
public class TaskListRepository implements Repository<TaskList>{
    private static Logger LOGGER = LoggerFactory.getLogger(TaskListRepository.class);
    private HashMap<Integer, TaskList> taskListMap;


    /**
     * Create a new instance of a repository.
     */
    public TaskListRepository(){
        taskListMap=new HashMap<Integer, TaskList>();
    }

    /**
     * First discover a taskList id that has not been used and assign it to
     * the given task. Then, add the new task to the repository.
     *
     * @param foobar
     * @throws BusinessException
     * @throws SystemException
     */
    public void add(TaskList foobar) throws BusinessException, SystemException{
	// First, we make sure that the taskList DNE. Else throw BusinessException
        int taskListId=0;
        while(taskListMap.containsKey(taskListId))
            taskListId++;
        foobar.setId(taskListId);
    //  LOGGER.debug("@ADDING TaskList id: " + taskListId + ".\tTaskList hashCode(): " + foobar.hashCode() + "\tTaskList toJson(): " + foobar.toJson());
        taskListMap.put(taskListId, foobar);
    }

    /**
     * Fetch a taskList object from the repository with the given taskList id.
     * @param foobar
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public TaskList get(TaskList foobar)throws BusinessException, SystemException{
        if(taskListMap.containsKey(foobar.getId()))
            return taskListMap.get(foobar.getId());
        else
            throw new BusinessException(" TaskList not found. ID=" + foobar.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     * Replace an instance of TaskList with the instance provided.
     * @param foobar
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(TaskList foobar) throws BusinessException, SystemException{
        // First, delete the taskList:
        this.delete(foobar);
        // Then add the new taskList:
        this.add(foobar);
	}

    /**
     * Deletes the provided taskList.
     * @param foobar
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(TaskList foobar) throws BusinessException, SystemException {
        //LOGGER.debug("@REMOVING TaskList id: " + foobar.getId() + ".\tTaskList hashCode(): " + foobar.hashCode() + "\tTaskList toJson(): " + foobar.toJson());
        if (taskListMap.containsKey(foobar.getId())) {
            taskListMap.remove(foobar.getId());
        } else {
            throw new BusinessException(" TaskList not found. ID=" + foobar.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
        }
    }
}

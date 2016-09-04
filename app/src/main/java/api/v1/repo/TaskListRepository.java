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
     * @param tl
     * @throws BusinessException
     * @throws SystemException
     */
    public TaskList add(TaskList tl) throws BusinessException, SystemException{
        LOGGER.debug("Here we are placing the TaskList in the repository.   {}", tl.toJson());
        int taskListId=0;
        while(taskListMap.containsKey(taskListId))
            taskListId++;
        tl.setId(taskListId);
        taskListMap.put(taskListId, tl);
        return new TaskList(tl);
    }

    /**
     * Fetch a taskList object from the repository with the given taskList id.
     * @param tl
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public TaskList get(TaskList tl)throws BusinessException, SystemException{ 
       if(taskListMap.containsKey(tl.getId()))
            return new TaskList(taskListMap.get(tl.getId()));
        else
            throw new BusinessException(" TaskList not found. ID=" + tl.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     * Replace an instance of TaskList with the instance provided.
     * @param tl
     * @throws BusinessException
     * @throws SystemException
     */
    public void update(TaskList tl) throws BusinessException, SystemException{
        if (taskListMap.containsKey(tl.getId())) {
            taskListMap.remove(tl.getId());
            taskListMap.put(tl.getId(), tl);
        } else
            throw new BusinessException(" TaskList not found. ID=" + tl.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     * Deletes the provided taskList.
     * @param tl
     * @throws BusinessException
     * @throws SystemException
     */
    public void delete(TaskList tl) throws BusinessException, SystemException {
        //LOGGER.debug("@REMOVING TaskList id: " + tl.getId() + ".\tTaskList hashCode(): " + tl.hashCode() + "\tTaskList toJson(): " + tl.toJson());
        if (taskListMap.containsKey(tl.getId())) {
            taskListMap.remove(tl.getId());
        } else {
            throw new BusinessException(" TaskList not found. ID=" + tl.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
        }
    }
}

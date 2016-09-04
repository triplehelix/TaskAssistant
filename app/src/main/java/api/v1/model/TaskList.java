package api.v1.model;
import api.v1.error.BusinessException;
import api.v1.error.Error;
import api.v1.helper.ModelHelper;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.util.ArrayList;

/**
 * This class serves as a container to which tasks must belong.
 * @author kennethlyon
 */
public class TaskList {
	private int id;
    private int userId;
    private String name;
	private String description;
    private ArrayList<Integer> taskIds;
    /**
     * Create a new TaskList w/o a taskList id. TasksLists created without
     * an id are assigned an id of -1.
     */
	public TaskList(){
        this.id=-1;
        this.userId=-1;
	}

    /**
     * Create a deep copy of this TaskList.
     * @param taskList
     */
	public TaskList(TaskList taskList){
	    this.id=taskList.getId();
        this.userId=taskList.getUserId();
        this.name=new String(taskList.getName());
        this.description=new String(taskList.getDescription());
        this.taskIds= ModelHelper.copyIntegerArrayList(taskList.getTaskIds());
    }

    public String getName() {
        return name;
    }

    /**
     * This method sets the name of a task list. Null and empty
     * strings throw an exception.
     *
     * @param name
     * @throws BusinessException
     */
    public void setName(String name) throws BusinessException{
        this.name=name.trim();
        if(this.name==null || this.name.equals(""))
            throw new BusinessException("The task name cannot be empty.", Error.valueOf("INVALID_NAME_ERROR"));
    }

	public int getId() {
		return id;
	}

	public void setId(int id) throws BusinessException{
		this.id=id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public ArrayList<Integer> getTaskIds() {
        return taskIds;
    }
    public void setTaskIds(ArrayList<Integer> tasks) {
        this.taskIds = taskIds;
    }
    public void addTask(Task task){
        if(taskIds==null)
            taskIds=new ArrayList<Integer>();
        taskIds.add(task.getId());
    }
    /**
     * Create a serialized JSON String of this instance
     * using GSON.
     * @return
     */
    public String toJson(){
        Gson gson=new Gson();
        return gson.toJson(this);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskList taskList = (TaskList) o;

        if (id != taskList.id) return false;
        if (userId != taskList.userId) return false;
        if (!name.equals(taskList.name)) return false;
        if (description != null ? !description.equals(taskList.description) : taskList.description != null)
            return false;
        return taskIds != null ? taskIds.equals(taskList.taskIds) : taskList.taskIds == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (taskIds != null ? taskIds.hashCode() : 0);
        return result;
    }
}

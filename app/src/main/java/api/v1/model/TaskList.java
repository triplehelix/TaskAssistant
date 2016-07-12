package api.v1.model;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.util.ArrayList;

/**
 * This class serves as a list of Tasks. 
 * TODO decide how we want to order prioritize tasks. Should this
 * be done here or in the view?
 * @author kennethlyon
 */
public class TaskList {
	private int id;
	private ArrayList<Integer> taskIdList;
    private String name;
	private String description;
	
    /**
     * Create a new TaskList w/o a taskList id. TasksLists created without
     * an id are assigned an id of -1.
     */
	public TaskList(){
		this.id=-1;
	}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getTaskIdList() {
       // TODO Search through the TaskRepository for Tasks that belong to this TaskList.
		return taskIdList;
	}

	public void setTaskArrayList(ArrayList<Integer> taskArrayList) {
		this.taskIdList = taskArrayList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
}

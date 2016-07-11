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
	private ArrayList<Task> taskArrayList;


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

	public ArrayList<Task> getTaskArrayList() {
		return taskArrayList;
	}
	public void setTaskArrayList(ArrayList<Task> taskArrayList) {
		this.taskArrayList = taskArrayList;
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

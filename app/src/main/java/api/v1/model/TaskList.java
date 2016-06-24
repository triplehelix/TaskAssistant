package api.v1.model;
import java.util.ArrayList;

/**
 * This class serves as a list of Tasks. 
 * TODO decide how we want to order prioritize tasks. Should this
 * be done here or in the view?
 * @author kennethlyon
 */
public class TaskList {	
	private ArrayList<Task> taskArrayList;
	private int id;
	private String description;
	
    /**
     * Create a new TaskList w/o a taskList id. TasksLists created without
     * an id are assigned an id of -9.
     */
	public TaskList(){
		this.id=-9;
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
}

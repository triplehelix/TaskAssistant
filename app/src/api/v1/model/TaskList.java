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
	 * Use this constructor to fetch a TaskList that already exists.
	 * @param id
	 */
	public TaskList(int id){
		
	}
	
	public TaskList(){
		this.taskArrayList = new ArrayList<Task>();
	}	
}

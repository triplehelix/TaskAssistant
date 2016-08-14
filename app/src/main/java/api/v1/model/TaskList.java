package api.v1.model;
import api.v1.error.BusinessException;
import api.v1.error.Error;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.util.ArrayList;

/**
 * This class serves as a container to which tasks must belong.
 * @author kennethlyon
 */
public class TaskList {
	private int id;
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
        if(id<0)
            throw new BusinessException("Invalid id: " + id + ". A non-negative TaskList id is required", Error.valueOf("INVALID_ID_ERROR"));
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

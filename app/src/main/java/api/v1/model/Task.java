package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class serves as a basic data structure for tasks.
 * @author kennethlyon
 */
public class Task {
    private int id;
    private int taskListId;
    private String name;
    private String note;
    private long estimatedTime;
    private long investedTime;
    private boolean important;
    private boolean urgent;
    private Date dueDate;
    private Status status;
    public enum Status{NEW, IN_PROGRESS, DELEGATED, DEFERRED, DONE};
    private ArrayList<Integer> categoryIds;

    public ArrayList<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(ArrayList<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

	/**
	 * Create a new task w/o an id. Tasks created without an id are assigned
     * an id of -1.
	 */
	public Task(){
        this.id=-1;
        this.taskListId=-1;
		this.note="";
		this.estimatedTime=0;
		this.investedTime=0;
		this.urgent=false;
		this.dueDate=null;
        this.status=Status.valueOf("NEW");
	}

    public void setId(int id) throws BusinessException{
        if(id<0)
            throw new BusinessException("Invalid id: " + id + ". A non-negative Task id is required", Error.valueOf("INVALID_ID_ERROR"));
        this.id=id;
    }

    public void setTaskListId(int taskListId) throws BusinessException{
        if(taskListId<0)
            throw new BusinessException("Invalid id: " + taskListId + ". A non-negative TaskList id is required", Error.valueOf("INVALID_ID_ERROR"));
        this.taskListId = taskListId;
    }

    public void setName(String name)throws BusinessException{
        if(name==null || name.equals(""))
            throw new BusinessException("The task name cannot be empty.", Error.valueOf("INVALID_NAME_ERROR"));
        this.name=name;
    }

    public void setNote(String note){
	this.note = note;
	}

    /**
     * The amount of continuous time a expected for a task to
     * be completed.
     * @param estimatedTime
     */
    public void setEstimatedTime(long estimatedTime){
	this.estimatedTime = estimatedTime;
    }
    /**
     * The amount of time invested in this task so far.
     * @param investedTime
     */
    public void setInvestedTime(long investedTime){
	this.investedTime = investedTime;
    }
    public void setImportant(boolean important){
        this.important=important;
    }
    public void setUrgent(boolean urgent){
    this.urgent = urgent;
    }
    public void setDueDate(Date dueDate){
        this.dueDate = dueDate;
    }
    public void setStatus(String status) throws BusinessException{
        try {
            this.status = Status.valueOf(status);
        }
        catch(java.lang.IllegalArgumentException e){
            throw new BusinessException(status +" is not a valid task status.", Error.valueOf("INVALID_TASK_STATUS_ERROR"));
        }
    }

    public int getId(){
        return this.id;
    }
    public int getTaskListId() {
        return taskListId;
    }
    public String getName(){
        return this.name;
    }
    public String getNote(){
        return this.note;
    }
    public long getEstimatedTime(){
        return this.estimatedTime;
    }
    public long getInvestedTime(){
        return this.investedTime;
    }
    public boolean getImportant(){
        return this.important;
    }
    public boolean getUrgent(){
        return this.urgent;
    }
    public Date getDueDate(){
        return this.dueDate;
    }
    public Status getStatus() {
        return status;
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

    public void addCategory(Category category){
        if(null==categoryIds)
            categoryIds=new ArrayList<Integer>();
        categoryIds.add(category.getId());
    }
}

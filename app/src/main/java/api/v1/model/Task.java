package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.util.Date;

/**
 * This class serves as a basic data structure for tasks.
 * @author kennethlyon
 */
public class Task {
    private int id;
    private String name;
    private boolean important;
    private String note;
    private long estimatedTime;
    private long investedTime;
    private boolean urgent;
    private Date dueDate;
    public enum Status{NEW, IN_PROGRESS, DELEGATED, DEFERRED, DONE};
    private Status status;


	/**
	 * Create a new task w/o an id. Tasks created without an id are assigned
     * an id of -1.
	 */
	public Task(){
        this.id=-1;
		this.note="";
		this.estimatedTime=0;
		this.investedTime=0;
		this.urgent=false;
		this.dueDate=null;
        this.status=Status.valueOf("NEW");
	}
    public void setId(int id){
        this.id=id;
    }
    public void setName(String name)throws BusinessException{
        if(name==null || name.equals(""))
            throw new BusinessException("The task name cannot be empty.", Error.valueOf("INVALID_TASK_NAME_ERROR"));
        this.name=name;
    }
    public void setImportant(boolean important){
        this.important=important;
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

    /**
     * Copy the member fields of t into this.
     * @param t
     */
    public void clone(Task t){
        this.id=t.getId();
        this.name=t.getName();
        this.note=t.getNote();
        this.important=t.getImportant();
		this.estimatedTime=t.getEstimatedTime();
		this.investedTime=t.getInvestedTime();
		this.urgent=t.getUrgent();
		this.dueDate=t.getDueDate();
        this.status=t.getStatus();
    }

    public Status getStatus() {
        return status;
    }
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getNote(){
        return this.note;
    }
    public boolean getImportant(){
        return this.important;
    }
    public long getEstimatedTime(){
        return this.estimatedTime;
    }
    public long getInvestedTime(){
        return this.investedTime;
    }
    public boolean getUrgent(){
        return this.urgent;
    }
    public Date getDueDate(){
        return this.dueDate;
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

package api.v1.model;

import java.util.Date;

/**
 * 
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
    public enum State{NEW, IN_PROGRESS, DELEGATED, DEFERRED, DONE};
    private State status;

    /**
	 * Create a task with a user id.
	 */
	public Task(int id)
	{
		this.id=id;
		this.note="";
		this.estimatedTime=0;
		this.investedTime=0;
		this.urgent=false;
		this.dueDate=null;
        this.status=State.valueOf("NEW");
	}
	
	/**
	 * Create a new task w/o an id. Tasks created without an id are assigned
     * an id of -9.
	 */
	public Task(){
        this.id=-9;
		this.note="";
		this.estimatedTime=0;
		this.investedTime=0;
		this.urgent=false;
		this.dueDate=null;
        this.status=State.valueOf("NEW");
	}

    public void setName(String name){
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

    public void setStatus(State status){
        this.status=status;
    }

    public State getStatus() {
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
}

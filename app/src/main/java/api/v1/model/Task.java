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
	private enum Status{NEW, IN_PROGRESS, DELIGATED, DEFERRED, DONE};
	private Status status;
	/* Mike thinks we need to add a "status" field. Methinks this is a good idea.
	 * TODO all status field. Should be enum.
	 */
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
	}
	
	/**
	 * Create a new task w/o an id.
	 */
	public Task(){
		this.note="";
		this.estimatedTime=0;
		this.investedTime=0;
		this.urgent=false;
		this.dueDate=null;
	}
	
    public void setNote(String note){
	this.note = note;
	}

    public void setEstimatedTime(long estimatedTime){
	//TODO what is the relationship between estimatedTime and investedTime
	this.estimatedTime = estimatedTime;
    }
    public void setInvestedTime(long investedTime){
	//TODO what is the relationship between estimatedTime and investedTime
	this.investedTime = investedTime;
    }
    private void setUrgent(boolean urgent){
	this.urgent = urgent;
    }
    public void setDueDate(Date dueDate){
    	this.dueDate = dueDate;
    }
    
    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public String getNote(){return this.note;}
    public boolean getImportant(){return this.important;}
    public long getEstimatedTime(){return this.estimatedTime;}
    public long getInvestedTime(){return this.investedTime;}
    public boolean getUrgent(){return this.urgent;}
    public Date getDueDate(){return this.dueDate;}
    
}

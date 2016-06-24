package api.v1.model;

import java.util.Date;

/**
 * 
 * @author kennethlyon
 *
 */
public class Reminder {
	private int id;
	private int taskId;
	private Date reminderTime;
	
	/**
	 * Create a new, Reminder w/o an reminder id. Reminders without an id are
     * assigned an id of -9.
	 */
	public Reminder(){
        this.id=-9;
	}

	public int getId() {
		return id;
	}

	public void setId(int id){
		this.id=id;
	}


	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public Date getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(Date reminderTime) {
		this.reminderTime = reminderTime;
	}
}

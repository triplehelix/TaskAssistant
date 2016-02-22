package api.v1.model;

import java.util.Date;

public class Reminder {
	private int id;
	private int taskId;
	private Date reminderTime;
	
	/**
	 * Create a Reminder object that already has an id.
	 * Such a reminder must come from the database.
	 * @param id
	 */
	public Reminder(int id){
		this.id=id;
	}
	
	/**
	 * Create a new, empty Reminder.
	 */
	public Reminder(){
	}

	public int getId() {
		return id;
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


package api.v1.model;

import java.util.Date;

public class Schedule {
	private int id;
	private Date startDate;
	private Date endDate;
	public static enum RepeatTypes {NONE, DAILY, WEEKLY, MONTHLY, YEARLY};
	private RepeatTypes repeatType;	
	
	/**
	 * Create a complete Schedule object. Such an object only comes from the 
	 * database.
	 * 
	 * @param id
	 * @param startDate
	 * @param endDate
	 * @param repeatType
	 */
	public Schedule(int id){
		this.id = id;
	}

	/**
	 * Create a new, empty schedule.
	 */
	public Schedule(){
	}
	
	public int getId() {
		return id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public RepeatTypes getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(RepeatTypes repeatType) {
		this.repeatType = repeatType;
	}
	
}

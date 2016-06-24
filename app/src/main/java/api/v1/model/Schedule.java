package api.v1.model;

import java.util.Date;

public class Schedule {
	private int id;
	private Date startDate;
	private Date endDate;
	public static enum RepeatTypes {NONE, DAILY, WEEKLY, MONTHLY, YEARLY};
	private RepeatTypes repeatType;	
	
	/**
	 * Create a new schedule w/o an id. Schedules without an id are
     * assigned an id of -9.
	 */
	public Schedule(){
		this.id=-9;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id){
		this.id=id;
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

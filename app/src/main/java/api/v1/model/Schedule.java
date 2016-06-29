package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import java.util.Date;

public class Schedule {
	private int id;
	private Date startDate;
	private Date endDate;
	public static enum RepeatTypes {NONE, DAILY, WEEKLY, MONTHLY, YEARLY};
	private RepeatTypes repeatType;	
	
	/**
	 * Create a new schedule w/o an id. Schedules without an id are
     * assigned an id of -1.
	 */
	public Schedule(){
		this.id=-1;
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

	public void setRepeatType(String repeatType) throws BusinessException{
		try {
			this.repeatType = RepeatTypes.valueOf(repeatType);
		}
		catch(java.lang.IllegalArgumentException e){
			throw new BusinessException(repeatType +" is not a valid schedule repeat type.", Error.valueOf("INVALID_TASK_STATUS_ERROR"));
		}
	}
	
}

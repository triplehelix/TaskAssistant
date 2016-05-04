package api.v1.model;

/**
 * Note that this is a dummy class. At the moment I am not sure how
 * to handle the google calendar objects so  a substantial revision
 * may be required for this class.
 * 
 * See: 
 *    developers.google.com/google-apps/calendar/quickstart/java
 *    com.google.api.services.calendar.Calendar.Builder
 *
 * @author kennethlyon
 *
 *
 */
public class Calendar {
	private int id;
	private String name;
	public static enum CalendarTypes{};
	private CalendarTypes calendarType;
    private String remoteId;
	private String remoteToken;

	/**
	 * Create a Calendar that already has an assigned id.
	 * @param id
	 */
	public Calendar(int id){
		this.id=id;
	}
	
	/**
	 * Create a new, empty calendar object.
	 */
	public Calendar(){
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CalendarTypes getCalendarType() {
		return calendarType;
	}
	public void setCalendarType(CalendarTypes calendarType) {
		this.calendarType = calendarType;
	}
	public String getRemoteId() {
		return remoteId;
	}
	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}
	public String getRemoteToken() {
		return remoteToken;
	}
	public void setRemoteToken(String remoteToken) {
		this.remoteToken = remoteToken;
	}
}

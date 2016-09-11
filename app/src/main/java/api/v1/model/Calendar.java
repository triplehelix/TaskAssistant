package api.v1.model;

import com.google.appengine.repackaged.com.google.gson.Gson;

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
 */
public class Calendar {
	private int id;
	private String name;
	public enum CalendarTypes{};
	private CalendarTypes calendarType;
    private String remoteId;
	private String remoteToken;

	/**
	 * Create a new calendar w/o a calendar id. Calendars without an id are
     * assigned an id of -1.
	 */
	public Calendar(){
        this.id = -1;
    }

    public void setId(int id){
        this.id=id;
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

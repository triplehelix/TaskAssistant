package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;

public class User {
	private int id;
	private String email;
	private String password;
    private ArrayList<Integer> calendarIds;
    private ArrayList<Integer> categoryIds;
    private ArrayList<Integer> scheduleIds;
    private ArrayList<Integer> taskListIds;

    protected static final Logger log = LoggerFactory.getLogger(User.class);

	/**
	 * Create a new User w/o an user id. Users created without an id
     * are assigned an id of -1.
	 */
	public User(){
        this.id=-1;
	}
	public int getId() {return id;}
	public void setId(int id){
		this.id=id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) throws BusinessException {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            this.email = email;
        } catch (AddressException ex) {
            log.error("Supplied email address: {} is not valid.", email);
            throw new BusinessException("Email address: " + email + " is invalid.", Error.valueOf("INVALID_EMAIL_ERROR"));
        }
	}

    /**
     * @param password
     * @throws BusinessException
     */
	public void setPassword(String password) {
            this.password=password;
	}

    public ArrayList<Integer> getTaskListIds() {
        return taskListIds;
    }

    public void setTaskListIds(ArrayList<Integer> taskListIds) {
        this.taskListIds = taskListIds;
    }

    public ArrayList<Integer> getScheduleIds() {
        return scheduleIds;
    }

    public void setScheduleIds(ArrayList<Integer> scheduleIds) {
        this.scheduleIds = scheduleIds;
    }

    public ArrayList<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(ArrayList<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public ArrayList<Integer> getCalendarIds() {
        return calendarIds;
    }

    public void setCalendarIds(ArrayList<Integer> calendarIds) {
        this.calendarIds = calendarIds;
    }

    public String getPassword(){
        return this.password;
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

    public void addCategory(Category category){
        if(categoryIds==null)
            categoryIds=new ArrayList<Integer>();
        categoryIds.add(category.getId());
    }
}

package api.v1.model;


import api.v1.error.Error;
import api.v1.error.BusinessException;
import api.v1.helper.ModelHelper;
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
        calendarIds=new ArrayList<Integer>();
        categoryIds=new ArrayList<Integer>();
        scheduleIds=new ArrayList<Integer>();
        taskListIds=new ArrayList<Integer>();
	}
    /**
     * Return an deep copy of a given User.
     * @param user
     */
    public User(User user){
        this.id=user.getId();
        this.email=new String(user.getEmail());
        this.password=new String(user.getPassword());
        this.calendarIds = ModelHelper.copyIntegerArrayList(user.getCalendarIds());
        this.categoryIds = ModelHelper.copyIntegerArrayList(user.getCategoryIds());
        this.scheduleIds = ModelHelper.copyIntegerArrayList(user.getScheduleIds());
        this.taskListIds = ModelHelper.copyIntegerArrayList(user.getTaskListIds());
    }
	public int getId() {return id;}
	public void setId(int id){
		this.id=id;
	}
	public String getEmail() {
		return email;
	}

    /**
     *
     * @param email
     * @throws BusinessException
     */
	public void setEmail(String email){
            this.email = email;
	}

    /**
     *
     * @param password
     * @throws BusinessException
     */
	public void setPassword(String password){
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

    public void addTaskList(TaskList taskList){
        if(taskListIds==null)
            taskListIds=new ArrayList<Integer>();
        taskListIds.add(taskList.getId());
    }
    
    public void addSchedule(Schedule schedule){
        if(scheduleIds==null)
            scheduleIds=new ArrayList<Integer>();
        scheduleIds.add(schedule.getId());
    }
    
    public void addCalendar(Calendar calendar){
        if(calendarIds==null)
            calendarIds=new ArrayList<Integer>();
        calendarIds.add(calendar.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (calendarIds != null ? !calendarIds.equals(user.calendarIds) : user.calendarIds != null) return false;
        if (categoryIds != null ? !categoryIds.equals(user.categoryIds) : user.categoryIds != null) return false;
        if (scheduleIds != null ? !scheduleIds.equals(user.scheduleIds) : user.scheduleIds != null) return false;
        return taskListIds != null ? taskListIds.equals(user.taskListIds) : user.taskListIds == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (calendarIds != null ? calendarIds.hashCode() : 0);
        result = 31 * result + (categoryIds != null ? categoryIds.hashCode() : 0);
        result = 31 * result + (scheduleIds != null ? scheduleIds.hashCode() : 0);
        result = 31 * result + (taskListIds != null ? taskListIds.hashCode() : 0);
        return result;
    }
}

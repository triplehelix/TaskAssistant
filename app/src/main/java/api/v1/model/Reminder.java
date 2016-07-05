package api.v1.model;

import com.google.appengine.repackaged.com.google.gson.Gson;

import java.util.Date;

/**
 * TODO should this be implemented as a google calendar reminder?
 * @author kennethlyon
 *
 */
public class Reminder {
    private int id;
    private int taskId;
    private Date reminderTime;
    
    /**
     * Create a new, Reminder without an id. Reminders are
     * assigned an id of -1 when created.
     */
    public Reminder(){
        this.id=-1;
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

package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import com.google.appengine.repackaged.com.google.gson.Gson;

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
     * Create a new, Reminder without an id. Reminders are
     * assigned an id of -1 when created.
     */
    public Reminder(){
        this.id=-1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) throws BusinessException{
        if(id<0)
            throw new BusinessException("Invalid id: " + id + ". A non-negative Task id is required", Error.valueOf("INVALID_ID_ERROR"));
        this.id=id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) throws BusinessException{
        if(taskId<0)
            throw new BusinessException("Invalid id: " + id + ". A non-negative Task id is required", Error.valueOf("INVALID_ID_ERROR"));
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

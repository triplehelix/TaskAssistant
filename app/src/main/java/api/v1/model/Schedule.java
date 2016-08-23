package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class Schedule {
    private int id;
    private int userId;
    private Date startDate;
    private Date endDate;
    public static enum RepeatTypes {NONE, DAILY, WEEKLY, MONTHLY, YEARLY};
    private RepeatTypes repeatType;
    ArrayList<Integer> taskIds;
    ArrayList<Integer> categoryIds;
    
    /**
     * Create a new schedule w/o an id. Schedules without an id are
     * assigned an id of -1.
     */
    public Schedule(){
        this.id=-1;
        this.userId=-1;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRepeatType(RepeatTypes repeatType) {
        this.repeatType = repeatType;
    }

    public ArrayList<Integer> getTaskIds() {

        return taskIds;
    }

    public void setTaskIds(ArrayList<Integer> taskIds) {
        this.taskIds = taskIds;
    }

    public ArrayList<Integer> getCategoryIds() {

        return categoryIds;
    }

    public void setCategoryIds(ArrayList<Integer> categoryIds) {
        this.categoryIds = categoryIds;
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

package api.v1.model;

import api.v1.error.Error;
import api.v1.error.BusinessException;
import api.v1.helper.ModelHelper;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;

public class Schedule {
    private int id;
    private int userId;
    private Date startDate;
    private Date endDate;
    private RepeatTypes repeatType;
    public enum RepeatTypes {NONE, DAILY, WEEKLY, MONTHLY, YEARLY};
    ArrayList<Integer> categoryIds;
    ArrayList<Integer> taskIds;

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

    public void setRepeatType(String repeatType) throws BusinessException{
        try {
            this.repeatType = RepeatTypes.valueOf(repeatType);
        }
        catch(java.lang.IllegalArgumentException e){
            throw new BusinessException(repeatType +" is not a valid task status.", Error.valueOf("INVALID_SCHEDULE_REPEAT_TYPE_ERROR"));
        }
    }

    public ArrayList<Integer> getTaskIds() {

        return taskIds;
    }

    public void setTaskIds(ArrayList<Integer> taskIds) {
        this.taskIds = taskIds;
    }

    public void addTask(Task task){
        if(this.taskIds==null)
            taskIds=new ArrayList<Integer>();
        taskIds.add(task.getId());
    }

    public ArrayList<Integer> getCategoryIds() {

        return categoryIds;
    }

    public void setCategoryIds(ArrayList<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public void addCategory(Category category){
        if(categoryIds==null)
            categoryIds=new ArrayList<Integer>();
        categoryIds.add(category.getId());
    }

    /**
     * Create a serialized JSON String of this instance
     * using GSON.
     * @return
     */
    public String toJson(){
        String format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Gson gson = new GsonBuilder().setDateFormat(format).create();
        //Gson gson=new Gson();
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        if (id != schedule.id) return false;
        if (userId != schedule.userId) return false;
        if (startDate != null ? !startDate.equals(schedule.startDate) : schedule.startDate != null) return false;
        if (endDate != null ? !endDate.equals(schedule.endDate) : schedule.endDate != null) return false;
        if (repeatType != schedule.repeatType) return false;
        if (taskIds != null ? !taskIds.equals(schedule.taskIds) : schedule.taskIds != null) return false;
        return categoryIds != null ? categoryIds.equals(schedule.categoryIds) : schedule.categoryIds == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + repeatType.hashCode();
        result = 31 * result + (taskIds != null ? taskIds.hashCode() : 0);
        result = 31 * result + (categoryIds != null ? categoryIds.hashCode() : 0);
        return result;
    }

    /**
     * @param schedule
     */
    public Schedule(Schedule schedule){
        this.id=schedule.getId();
        this.userId=schedule.getUserId();
        this.startDate=new Date(schedule.getStartDate().getTime());
        this.endDate=new Date(schedule.getEndDate().getTime());
        this.repeatType=RepeatTypes.valueOf(schedule.getRepeatType().toString());
        this.categoryIds=ModelHelper.copyIntegerArrayList(schedule.getCategoryIds());
        this.taskIds= ModelHelper.copyIntegerArrayList(schedule.getTaskIds());
    }
}

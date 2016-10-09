package api.v1.model;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;

//import java.time.Instant;
import java.util.Date;

/**
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
        this.taskId=-1;
    }

    /**
     * Create a copy of the given Reminder.
     * @param reminder
     */
    public Reminder(Reminder reminder){
        this.id=reminder.getId();
        this.taskId=reminder.getTaskId();
        this.reminderTime=new Date(reminder.getReminderTime().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId){
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
        String format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Gson gson = new GsonBuilder().setDateFormat(format).create();
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reminder reminder = (Reminder) o;

        if (id != reminder.id) return false;
        if (taskId != reminder.taskId) return false;
        return reminderTime != null ? reminderTime.equals(reminder.reminderTime) : reminder.reminderTime == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + taskId;
        result = 31 * result + (reminderTime != null ? reminderTime.hashCode() : 0);
        return result;
    }
}

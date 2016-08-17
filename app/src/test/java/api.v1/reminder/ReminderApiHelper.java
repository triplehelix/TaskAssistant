package api.v1.reminder;

import api.v1.UnitTestHelper;
import api.v1.category.CategoryApiHelper;
import api.v1.model.Reminder;
import api.v1.model.Task;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 8/7/16.
 */
public class ReminderApiHelper extends UnitTestHelper{

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of JSONObjects.
     * @param backtickReminders
     * @return
     * @throws Exception
     */
    static ArrayList<JSONObject> toJSONObject(ArrayList<String> backtickReminders) throws Exception {
        ArrayList<JSONObject> myJSONObjects = new ArrayList<JSONObject>();
        for(String s: backtickReminders){
            String[] ReminderElementArray = s.split("`");
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id",           ReminderElementArray[0]);
            jsonObj.put("taskId",       ReminderElementArray[1]);
            jsonObj.put("reminderTime", ReminderElementArray[2]);
            myJSONObjects.add(jsonObj);
        }
        return myJSONObjects;
    }

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Tasks.
     * @param bactickTasks
     * @return  ArrayList<Task>
     * @throws Exception
     */
    protected static ArrayList<Task> toTasks(ArrayList<String> bactickTasks) throws Exception{
        ArrayList <Task> myTasks = new ArrayList<Task>();
        for(String s: bactickTasks) {
            String[] elements = s.split("`");
            Task task = new Task();
            task.setId(Integer.parseInt(elements[0]));
            task.setTaskListId(Integer.parseInt(elements[1]));
            task.setName(elements[2]);
            task.setImportant(CategoryApiHelper.parseJsonBooleanAsBoolean(elements[3]));
            task.setNote(elements[4]);
            task.setEstimatedTime(Long.parseLong(elements[5]));
            task.setInvestedTime(Long.parseLong(elements[6]));
            task.setUrgent(CategoryApiHelper.parseJsonBooleanAsBoolean(elements[7]));
            task.setDueDate(CategoryApiHelper.parseJsonDateAsDate(elements[8]));
            task.setStatus(elements[9]);
            if (elements.length > 10) {
                task.setReminderIds(toIntegerArrayList(elements[10]));
            }
            myTasks.add(task);
        }
        return myTasks;
    }

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Reminders.
     * @param backtickReminders
     * @return  ArrayList<Reminder>
     * @throws Exception
     */
    protected static ArrayList<Reminder> toReminders(ArrayList<String> backtickReminders) throws Exception {
        ArrayList<Reminder> myReminders=new ArrayList<Reminder>();
        for(String s: backtickReminders) {
            String[] reminderElementArray = s.split("`");
            Reminder reminder = new Reminder();
            reminder.setId(Integer.parseInt(reminderElementArray[0]));
            reminder.setTaskId(Integer.parseInt(reminderElementArray[1]));
            reminder.setReminderTime(parseJsonDateAsDate(reminderElementArray[2]));
            myReminders.add(reminder);
        }
        return myReminders;
    }
}

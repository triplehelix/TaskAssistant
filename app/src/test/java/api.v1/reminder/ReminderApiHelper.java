package api.v1.reminder;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import api.v1.model.Reminder;
import api.v1.model.Task;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kennethlyon on 8/7/16.
 */
public class ReminderApiHelper {

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Reminders.
     * @param backtickReminders
     * @return
     * @throws Exception
     */
    static ArrayList<Reminder> toReminders(ArrayList<String> backtickReminders) throws Exception {
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
     * @return
     * @throws Exception
     */
    static ArrayList<Task> toTasks(ArrayList<String> bactickTasks) throws Exception{
        ArrayList <Task> myTasks = new ArrayList<Task>();
        for(String s: bactickTasks) {
            String[] taskElementArray = s.split("`");
            Task task = new Task();
            task.setId(Integer.parseInt(taskElementArray[0]));
            task.setTaskListId(Integer.parseInt(taskElementArray[1]));
            task.setName(taskElementArray[2]);
            task.setImportant(ReminderApiHelper.parseJsonBooleanAsBoolean(taskElementArray[3]));
            task.setNote(taskElementArray[4]);
            task.setEstimatedTime(Long.parseLong(taskElementArray[5]));
            task.setInvestedTime(Long.parseLong(taskElementArray[6]));
            task.setUrgent(ReminderApiHelper.parseJsonBooleanAsBoolean(taskElementArray[7]));
            task.setDueDate(ReminderApiHelper.parseJsonDateAsDate(taskElementArray[8]));
            task.setStatus(taskElementArray[9]);
            myTasks.add(task);
        }
        return myTasks;
    }

    /**
     * Parse string as boolean.
     * @param b
     * @return
     */
    private static boolean parseJsonBooleanAsBoolean(String b) throws BusinessException {
        b = b.trim().toUpperCase();
        if (b.equals("TRUE"))
            return true;
        else if(b.equals("FALSE"))
            return false;
        else
            throw new BusinessException("Invalid boolean value: " + b, Error.valueOf("PARSE_BOOLEAN_ERROR"));
    }


    /**
     * Parse a String representing a given date and return a Date object.
     * String must be in the format: yyyy-MM-dd_HH:mm:ss
     *
     * @param stringDate
     * @return
     */
    private static Date parseJsonDateAsDate(String stringDate) throws BusinessException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        df.setLenient(false);
        Date result = null;
        try {
            result = df.parse(stringDate);
        } catch (java.text.ParseException e) {
            throw new BusinessException("Error caused by the String date: " + stringDate, Error.valueOf("PARSE_DATE_ERROR"));
        }
        return result;
    }

}

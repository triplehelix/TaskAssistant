package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class serves a a container for test case proto-Reminders.
 * Created by kennethlyon on 6/9/16.
 */
public class ReminderTest {
    private static Logger LOGGER = LoggerFactory.getLogger(ReminderTest.class);
    private static ArrayList<String> validReminders;
    private static ArrayList<String> errorReminders;
    private static ArrayList<String> modelOnlyErrorReminders;
    static {
        /* Add valid Reminders. Reminders fields are arranged in the order:
         *    id, taskId, reminderTime
         *    
         */
        validReminders=new ArrayList<String>();
        errorReminders=new ArrayList<String>();
        modelOnlyErrorReminders=new ArrayList<String>();

        validReminders.add("0`1`2020-05-28_08:31:01");
        validReminders.add("1`1`2020-05-31_00:00:00");
        validReminders.add("2`2`2016-06-09_18:30:00");
        validReminders.add("3`2`2016-06-12_08:00:00");
        validReminders.add("4`3`2016-06-09_19:00:00");
        validReminders.add("5`4`2020-05-31_00:00:00");

        modelOnlyErrorReminders.add("2`2`yyyy-MM-dd_HH:mm:ss");
        modelOnlyErrorReminders.add("3`2`2020-18-31_00:00:00");
        modelOnlyErrorReminders.add("4`-3`2016-06-09_19:00:00");
        modelOnlyErrorReminders.add("5`-40`2020-05-31_00:00:00");
        modelOnlyErrorReminders.add("0`1`0");
        modelOnlyErrorReminders.add("1`1` ");

        errorReminders.add("2`2`yyyy-MM-dd_HH:mm:ss");
        errorReminders.add("3`2`2020-18-31_00:00:00");
        errorReminders.add("4`-3`2016-06-09_19:00:00");
        errorReminders.add("5`40`2020-05-31_00:00:00");
        errorReminders.add("0`1`0");
        errorReminders.add("1`1` ");
    }

    public static ArrayList<JSONObject> getValidTestRemindersAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : validReminders)
            jsonObjectArrayList.add(ReminderTest.toJson(s));
        return jsonObjectArrayList;
    }

    public static ArrayList<JSONObject> getErrorTestRemindersAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : errorReminders)
            jsonObjectArrayList.add(ReminderTest.toJson(s));
        return jsonObjectArrayList;
    }

    public static ArrayList<Reminder> getValidTestRemindersAsReminders() throws Exception{
        ArrayList<Reminder> reminderArrayList = new ArrayList<Reminder>();
        for (String s : validReminders) {
            reminderArrayList.add(ReminderTest.toReminder(s));
        }
        return reminderArrayList;
    }

    private static Reminder toReminder(String s) throws Exception{
        String[] reminderElementArray = s.split("`");
        Reminder reminder = new Reminder();
        reminder.setId(Integer.parseInt(reminderElementArray[0]));
        reminder.setTaskId(Integer.parseInt(reminderElementArray[1]));
        reminder.setReminderTime(parseJsonDateAsDate(reminderElementArray[2]));
        return reminder;
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
            // LOGGER.error("Exception while parsing date token: " + stringDate, e);
            throw new BusinessException("Error caused by the String date: " + stringDate, Error.valueOf("PARSE_DATE_ERROR"));
        }
        return result;
    }


    /**
     * Parse string as boolean.
     * @param b
     * @return
     */
    private static boolean parseJsonBooleanAsBoolean(String b) throws BusinessException{
        b = b.trim().toUpperCase();
        if (b.equals("TRUE"))
            return true;
        else if(b.equals("FALSE"))
            return false;
        else
            throw new BusinessException("Invalid boolean value: " + b, Error.valueOf("PARSE_BOOLEAN_ERROR"));
    }

    private static JSONObject toJson(String stringReminder) {
        String[] ReminderElementArray = stringReminder.split("`");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",           ReminderElementArray[0]);
        jsonObj.put("taskId",       ReminderElementArray[1]);
        jsonObj.put("reminderTime", ReminderElementArray[2]);
        LOGGER.info("Created request {}", jsonObj.toJSONString());
        return jsonObj;
    }

    /**
     * @throws Exception
     */
    @Test
    public void setUp() throws Exception {
        for(String s: validReminders){
            ReminderTest.toReminder(s);
            LOGGER.info("Valid Reminder {}", toJson(s));
        }

        for(String s: modelOnlyErrorReminders){
            validateErrorReminder(s);
            LOGGER.info("Error Reminder {}", toJson(s));
        }
    }

    public void validateErrorReminder(String s){
        boolean error=false;
        try{
            ReminderTest.toReminder(s);
        }catch(Exception e){
            error=true;
            LOGGER.info("Invalid Reminder returned error. " + e.getMessage(), e);
        }
        if(!error){
            fail("Success returned for invalid Reminder: " + s);
        }
    }
}

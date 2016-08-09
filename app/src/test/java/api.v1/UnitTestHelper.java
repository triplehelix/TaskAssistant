package api.v1;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import api.v1.model.Category;
import api.v1.model.Reminder;
import api.v1.model.Task;
import api.v1.model.TaskList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.junit.Assert.fail;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kennethlyon on 7/11/16.
 */
public class UnitTestHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(UnitTestHelper.class);

    /**
     * Check to verify that valid object doPost responses are indeed
     * valid and log results.
     *
     * @param response
     */
    protected void validateDoPostValidResponse(MockHttpServletResponse response) {
        // Valid cases are: success or error if error then error
        String responseString;
        try{
            responseString = response.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error recorded while reading response", e);
            return;
        }
        LOGGER.info("response={}", responseString);
        JSONObject responseObj;
        try {
            responseObj = (JSONObject) new JSONParser().parse(responseString);
        } catch (ParseException e) {
            LOGGER.error("Parse Exception while parsing the response string", e);
            return;
        }
        if (null != responseObj){
            JSONObject error;
            if (null != (error=(JSONObject) responseObj.get("error"))){
                LOGGER.info("Response contained the error: code={}, msg={}", error.get("code"), error.get("msg"));
                fail("Received an error response on a valid input");
            }else{
                boolean success = (Boolean) responseObj.get("success");
                if (success){
                    LOGGER.info("Success value returned to the caller as: true ");
                }else{
                    fail("success value false in response and error value was not found");
                }
            }
        }else{
            fail("Response Object is empty");
        }
    }

    /**
     * Check to verify that invalid object requests are caught by
     * by UpdateObject().doPost(...) and are received as error messages.
     * @param response
     */
    protected void validateDoPostErrorResponse(MockHttpServletResponse response) {
        // Valid cases are: success or error if error then error
        String responseString;
        try{
            responseString = response.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error recorded while reading response", e);
            return;
        }
        LOGGER.info("response={}", responseString);
        JSONObject responseObj;
        try {
            responseObj = (JSONObject) new JSONParser().parse(responseString);
        } catch (ParseException e) {
            LOGGER.error("Parse Exception while parsing the response string", e);
            return;
        }
        if (null != responseObj){
            JSONObject error;
            if (null != (error=(JSONObject) responseObj.get("error"))){
                LOGGER.info("Response contained the error: code={}, msg={}", error.get("code"), error.get("msg"));
            }else{
                boolean success = (Boolean) responseObj.get("success");
                if (success){
                    LOGGER.info("Success value returned to the caller as: true ");
                    fail("Success value should not be present in case of invalid inputs.");
                }else{
                    fail("success value false in response and error value was not found");
                }
            }
        }else{
            fail("Response Object is empty");
        }
    }

    /**
     * Parse string as boolean.
     * @param b
     * @return
     */
    protected static boolean parseJsonBooleanAsBoolean(String b) throws BusinessException {
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
    protected static Date parseJsonDateAsDate(String stringDate) throws BusinessException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        df.setLenient(false);
        Date result = null;
        try {
            result = df.parse(stringDate);
        } catch (java.text.ParseException e) {
            LOGGER.error("Exception while parsing date token: " + stringDate, e);
            throw new BusinessException("Error caused by the String date: " + stringDate, Error.valueOf("PARSE_DATE_ERROR"));
        }
        return result;
    }
    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Tasks.
     * @param bactickTasks
     * @return
     * @throws Exception
     */
    protected static ArrayList<Task> toTasks(ArrayList<String> bactickTasks) throws Exception{
        ArrayList <Task> myTasks = new ArrayList<Task>();
        for(String s: bactickTasks) {
            String[] taskElementArray = s.split("`");
            Task task = new Task();
            task.setId(Integer.parseInt(taskElementArray[0]));
            task.setTaskListId(Integer.parseInt(taskElementArray[1]));
            task.setName(taskElementArray[2]);
            task.setImportant(UnitTestHelper.parseJsonBooleanAsBoolean(taskElementArray[3]));
            task.setNote(taskElementArray[4]);
            task.setEstimatedTime(Long.parseLong(taskElementArray[5]));
            task.setInvestedTime(Long.parseLong(taskElementArray[6]));
            task.setUrgent(UnitTestHelper.parseJsonBooleanAsBoolean(taskElementArray[7]));
            task.setDueDate(UnitTestHelper.parseJsonDateAsDate(taskElementArray[8]));
            task.setStatus(taskElementArray[9]);
            myTasks.add(task);
        }
        return myTasks;
    }
    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of TaskLists.
     * @param backtickTaskLists
     * @return
     * @throws Exception
     */
    protected static ArrayList<TaskList> toTaskLists(ArrayList<String> backtickTaskLists)throws Exception{
        ArrayList<TaskList> myTaskLists=new ArrayList<TaskList>();
        for(String s: backtickTaskLists) {
            String[] taskListElementArray = s.split("`");
            TaskList taskList = new TaskList();
            taskList.setId(Integer.parseInt(taskListElementArray[0]));
            taskList.setName(taskListElementArray[1]);
            taskList.setDescription(taskListElementArray[2]);
            myTaskLists.add(taskList);
        }
        return myTaskLists;
    }
    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Reminders.
     * @param backtickReminders
     * @return
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
    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Categories.
     * @param backtickCategories
     * @return
     * @throws Exception
     */
    protected static ArrayList<Category> toCategories(ArrayList<String> backtickCategories) throws Exception{
        ArrayList<Category> myCategories = new ArrayList<Category>();
        for(String s:backtickCategories){
            String[] CategoryElementArray = s.split("`");
            Category category = new Category();
            category.setId(Integer.parseInt(CategoryElementArray[0]));
            category.setName(CategoryElementArray[1]);
            category.setDescription(CategoryElementArray[2]);
            myCategories.add(category);
        }
        return myCategories;
    }
}

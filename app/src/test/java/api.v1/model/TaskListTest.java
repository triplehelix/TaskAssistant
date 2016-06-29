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
 * This class serves a a container for test case proto-TaskLists.
 * Created by kennethlyon on 6/9/16.
 */
public class TaskListTest {
    private static Logger LOGGER = LoggerFactory.getLogger(TaskListTest.class);
    private static ArrayList<String> validTaskLists;
    private static ArrayList<String> errorTaskLists;

    static {

        /* Add valid TaskLists.
         *
         */
        validTaskLists = new ArrayList<String>();
        errorTaskLists = new ArrayList<String>();

        validTaskLists.add("0");
        validTaskLists.add("1");
        validTaskLists.add("2");
        validTaskLists.add("3");
        validTaskLists.add("4");
			     
        errorTaskLists.add("0");
        errorTaskLists.add("1");
        errorTaskLists.add("2");
        errorTaskLists.add("3");
        errorTaskLists.add("4");
    }



    public static ArrayList<JSONObject> getValidTestTaskListsAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();

        for (String s : validTaskLists)
            jsonObjectArrayList.add(TaskListTest.toJson(s));
        return jsonObjectArrayList;
    }

    public static ArrayList<JSONObject> getErrorTestTaskListsAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : errorTaskLists)
            jsonObjectArrayList.add(TaskListTest.toJson(s));
        return jsonObjectArrayList;

    }
    public static ArrayList<TaskList> getValidTestTaskListsAsTaskLists() throws Exception{
        ArrayList<TaskList> TaskListArrayList = new ArrayList<TaskList>();
        for (String s : validTaskLists) {
            TaskListArrayList.add(TaskListTest.toTaskList(s));
        }
        return TaskListArrayList;
    }

    private static TaskList toTaskList(String s) throws Exception{
        String[] taskListElementArray = s.split("`");
        TaskList taskList = new TaskList();
        taskList.setId(Integer.parseInt(taskListElementArray[0]));
        return taskList;
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
            LOGGER.error("Exception while parsing date token: " + stringDate);
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

    private static JSONObject toJson(String stringTaskList) {
        String[] taskListElementArray = stringTaskList.split("`");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",         taskListElementArray[0]);
        jsonObj.put("startDate",  taskListElementArray[1]);
        jsonObj.put("endDate",    taskListElementArray[2]);
        jsonObj.put("repeatType", taskListElementArray[3]);
        LOGGER.info("Created request {}", jsonObj.toJSONString());
        return jsonObj;
    }

    /**
     * @throws Exception
     */
    @Test
    public void setUp() throws Exception {
        for(String s: validTaskLists){
            TaskListTest.toTaskList(s);
            LOGGER.info("Valid TaskList {}", toJson(s));
        }

        for(String s: errorTaskLists){
            validateErrorTaskList(s);
            LOGGER.info("Error TaskList {}", toJson(s));
        }
    }

    public void validateErrorTaskList(String s){
        boolean error=false;
        try{
            TaskListTest.toTaskList(s);
        }catch(Exception e){
            error=true;
            LOGGER.info("Invalid TaskList returned error. " + e.getMessage(), e);
        }
        if(!error){
            fail("Success returned for invalid TaskList: " + s);
        }
    }
}

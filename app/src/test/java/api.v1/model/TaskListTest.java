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
         * To add valid tasks, create an ArrayList of valid task objects. Use 
         * getValidTestTasksAsTasks to set the ArrayListOfTasks
         */
        validTaskLists = new ArrayList<String>();
        errorTaskLists = new ArrayList<String>();

        validTaskLists.add("0");
        validTaskLists.add("1");

        errorTaskLists.add("0");
        errorTaskLists.add("1");
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


    private static JSONObject toJson(String stringTaskList) {
        String[] taskListElementArray = stringTaskList.split("`");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",         taskListElementArray[0]);
        //TODO how does one populate Json with nested objects?
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

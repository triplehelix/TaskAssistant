package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.repo.TaskRepository;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;
import java.util.ArrayList;


/**
 * Here we create TaskLists and test their functionality.
 *
 * Created by kennethlyon on 6/9/16.
 */
public class TaskListTest {
    private static Logger LOGGER = LoggerFactory.getLogger(TaskListTest.class);
    private static ArrayList<String> validTaskLists;
    private static ArrayList<String> errorTaskLists;

    static {
        /* Add valid TaskLists.
         */
        validTaskLists=new ArrayList<String>();
        //TODO create some valid TaskLists.
        validTaskLists.add("0`TaskList 0 created from ValidTasks`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksAsTasks().");
        validTaskLists.add("1`TaskList 1 created from ValidTaskUpdates`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksUpdatesAsTasks().");

        errorTaskLists=new ArrayList<String>();
        errorTaskLists.add("0``This TaskList has no name.");
        errorTaskLists.add("abc`TaskList 3 created from ErrorTaskUpdates`This is an invalid TaskList composed of Task ids from: TaskTest.getValidTestTasksUpdatesAsTasks().");
        errorTaskLists.add("-9`Invalid Id TaskList`This is an invalid TaskList because it has an invalid id.");
    }

    public static ArrayList<JSONObject> getValidTestTaskListsAsJson() {
        return null;
    }

    public static ArrayList<JSONObject> getErrorTestTaskListsAsJson() {
        return null;
    }

    public static ArrayList<TaskList> getValidTestTaskListsAsTaskLists() throws Exception{
        ArrayList<TaskList> taskListArrayList = new ArrayList<TaskList>(2);

        // First create two new task lists...
        TaskList tl0=new TaskList();
        TaskList tl1=new TaskList();
        // Set their name, id and description fields:

        // Then add them to the ArrayList
        taskListArrayList.add(tl0);
        taskListArrayList.add(tl1);

        return taskListArrayList;
    }

    /**
     * Use the backtick delimited Strings in errorTaskLists, or
     * validTaskLists to create a TaskList.
     * @param s
     * @return taskList
     * @throws BusinessException
     */
    private static TaskList toTaskList(String s) throws BusinessException{
        String[] taskListElementArray = s.split("`");
        TaskList taskList = new TaskList();
        taskList.setId(Integer.parseInt(taskListElementArray[0]));
        taskList.setName(taskListElementArray[1]);
        taskList.setDescription(taskListElementArray[2]);
        return taskList;
    }


    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        LOGGER.info("Creating valid TaskLists...");
        for(String s: validTaskLists)
            validateValidTaskList(s);

        LOGGER.info("Creating error TaskLists...");
        for(String s: errorTaskLists)
            validateErrorTaskList(s);
        LOGGER.info("Success!");
    }

    /**
     * This method is used to verify that an error causing TaskList results
     * in an error. Errors should result from invalid IDs, or null names.
     * @param s
     */
    public void validateErrorTaskList(String s){
        boolean error=false;
        TaskList taskList;
        try{
            taskList=TaskListTest.toTaskList(s);
        }catch(Exception e){
            error=true;
            LOGGER.info("Invalid TaskList returned error. " + e.getMessage(), e);
        }
        if(!error){
            fail("Success returned for invalid TaskList: " + s);
        }
    }

    /**
     * Ensure that the provided String can be translated into a
     * valid TaskList.
     * @param s
     */
    public void validateValidTaskList(String s){
        TaskList taskList;
        try{
            taskList=TaskListTest.toTaskList(s);
            LOGGER.info("Valid TaskList returned {}", taskList.toJson());
        }catch (Exception e){
            LOGGER.error("Invalid TaskList returned error. " + e.getMessage(), e);
            fail("Error returned for valid TaskList. {} " + s);
        }

    }
}

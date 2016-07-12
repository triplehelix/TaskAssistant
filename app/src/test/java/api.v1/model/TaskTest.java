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
 * This class serves a a container for test case proto-tasks.
 * Created by kennethlyon on 6/9/16.
 */
public class TaskTest {
    private static Logger LOGGER = LoggerFactory.getLogger(TaskTest.class);
    private static ArrayList<String> validTasks;
    private static ArrayList<String> errorTasks;
    private static ArrayList<String> validUpdates;
    private static ArrayList<String> errorUpdates;

    static {
        /* Add valid tasks. Tasks fields are arranged in the order:
         * validTasks.add("int id` String name` boolean important` String note` long estimatedTime` long investedTime` boolean urgent` Date dueDate` State status");
         */
        validTasks = new ArrayList<String>();
        validTasks.add("0`0`Feed dog`TRUE`Dog eats kibble.`60000`0`TRUE`2020-05-28_08:31:01`NEW");
        validTasks.add("1`0`Create AddTask unit test`TRUE`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS");
        validTasks.add("2`0`Buy beer`TRUE`Pick up some IPAs on the way home from work. Edit: Bill said he would pick up beers instead.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");
        validTasks.add("3`0`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`3600000`0`FALSE`2016-06-12_08:00:00`DEFERRED");
        validTasks.add("4`0`Shave`FALSE`GF said I need to shave.`180000`0`TRUE`2016-06-09_19:00:00`DONE");
        validTasks.add("5`0`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`TRUE`2016-06-09_19:00:00`NEW");
        validTasks.add("6`0`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW");
        validTasks.add("7`0`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED");
        validTasks.add("8`0`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS");

        // Add error causing tasks.
        errorTasks = new ArrayList<String>();
        errorTasks.add("0`1`Call Attorney J.P. Coleostomy`TRUE`Bring photographic proof!`3600000`0`YES`2016-06-14_15:15:00`NEW");
        errorTasks.add("1`1`Fix mom's computer.`TRUE`Again!?!`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorTasks.add("2`1`Prepare for apocalyptic zombie-cat-hoard.`TRUE`Need cat nip and shotguns.`4200000`0`TRUE`yyyy-MM-dd_HH:mm:ss`NEW");
        errorTasks.add("3`1`Vaccinate cat against zombie cat syndrome.`TRUE`Don't forget that Mr Bigglesworth doesn't like shots.`1 hour`0`TRUE`2020-08-31_00:00:00`NEW");
        errorTasks.add("4`1`Change motor oil`BLUE`Use quicky-lube coupon.`1600000`0`FALSE`2020-05-31_22:00:00`NEW");
        errorTasks.add("5`1`merge git conflicts`TRUE`I really need to learn how to use git.`180000`0`TRUE`2020-05-31_03:00:00`incomplete");
        errorTasks.add("6`1`Refinish porch`FALSE``210000`0`TRUE`2020-09-31_00:00:00`NEW");
        errorTasks.add("7`1``TRUE`THIS TASK HAS NO NAME`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorTasks.add("8`-9`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS");



        // Add valid mutations to valid tasks.         
        validUpdates = new ArrayList<String>();
        validUpdates.add("0`2`Feed dog`TRUE`Give food to the fluff.`60000`0`TRUE`2020-05-28_08:31:01`NEW");
        validUpdates.add("1`2`Create AddTask unit test`false`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS");
        validUpdates.add("2`2`Buy beer`TRUE`Bill is getting IPAs for the party.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");
        validUpdates.add("3`2`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`1800000`0`FALSE`2016-06-12_08:00:00`DEFERRED");
        validUpdates.add("4`2`Shave`FALSE`GF said I need to shave.`180000`90000`TRUE`2016-06-09_19:00:00`DONE");
        validUpdates.add("5`2`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`false`2016-06-09_19:00:00`NEW");
        validUpdates.add("6`2`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW");
        validUpdates.add("7`2`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED");
        validUpdates.add("8`2`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`DONE");

        // Add invalid mutations to valid tasks.
        errorUpdates = new ArrayList<String>();
        errorUpdates.add("0`3`Call Attorney J.P. Coleostomy`TRUE`Bring photographic proof!`3600000`0`YES`2016-06-14_15:15:00`NEW");
        errorUpdates.add("1`3`Fix mom's computer.`TRUE`Again!?!`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorUpdates.add("2`3`Prepare for apocalyptic zombie-cat-hoard.`TRUE`Need cat nip and shotguns.`4200000`0`TRUE`yyyy-MM-dd_HH:mm:ss`NEW");
        errorUpdates.add("3`3`Vaccinate cat against zombie cat syndrome.`TRUE`Don't forget that Mr Bigglesworth doesn't like shots.`1 hour`0`TRUE`2020-08-31_00:00:00`NEW");
        errorUpdates.add("4`3`Change motor oil`BLUE`Use quicky-lube coupon.`1600000`0`FALSE`2020-05-31_22:00:00`NEW");
        errorUpdates.add("5`3`merge git conflicts`TRUE`I really need to learn how to use git.`180000`0`TRUE`2020-05-31_03:00:00`incomplete");
        errorUpdates.add("6`3`Refinish porch`FALSE``210000`0`TRUE`2020-09-31_00:00:00`NEW");
        errorUpdates.add("7`3``TRUE`THIS TASK HAS NO NAME`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorUpdates.add("8`3`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`abcdefg`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS");
    }

    public static ArrayList<JSONObject> getValidTestTasksAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();

        for (String s : validTasks)
            jsonObjectArrayList.add(TaskTest.toJson(s));
        return jsonObjectArrayList;
    }

    public static ArrayList<JSONObject> getErrorTestTasksAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : errorTasks)
            jsonObjectArrayList.add(TaskTest.toJson(s));
        return jsonObjectArrayList;

    }

    public static ArrayList<JSONObject> getValidTestTaskUpdatesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : validUpdates)
            jsonObjectArrayList.add(TaskTest.toJson(s));
        return jsonObjectArrayList;
    }

    public static ArrayList<JSONObject> getErrorTestTaskUpdatesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : errorUpdates)
            jsonObjectArrayList.add(TaskTest.toJson(s));
        return jsonObjectArrayList;
    }

    public static ArrayList<Task> getValidTestTasksAsTasks() throws Exception{
        ArrayList<Task> taskArrayList = new ArrayList<Task>();
        for (String s : validTasks) {
            taskArrayList.add(TaskTest.toTask(s));
        }
        return taskArrayList;
    }

    public static ArrayList<Task> getValidTestTasksUpdatesAsTasks() throws Exception{
        ArrayList<Task> taskArrayList = new ArrayList<Task>();
        for (String s : validUpdates) {
            taskArrayList.add(TaskTest.toTask(s));
        }
        return taskArrayList;
    }


    private static Task toTask(String s) throws Exception{
        String[] taskElementArray = s.split("`");
        Task task = new Task();
        task.setId(Integer.parseInt(taskElementArray[0]));
        task.setTaskListId(Integer.parseInt(taskElementArray[1]));
        task.setName(taskElementArray[2]);
        task.setImportant(TaskTest.parseJsonBooleanAsBoolean(taskElementArray[3]));
        task.setNote(taskElementArray[4]);
        task.setEstimatedTime(Long.parseLong(taskElementArray[5]));
        task.setInvestedTime(Long.parseLong(taskElementArray[6]));
        task.setUrgent(TaskTest.parseJsonBooleanAsBoolean(taskElementArray[7]));
        task.setDueDate(parseJsonDateAsDate(taskElementArray[8]));
        task.setStatus(taskElementArray[9]);
        return task;
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
            LOGGER.error("Exception while parsing date token: " + stringDate, e);
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


    private static JSONObject toJson(String stringTask) {
        String[] taskElementArray = stringTask.split("`");
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("id", taskElementArray[0]);
        jsonObj.put("taskListId", taskElementArray[1]);
        jsonObj.put("name", taskElementArray[2]);
        jsonObj.put("important", taskElementArray[3]);
        jsonObj.put("note", taskElementArray[4]);
        jsonObj.put("estimatedTime", taskElementArray[5]);
        jsonObj.put("investedTime", taskElementArray[6]);
        jsonObj.put("urgent", taskElementArray[7]);
        jsonObj.put("dueDate", taskElementArray[8]);
        jsonObj.put("status", taskElementArray[9]);
        LOGGER.info("Created request {}", jsonObj.toJSONString());
        return jsonObj;
    }

    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        for(String s: validTasks){
            TaskTest.toTask(s);
            LOGGER.info("Valid task {}", toJson(s));
        }

        for(String s: validUpdates){
            TaskTest.toTask(s);
            LOGGER.info("Valid task {}", toJson(s));
        }

        for(String s: errorTasks){
            validateErrorTask(s);
            LOGGER.info("Error task {}", toJson(s));
        }

        for(String s: errorUpdates){
            validateErrorTask(s);
            LOGGER.info("Error task {}", toJson(s));
        }

    }

    public void validateErrorTask(String s){
        boolean error=false;
        try{
            TaskTest.toTask(s);
        }catch(Exception e){
            error=true;
            LOGGER.info("Invalid Task returned error. " + e.getMessage(), e);
        }
        if(!error){
            fail("Success returned for invalid Task: " + s);
        }
    }
}
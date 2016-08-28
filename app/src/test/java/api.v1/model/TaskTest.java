package api.v1.model;

import api.v1.UnitTestHelper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;

import java.util.ArrayList;

/**
 * This class serves a a container for test case proto-tasks.
 * Created by kennethlyon on 6/9/16.
 */
public class TaskTest extends UnitTestHelper{
    private static Logger LOGGER = LoggerFactory.getLogger(TaskTest.class);
    private static ArrayList<String> validTasks=new ArrayList<String>();
    private static ArrayList<String> validUpdates=new ArrayList<String>();

    static {
        /* Add valid tasks. Tasks fields are arranged in the order:
         * validTasks.add("int id` String name` boolean important` String note` long estimatedTime` long investedTime` boolean urgent` Date dueDate` State status");
         */
        validTasks.add( "0`0`Task Name 0`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add( "1`0`Task Name 1`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add( "2`0`Task Name 2`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add( "3`0`Task Name 3`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add( "4`1`Task Name 4`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add( "5`1`Task Name 5`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add( "6`1`Task Name 6`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add( "7`1`Task Name 7`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add( "8`2`Task Name 8`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add( "9`2`Task Name 9`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add("10`2`Task Name A`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add("11`2`Task Name B`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add("12`2`Task Name C`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add("13`2`Task Name D`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add("14`2`Task Name E`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validTasks.add("15`2`Task Name F`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");

        // Add valid mutations to valid tasks.
        validUpdates.add( "0`0`Task Name 0`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`DONE`[]`[]`[]");
        validUpdates.add( "1`0`Task Name 1`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`IN_PROGRESS`[]`[]`[]");
        validUpdates.add( "2`0`Task Name 2`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`DELEGATED`[]`[]`[]");
        validUpdates.add( "3`0`Task Name 3`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`DEFERRED`[]`[]`[]");
        validUpdates.add( "4`1`Task Name 4`TRUE`This is the Task's note.`60000`0`TRUE`2000-05-28_08:01:01`NEW`[]`[]`[]");
        validUpdates.add( "5`1`Task Name 5`TRUE`This is the Task's note.`60000`0`TRUE`2099-05-28_08:31:01`NEW`[]`[]`[]");
        validUpdates.add( "6`1`Task Name 6`TRUE`This is the Task's note.`60000`0`FALSE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validUpdates.add( "7`1`Task Name 7`TRUE`This is the Task's note.`60000`1`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validUpdates.add( "8`2`Task Name 8`TRUE`This is the Task's note.`60001`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validUpdates.add( "9`2`Task Name 9`TRUE`This is the Task's note!`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validUpdates.add("10`2`Task Name10`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validUpdates.add("11`3`Task Name B`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validUpdates.add("-1`2`Task Name C`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[]");
        validUpdates.add("13`2`Task Name D`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[1]`[]`[]");
        validUpdates.add("14`2`Task Name E`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[2]`[]");
        validUpdates.add("15`2`Task Name F`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28_08:31:01`NEW`[]`[]`[3]");

    }

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Tasks.
     * @param bactickTasks
     * @return  ArrayList<Task>
     * @throws Exception
     */
    protected static ArrayList<Task> toTasks(ArrayList<String> bactickTasks) throws Exception {
        ArrayList<Task> myTasks = new ArrayList<Task>();
        for (String s : bactickTasks) {
            String[] elements = s.split("`");
            Task task = new Task();
            task.setId(Integer.parseInt(elements[0]));
            task.setTaskListId(Integer.parseInt(elements[1]));
            task.setName(elements[2]);
            task.setImportant(UnitTestHelper.parseJsonBooleanAsBoolean(elements[3]));
            task.setNote(elements[4]);
            task.setEstimatedTime(Long.parseLong(elements[5]));
            task.setInvestedTime(Long.parseLong(elements[6]));
            task.setUrgent(UnitTestHelper.parseJsonBooleanAsBoolean(elements[7]));
            task.setDueDate(UnitTestHelper.parseJsonDateAsDate(elements[8]));
            task.setStatus(elements[9]);
            task.setCategoryIds(toIntegerArrayList(elements[10]));
            task.setReminderIds(toIntegerArrayList(elements[11]));
            task.setScheduleIds(toIntegerArrayList(elements[12]));
            myTasks.add(task);
        }
        return myTasks;
    }
    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {        
    // Verify that clones generated from "validTasks" are identical to themselves:
        ArrayList<Task> myValidTasks1=toTasks(validTasks);
        ArrayList<Task> myValidTasks2=toTasks(validTasks);
        ArrayList<Task> myValidUpdates=toTasks(validUpdates);
        LOGGER.info("Verifying object equivalence.");
        for(int i=0; i<myValidTasks1.size(); i++) {
            LOGGER.info("Evaluating {} {}",
                    myValidTasks1.get(i),
                    myValidTasks2.get(i));
            if (!myValidTasks1.get(i).equals(myValidTasks2.get(i))) {
                LOGGER.error("These objects were evaluated as not equal when they should be: {} {}",
                        myValidTasks1.get(i).toJson(),
                        myValidTasks2.get(i).toJson());
                fail("Error! These objects should be equal!");
            }
        }

    // Verify that instances made from "validTasks" and validUpdates are not equal to eachother.
        LOGGER.info("Verifying object non-equivalence.");
        for(int i=0; i<myValidTasks1.size(); i++){
            LOGGER.info("Evaluating {} {}",
                    myValidTasks1.get(i),
                    myValidUpdates.get(i));
            if(myValidTasks1.get(i).equals(myValidUpdates.get(i))){
                LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                        myValidTasks1.get(i).toJson(),
                        myValidUpdates.get(i).toJson());
                fail("Error! These objects should not be equal!");
            }
        }
    }
}

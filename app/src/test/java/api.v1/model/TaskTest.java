package api.v1.model;

import api.v1.UnitTestHelper;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
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
        validTasks.add( "0`0`Task Name 0`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add( "1`0`Task Name 1`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add( "2`0`Task Name 2`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add( "3`0`Task Name 3`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add( "4`1`Task Name 4`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add( "5`1`Task Name 5`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add( "6`1`Task Name 6`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add( "7`1`Task Name 7`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add( "8`2`Task Name 8`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add( "9`2`Task Name 9`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add("10`2`Task Name A`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add("11`2`Task Name B`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add("12`2`Task Name C`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add("13`2`Task Name D`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add("14`2`Task Name E`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validTasks.add("15`2`Task Name F`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");

        // Add valid mutations to valid tasks.
        validUpdates.add( "0`0`Task Name 0`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`DONE`[]`[]`[]");
        validUpdates.add( "1`0`Task Name 1`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`IN_PROGRESS`[]`[]`[]");
        validUpdates.add( "2`0`Task Name 2`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`DELEGATED`[]`[]`[]");
        validUpdates.add( "3`0`Task Name 3`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`DEFERRED`[]`[]`[]");
        validUpdates.add( "4`1`Task Name 4`TRUE`This is the Task's note.`60000`0`TRUE`2000-05-28T08:01:01.123Z`NEW`[]`[]`[]");
        validUpdates.add( "5`1`Task Name 5`TRUE`This is the Task's note.`60000`0`TRUE`2099-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validUpdates.add( "6`1`Task Name 6`TRUE`This is the Task's note.`60000`0`FALSE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validUpdates.add( "7`1`Task Name 7`TRUE`This is the Task's note.`60000`1`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validUpdates.add( "8`2`Task Name 8`TRUE`This is the Task's note.`60001`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validUpdates.add( "9`2`Task Name 9`TRUE`This is the Task's note!`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validUpdates.add("10`2`Task Name10`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validUpdates.add("11`3`Task Name B`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validUpdates.add("-1`2`Task Name C`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[]");
        validUpdates.add("13`2`Task Name D`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[1]`[]`[]");
        validUpdates.add("14`2`Task Name E`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[2]`[]");
        validUpdates.add("15`2`Task Name F`TRUE`This is the Task's note.`60000`0`TRUE`2020-05-28T08:31:01.123Z`NEW`[]`[]`[3]");

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
            task.setStatus(Task.Status.valueOf(elements[9].trim()));
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
        ArrayList<Task> myTasks=toTasks(validTasks);
        ArrayList<Task> myUpdates=toTasks(validUpdates);
        LOGGER.info("Verifying object equivalence.");
        Task tempTask=null;
        for(int i=0; i<myTasks.size(); i++){
            tempTask=new Task(myTasks.get(i));
            LOGGER.info("Evaluating {} {}",
                    myTasks.get(i),
                    tempTask);
            if(!myTasks.get(i).equals(tempTask)){
                LOGGER.error("These objects were evaluated as not equal when they should be: {} {}",
                        myTasks.get(i).toJson(),
                        tempTask.toJson());
                fail("Error! These objects should be equal!");
            }
        }


    // Verify that instances made from "validTasks" and validUpdates are not equal to eachother.
        LOGGER.info("Verifying object non-equivalence.");
        for(int i=0; i<myTasks.size(); i++){
            LOGGER.info("Evaluating {} {}",
                    myTasks.get(i),
                    myUpdates.get(i));
            if(myTasks.get(i).equals(myUpdates.get(i))){
                LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                        myTasks.get(i).toJson(),
                        myUpdates.get(i).toJson());
                fail("Error! These objects should not be equal!");
            }
        }


        // Verify Gson serialization works properly:
        LOGGER.info("Verifying Gson serialization works properly.");
        String format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Gson gson = new GsonBuilder().setDateFormat(format).create();
        String json="";
        for(int i=0; i<myTasks.size(); i++){
            json=myTasks.get(i).toJson();
            LOGGER.info("Evaluating {} {}", json, (gson.fromJson(json, Task.class)).toJson() );
            if(!myTasks.get(i).equals(gson.fromJson(json, Task.class))){
                LOGGER.info("Error attempting to serialize/deserialize the task {} {}", json, (gson.fromJson(json, Task.class)).toJson() );
            }
        }
        testAddCategoriesRemindersAndSchedules();
    }

    /**
     * Validate the addSchedule, addCalendar, addCategory & addReminder.
     */
    private void testAddCategoriesRemindersAndSchedules() throws Exception{
     
        Category category=new Category();
        Reminder reminder=new Reminder();
        Schedule schedule=new Schedule();

        ArrayList<Task> myTasks=toTasks(validUpdates);
        Task taskCategory, taskSchedule, taskReminder;
        taskCategory=new Task(myTasks.get(13));
        taskReminder=new Task(myTasks.get(14));
        taskSchedule=new Task(myTasks.get(15));

        category.setId(1);
        reminder.setId(2);
        schedule.setId(3);

        taskCategory.addCategory(category);
        taskReminder.addReminder(reminder);
        taskSchedule.addSchedule(schedule);


        if(!myTasks.get(13).equals(taskCategory)){
            LOGGER.error("These objects were evaluated to be not equal when they should be: {} {}",
                    myTasks.get(13).toJson(),
                    taskCategory.toJson());
            fail("Error! These objects should be equal!");
        }
        if(!myTasks.get(14).equals(taskReminder)){
            LOGGER.error("These objects were evaluated to be not equal when they should be: {} {}",
                    myTasks.get(14).toJson(),
                    taskReminder.toJson());
            fail("Error! These objects should be equal!");
        }
        if(!myTasks.get(15).equals(taskSchedule)){
            LOGGER.error("These objects were evaluated to be not equal when they should be: {} {}",
                    myTasks.get(15).toJson(),
                    taskSchedule.toJson());
            fail("Error! These objects should be equal!");
        }

        category.setId(31);
        reminder.setId(31);
        schedule.setId(31);

        taskCategory.addCategory(category);
        taskReminder.addReminder(reminder);
        taskSchedule.addSchedule(schedule);

        if(myTasks.get(13).equals(taskCategory)){
            LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                    myTasks.get(13).toJson(),
                    taskCategory.toJson());
            fail("Error! These objects should be not equal!");
        }
        if(myTasks.get(14).equals(taskReminder)){
            LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                    myTasks.get(14).toJson(),
                    taskReminder.toJson());
            fail("Error! These objects should be not equal!");
        }
        if(myTasks.get(15).equals(taskSchedule)){
            LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                    myTasks.get(15).toJson(),
                    taskSchedule.toJson());
            fail("Error! These objects should be not equal!");
        }

    }
}



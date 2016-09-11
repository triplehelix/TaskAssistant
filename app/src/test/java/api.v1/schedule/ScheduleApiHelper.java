package api.v1.schedule;

import api.v1.UnitTestHelper;
import api.v1.model.*;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 8/7/16.
 */
public class ScheduleApiHelper extends UnitTestHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(ScheduleApiHelper.class);

   /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of JSONObjects.
     * @param backtickCategories
     * @return
     * @throws Exception
     */
    static ArrayList<JSONObject> toJSONObject(ArrayList<String> backtickCategories) throws Exception{
        ArrayList<JSONObject> myJSONObjects = new ArrayList<JSONObject>();
        for(String s: backtickCategories) {
            String[] elements = s.split("`");
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", elements[0]);
            jsonObj.put("userId", elements[1]);
            jsonObj.put("startDate", elements[2]);
            jsonObj.put("endDate", elements[3]);
            jsonObj.put("repeatType", elements[4]);
            if(elements.length>5) {
                jsonObj.put("taskIds", elements[5]);
                jsonObj.put("categoryIds", elements[6]);
            }
            myJSONObjects.add(jsonObj);
        }
        return myJSONObjects;
    }
    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of TaskLists.
     * @param backtickTaskLists
     * @return  ArrayList<TaskList>
     * @throws Exception
     */
    protected static ArrayList<TaskList> toTaskLists(ArrayList<String> backtickTaskLists)throws Exception{
        ArrayList<TaskList> myTaskLists=new ArrayList<TaskList>();
        for(String s: backtickTaskLists) {
            String[] taskListElementArray = s.split("`");
            TaskList taskList = new TaskList();
            taskList.setId(Integer.parseInt(taskListElementArray[0]));
            taskList.setUserId(Integer.parseInt(taskListElementArray[1]));
            taskList.setName(taskListElementArray[2]);
            taskList.setDescription(taskListElementArray[3]);
            myTaskLists.add(taskList);
        }
        return myTaskLists;
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
            task.setImportant(UnitTestHelper.parseJsonBooleanAsBoolean(elements[3]));
            task.setNote(elements[4]);
            task.setEstimatedTime(Long.parseLong(elements[5]));
            task.setInvestedTime(Long.parseLong(elements[6]));
            task.setUrgent(UnitTestHelper.parseJsonBooleanAsBoolean(elements[7]));
            task.setDueDate(UnitTestHelper.parseJsonDateAsDate(elements[8]));
            task.setStatus(elements[9]);
            if (elements.length > 10) {
                task.setScheduleIds(toIntegerArrayList(elements[10]));
            }

            myTasks.add(task);
        }
        return myTasks;
    }
    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Categories.
     * @param backtickCategories
     * @return ArrayList<Category>
     * @throws Exception
     */
    protected static ArrayList<Category> toCategories(ArrayList<String> backtickCategories) throws Exception{
        ArrayList<Category> myCategories = new ArrayList<Category>();
        for(String s:backtickCategories){
            String[] elements = s.split("`");
            Category category = new Category();
            category.setId(Integer.parseInt(elements[0]));
            category.setUserId(Integer.parseInt(elements[1]));
            category.setName(elements[2]);
            category.setDescription(elements[3]);
            if(elements.length>4)
                category.setScheduleIds(toIntegerArrayList(elements[4]));
            myCategories.add(category);
        }
        return myCategories;
    }
    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Users.
     *
     * @param backtickUsers
     * @return ArrayList<User>
     * @throws Exception
     */
    protected static ArrayList<User> toUsers(ArrayList<String> backtickUsers) throws Exception{
        ArrayList<User> myUsers = new ArrayList<User>();
        for(String s: backtickUsers) {
            String[] elements = s.split("`");
            User user = new User();
            user.setId(Integer.parseInt(elements[0].trim()));
            user.setEmail(elements[1].trim());
            user.setPassword(elements[2].trim());
            if (elements.length > 3) {
                user.setScheduleIds(toIntegerArrayList(elements[3]));
            }
            myUsers.add(user);
        }
        return myUsers;
    }

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Categories.
     * @param backtickCategories
     * @return ArrayList<Schedule>
     * @throws Exception
     */
    protected static ArrayList<Schedule> toSchedules(ArrayList<String> backtickCategories) throws Exception{
        ArrayList<Schedule> mySchedules = new ArrayList<Schedule>();
        for(String s:backtickCategories){
            String[] elements = s.split("`");
            Schedule schedule = new Schedule();
            schedule.setId(Integer.parseInt(elements[0]));
            schedule.setUserId(Integer.parseInt(elements[1]));
            schedule.setStartDate(parseJsonDateAsDate(elements[2]));
            schedule.setEndDate(parseJsonDateAsDate(elements[3]));
            schedule.setRepeatType(elements[4].trim());
            if(elements.length>5) {
                schedule.setTaskIds(toIntegerArrayList(elements[5]));
                schedule.setCategoryIds(toIntegerArrayList(elements[6]));
            }
            mySchedules.add(schedule);
        }
        return mySchedules;
    }
 }



 
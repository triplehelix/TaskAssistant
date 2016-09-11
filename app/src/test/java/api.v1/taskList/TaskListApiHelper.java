package api.v1.taskList;

import api.v1.UnitTestHelper;
import api.v1.model.Category;
import api.v1.model.Schedule;
import api.v1.model.Task;
import api.v1.model.TaskList;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 8/7/16.
 */
public class TaskListApiHelper extends UnitTestHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(TaskListApiHelper.class);

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of JSONObjects.
     * @param backtickTaskLists
     * @return
     * @throws Exception
     */
    static ArrayList<JSONObject> toJSONObjects(ArrayList<String> backtickTaskLists)throws Exception {
        ArrayList<JSONObject> myJSONObjects = new ArrayList<JSONObject>();
        for (String s : backtickTaskLists) {
            String[] taskElementArray = s.split("`");
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", taskElementArray[0]);
            jsonObj.put("userId", taskElementArray[1]);
            jsonObj.put("name", taskElementArray[2]);
            jsonObj.put("description", taskElementArray[3]);
            LOGGER.info("Created request {}", jsonObj.toJSONString());
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
            String[] elements = s.split("`");
            TaskList taskList = new TaskList();
            taskList.setId(Integer.parseInt(elements[0]));
            taskList.setUserId(Integer.parseInt(elements[1]));
            taskList.setName(elements[2]);
            taskList.setDescription(elements[3]);
            if(elements.length>4)
                taskList.setTaskIds(toIntegerArrayList(elements[4].trim()));
            myTaskLists.add(taskList);
        }
        return myTaskLists;
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
            String[] categoryElementArray = s.split("`");
            Category category = new Category();
            category.setId(Integer.parseInt(categoryElementArray[0]));
            category.setUserId(Integer.parseInt(categoryElementArray[1]));
            category.setName(categoryElementArray[2]);
            category.setDescription(categoryElementArray[3]);
            if(categoryElementArray.length > 4)
                category.setTaskIds(toIntegerArrayList(categoryElementArray[4]));
            myCategories.add(category);
        }
        return myCategories;
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
            if(elements.length>5)
                schedule.setTaskIds(toIntegerArrayList(elements[5].trim()));
            mySchedules.add(schedule);
        }
        return mySchedules;
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
            task.setImportant(parseJsonBooleanAsBoolean(elements[3]));
            task.setNote(elements[4]);
            task.setEstimatedTime(Long.parseLong(elements[5]));
            task.setInvestedTime(Long.parseLong(elements[6]));
            task.setUrgent(parseJsonBooleanAsBoolean(elements[7]));
            task.setDueDate(parseJsonDateAsDate(elements[8]));
            task.setStatus(elements[9]);
            if (elements.length > 10) {
                task.setCategoryIds(toIntegerArrayList(elements[10]));
                task.setScheduleIds(toIntegerArrayList(elements[11]));
            }

            myTasks.add(task);
        }
        return myTasks;
    }
}

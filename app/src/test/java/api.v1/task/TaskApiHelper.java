package api.v1.task;

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
public class TaskApiHelper extends UnitTestHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(TaskApiHelper.class);

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of JSONObjects.
     * @param bactickTasks
     * @return
     * @throws Exception
     */
    static ArrayList<JSONObject> toJSONObjects(ArrayList<String> bactickTasks) throws Exception{
        ArrayList<JSONObject> myJSONObjects=new ArrayList<JSONObject>();
        for(String s: bactickTasks){
            String[] taskElementArray = s.split("`");
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", Integer.parseInt(taskElementArray[0]));
            jsonObj.put("taskListId", Integer.parseInt(taskElementArray[1].trim()));
            jsonObj.put("name", taskElementArray[2].trim());
            jsonObj.put("important", taskElementArray[3]);
            jsonObj.put("note", taskElementArray[4]);
            jsonObj.put("estimatedTime", Long.parseLong(taskElementArray[5]));
            jsonObj.put("investedTime", Long.parseLong(taskElementArray[6]));
            jsonObj.put("urgent", taskElementArray[7]);
            jsonObj.put("dueDate", taskElementArray[8]);
            jsonObj.put("status", taskElementArray[9]);
            if(taskElementArray.length> 10) {
                jsonObj.put("categoryIds", toIntegerArrayList(taskElementArray[10]));
                jsonObj.put("scheduleIds", toIntegerArrayList(taskElementArray[11]));
            }
            myJSONObjects.add(jsonObj);
        }
        return myJSONObjects;
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
            schedule.setRepeatType(Schedule.RepeatTypes.valueOf(elements[4].trim()));
            if(elements.length>5)
                schedule.setTaskIds(toIntegerArrayList(elements[5].trim()));
            mySchedules.add(schedule);
        }
        return mySchedules;
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

    protected static ArrayList<TaskList> toTaskLists(ArrayList<String> backtickTaskLists) throws Exception{
        ArrayList<TaskList> myTaskLists=new ArrayList<TaskList>();
        for(String s: backtickTaskLists) {
            String[] elements = s.split("`");
            TaskList taskList=new TaskList();
            taskList.setId(Integer.parseInt(elements[0]));
            taskList.setUserId(Integer.parseInt(elements[1]));
            taskList.setName(elements[2].trim());
            taskList.setDescription(elements[3].trim());
            if(elements.length>4) {
                taskList.setTaskIds(toIntegerArrayList(elements[4]));
            }
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
                if (elements.length > 12)
                    task.setReminderIds(toIntegerArrayList(elements[12]));
            }

            myTasks.add(task);
        }
        return myTasks;
    }
}

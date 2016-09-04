package api.v1.task;

import api.v1.UnitTestHelper;
import api.v1.model.Category;
import api.v1.model.Schedule;
import api.v1.model.TaskList;
import com.google.appengine.repackaged.com.google.gson.Gson;
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
            jsonObj.put("id", taskElementArray[0]);
            jsonObj.put("taskListId", taskElementArray[1].trim());
            jsonObj.put("name", taskElementArray[2].trim());
            jsonObj.put("important", taskElementArray[3]);
            jsonObj.put("note", taskElementArray[4]);
            jsonObj.put("estimatedTime", taskElementArray[5]);
            jsonObj.put("investedTime", taskElementArray[6]);
            jsonObj.put("urgent", taskElementArray[7]);
            jsonObj.put("dueDate", taskElementArray[8]);
            jsonObj.put("status", taskElementArray[9]);
            if(taskElementArray.length> 10) {
                jsonObj.put("categoryIds", taskElementArray[10]);
                jsonObj.put("scheduleIds", taskElementArray[11]);
            }
            LOGGER.info("Created request {}", jsonObj.toJSONString());
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
            schedule.setRepeatType(elements[4].trim());
            if(elements.length>5)
                schedule.setCategoryIds(toIntegerArrayList(elements[5].trim()));
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
            //LOGGER.debug("Here is element 0. It is supposed to be an id. {}", elements[0]);
            //LOGGER.debug("Here is element 1. It is supposed to be an id. {}", elements[1]);
            //LOGGER.debug("Here is element 2. It is supposed to be the TaskList name. {}", elements[2]);
            //LOGGER.debug("Here is element 3. It is supposed to be the TaskList description. {}", elements[3]);
            //LOGGER.debug("Here is element 4. It is supposed to be the Task IDs. {}", elements[4]);
            TaskList taskList=new TaskList();
            taskList.setId(Integer.parseInt(elements[0]));
            taskList.setUserId(Integer.parseInt(elements[1]));
            taskList.setName(elements[2].trim());
            taskList.setDescription(elements[3].trim());
            if(elements.length>4) {
                taskList.setTaskIds(toIntegerArrayList(elements[4]));
		//    LOGGER.debug("Here? Did we ever make it here?!?!");
            }

            LOGGER.debug("A new TaskList has just been created by the UnitTest class. {}{}", new Gson().toJson(taskList.getTaskIds()), taskList.toJson());
            myTaskLists.add(taskList);
        }
        return myTaskLists;
    }
}

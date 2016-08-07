package api.v1.taskList;

import api.v1.model.TaskList;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 8/7/16.
 */
public class TaskListApiHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(TaskListApiHelper.class);
    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of TaskLists.
     * @param backtickTaskLists
     * @return
     * @throws Exception
     */
    static ArrayList<TaskList> toTaskLists(ArrayList<String> backtickTaskLists)throws Exception{
        ArrayList<TaskList> myTaskLists=new ArrayList<TaskList>();
        for(String s: backtickTaskLists) {
            String[] taskListElementArray = s.split("`");
            TaskList taskList = new TaskList();
            taskList.setId(Integer.parseInt(taskListElementArray[0]));
            taskList.setName(taskListElementArray[1]);
            taskList.setDescription(taskListElementArray[2]);
            myTaskLists.add(taskList);
        }
        return myTaskLists;
    }

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
            jsonObj.put("name", taskElementArray[1]);
            jsonObj.put("description", taskElementArray[2]);
            LOGGER.info("Created request {}", jsonObj.toJSONString());
            myJSONObjects.add(jsonObj);
        }
        return myJSONObjects;
    }
}

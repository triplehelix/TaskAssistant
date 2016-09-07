package api.v1.taskList;

import api.v1.UnitTestHelper;
import api.v1.error.BusinessException;
import api.v1.error.Error;
import api.v1.model.Task;
import api.v1.model.TaskList;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
            jsonObj.put("name", taskElementArray[1]);
            jsonObj.put("description", taskElementArray[2]);
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
            taskList.setName(elements[1]);
            taskList.setDescription(elements[2]);
            if(elements.length>3)
                taskList.setTaskIds(toIntegerArrayList(elements[3].trim()));
            myTaskLists.add(taskList);
        }
        return myTaskLists;
    }
}

package api.v1.task;

import api.v1.UnitTestHelper;
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
            myJSONObjects.add(jsonObj);
        }
        return myJSONObjects;
    }

}

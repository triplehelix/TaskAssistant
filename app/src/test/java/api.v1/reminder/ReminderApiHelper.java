package api.v1.reminder;

import api.v1.UnitTestHelper;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 8/7/16.
 */
public class ReminderApiHelper extends UnitTestHelper{

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of JSONObjects.
     * @param backtickReminders
     * @return
     * @throws Exception
     */
    static ArrayList<JSONObject> toJSONObject(ArrayList<String> backtickReminders) throws Exception {
        ArrayList<JSONObject> myJSONObjects = new ArrayList<JSONObject>();
        for(String s: backtickReminders){
            String[] ReminderElementArray = s.split("`");
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id",           ReminderElementArray[0]);
            jsonObj.put("taskId",       ReminderElementArray[1]);
            jsonObj.put("reminderTime", ReminderElementArray[2]);
            myJSONObjects.add(jsonObj);
        }
        return myJSONObjects;
    }
}

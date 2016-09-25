package api.v1.auth;

import api.v1.UnitTestHelper;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 8/8/16.
 */
public class AuthApiHelper extends UnitTestHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(AuthApiHelper.class);

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of JSONObjects.
     * @param backtickCategories
     * @return
     * @throws Exception
     */
    protected static ArrayList<JSONObject> toJSONObject(ArrayList<String> backtickCategories) throws Exception{
        ArrayList<JSONObject> myJSONObjects = new ArrayList<JSONObject>();
        for(String s: backtickCategories) {
            String[] categoryElementArray = s.split("`");
            JSONObject jsonObj = new JSONObject();

            jsonObj.put("id", Integer.parseInt(categoryElementArray[0]));
            jsonObj.put("email", categoryElementArray[1].trim());
            jsonObj.put("password", categoryElementArray[2].trim());
            if(categoryElementArray.length>3){
                jsonObj.put("calendarIds", toIntegerArrayList(categoryElementArray[3]));
                jsonObj.put("categoryIds", toIntegerArrayList(categoryElementArray[4]));
                jsonObj.put("scheduleIds", toIntegerArrayList(categoryElementArray[5]));
                jsonObj.put("taskListIds", toIntegerArrayList(categoryElementArray[6]));
            }
            myJSONObjects.add(jsonObj);
        }
        return myJSONObjects;
    }
}

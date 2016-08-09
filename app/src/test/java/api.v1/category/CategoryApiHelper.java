package api.v1.category;

import api.v1.UnitTestHelper;
import api.v1.model.Category;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 8/7/16.
 */
public class CategoryApiHelper extends UnitTestHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(CategoryApiHelper.class);

   /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of JSONObjects.
     * @param backtickCategories
     * @return
     * @throws Exception
     */
    static ArrayList<JSONObject> toJSONObject(ArrayList<String> backtickCategories) throws Exception{
        ArrayList<JSONObject> myJSONObjects = new ArrayList<JSONObject>();
        for(String s: backtickCategories) {
            String[] CategoryElementArray = s.split("`");
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", CategoryElementArray[0]);
            jsonObj.put("name", CategoryElementArray[1]);
            jsonObj.put("description", CategoryElementArray[2]);
            LOGGER.info("Created request {}", jsonObj.toJSONString());
            myJSONObjects.add(jsonObj);
        }
        return myJSONObjects;
    }
}
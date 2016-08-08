package api.v1.category;

import api.v1.model.Category;
import api.v1.model.Task;
import api.v1.error.BusinessException;
import api.v1.error.Error;
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
public class CategoryApiHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(CategoryApiHelper.class);

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Categories.
     * @param backtickCategories
     * @return
     * @throws Exception
     */
    static ArrayList<Category> toCategories(ArrayList<String> backtickCategories) throws Exception{
        ArrayList<Category> myCategories = new ArrayList<Category>();
        for(String s:backtickCategories){
            String[] CategoryElementArray = s.split("`");
            Category category = new Category();
            category.setId(Integer.parseInt(CategoryElementArray[0]));
            category.setName(CategoryElementArray[1]);
            category.setDescription(CategoryElementArray[2]);
            myCategories.add(category);
        }
        return myCategories;
    }

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

    /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Tasks.
     * @param bactickTasks
     * @return
     * @throws Exception
     */
    static ArrayList<Task> toTasks(ArrayList<String> bactickTasks) throws Exception{
        ArrayList <Task> myTasks = new ArrayList<Task>();
        for(String s: bactickTasks) {
            String[] taskElementArray = s.split("`");
            Task task = new Task();
            task.setId(Integer.parseInt(taskElementArray[0]));
            task.setTaskListId(Integer.parseInt(taskElementArray[1]));
            task.setName(taskElementArray[2]);
            task.setImportant(CategoryApiHelper.parseJsonBooleanAsBoolean(taskElementArray[3]));
            task.setNote(taskElementArray[4]);
            task.setEstimatedTime(Long.parseLong(taskElementArray[5]));
            task.setInvestedTime(Long.parseLong(taskElementArray[6]));
            task.setUrgent(CategoryApiHelper.parseJsonBooleanAsBoolean(taskElementArray[7]));
            task.setDueDate(CategoryApiHelper.parseJsonDateAsDate(taskElementArray[8]));
            task.setStatus(taskElementArray[9]);
            myTasks.add(task);
        }
        return myTasks;
    }

    /**
     * Parse string as boolean.
     * @param b
     * @return
     */
    private static boolean parseJsonBooleanAsBoolean(String b) throws BusinessException {
        b = b.trim().toUpperCase();
        if (b.equals("TRUE"))
            return true;
        else if(b.equals("FALSE"))
            return false;
        else
            throw new BusinessException("Invalid boolean value: " + b, Error.valueOf("PARSE_BOOLEAN_ERROR"));
    }

    /**
     * Parse a String representing a given date and return a Date object.
     * String must be in the format: yyyy-MM-dd_HH:mm:ss
     *
     * @param stringDate
     * @return
     */
    private static Date parseJsonDateAsDate(String stringDate) throws BusinessException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        df.setLenient(false);
        Date result = null;
        try {
            result = df.parse(stringDate);
        } catch (java.text.ParseException e) {
            LOGGER.error("Exception while parsing date token: " + stringDate, e);
            throw new BusinessException("Error caused by the String date: " + stringDate, Error.valueOf("PARSE_DATE_ERROR"));
        }
        return result;
    }
}
package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class serves a a container for test case proto-Categories.
 * Created by kennethlyon on 6/9/16.
 */
public class CategoryTest {
    private static Logger LOGGER = LoggerFactory.getLogger(CategoryTest.class);
    private static ArrayList<String> validCategories;
    private static ArrayList<String> errorCategories;
    private static ArrayList<String> validUpdates;
    private static ArrayList<String> errorUpdates;

    static {

        /* Add valid Categories. Categories fields are arranged in the order:
         * validCategories.add("int id`);
         */
        validCategories = new ArrayList<String>();
        validUpdates = new ArrayList<String>();
        errorCategories = new ArrayList<String>();
        errorUpdates = new ArrayList<String>();
        validCategories.add("");
        validCategories.add("");
        validCategories.add("");
        validCategories.add("");
        validCategories.add("");
        validCategories.add("");
        validCategories.add("");
        validCategories.add("");
        validCategories.add("");
        validCategories.add("");
        errorCategories.add("");
        errorCategories.add("");
        errorCategories.add("");
        errorCategories.add("");
        errorCategories.add("");
        errorCategories.add("");
        errorCategories.add("");
        errorCategories.add("");
        errorCategories.add("");
        errorCategories.add("");

        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");

        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
    }


    public static ArrayList<JSONObject> getValidTestCategoriesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();

        for (String s : validCategories)
            jsonObjectArrayList.add(CategoryTest.toJson(s));
        return jsonObjectArrayList;
    }

    public static ArrayList<JSONObject> getErrorTestCategoriesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : errorCategories)
            jsonObjectArrayList.add(CategoryTest.toJson(s));
        return jsonObjectArrayList;

    }

    public static ArrayList<JSONObject> getValidTestCategoryUpdatesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : validUpdates)
            jsonObjectArrayList.add(CategoryTest.toJson(s));
        return jsonObjectArrayList;
    }

    public static ArrayList<JSONObject> getErrorTestCategoryUpdatesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : errorUpdates)
            jsonObjectArrayList.add(CategoryTest.toJson(s));
        return jsonObjectArrayList;
    }


    public static ArrayList<Category> getValidTestCategoriesAsCategories() throws Exception{
        ArrayList<Category> CategoryArrayList = new ArrayList<Category>();
        for (String s : validCategories) {
            CategoryArrayList.add(CategoryTest.toCategory(s));
        }
        return CategoryArrayList;
    }

    public static ArrayList<Category> getValidTestCategoriesUpdatesAsCategories() throws Exception{
        ArrayList<Category> CategoryArrayList = new ArrayList<Category>();
        for (String s : validUpdates) {
            CategoryArrayList.add(CategoryTest.toCategory(s));
        }
        return CategoryArrayList;
    }


    private static Category toCategory(String s) throws Exception{
        String[] CategoryElementArray = s.split("`");
        Category Category = new Category();
            Category.setId(Integer.parseInt(CategoryElementArray[0]));
        return Category;
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


    /**
     * Parse string as boolean.
     * @param b
     * @return
     */
    private static boolean parseJsonBooleanAsBoolean(String b) throws BusinessException{
        b = b.trim().toUpperCase();
        if (b.equals("TRUE"))
            return true;
        else if(b.equals("FALSE"))
            return false;
        else
            throw new BusinessException("Invalid boolean value: " + b, Error.valueOf("PARSE_BOOLEAN_ERROR"));
    }

    private static JSONObject toJson(String stringCategory) {
        String[] CategoryElementArray = stringCategory.split("`");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",   CategoryElementArray[0]);
        LOGGER.info("Created request {}", jsonObj.toJSONString());
        return jsonObj;
    }

    /**
     * @throws Exception
     */
    @Test
    public void setUp() throws Exception {
        for(String s: validCategories){
            CategoryTest.toCategory(s);
            LOGGER.info("Valid Category {}", toJson(s));
        }

        for(String s: validUpdates){
            CategoryTest.toCategory(s);
            LOGGER.info("Valid Category {}", toJson(s));
        }

        for(String s: errorCategories){
            validateErrorCategory(s);
            LOGGER.info("Error Category {}", toJson(s));
        }

        for(String s: errorUpdates){
            validateErrorCategory(s);
            LOGGER.info("Error Category {}", toJson(s));
        }

    }

    public void validateErrorCategory(String s){
        boolean error=false;
        try{
            CategoryTest.toCategory(s);
        }catch(Exception e){
            error=true;
            LOGGER.info("Invalid Category returned error. " + e.getMessage(), e);
        }
        if(!error){
            fail("Success returned for invalid Category: " + s);
        }
    }
}

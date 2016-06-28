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
 * What can go wrong with categories? This class is intended to test Category class 
 * and also serves as a container for sample category objects. 
 * At present, the only error a category should throw is an exception for a null or
 * empty String provided as it's name.
 * Created by kennethlyon on 6/9/16.
 */
public class CategoryTest {
    private static Logger LOGGER = LoggerFactory.getLogger(CategoryTest.class);
    private static ArrayList<String> validCategories;
    private static ArrayList<String> errorCategories;

    static {
        /* Add valid Categories. Categories fields are arranged in the order:
         * validCategories.add("int id`);
         */
        validCategories = new ArrayList<String>();
        errorCategories = new ArrayList<String>();

        validCategories.add("0`Physics`Homework, study groups, lab reports, etc, for physics II");
        validCategories.add("1`chores`Any kind of household chores.");
        validCategories.add("2`work`work related stuff only!");
        validCategories.add("3`money`Anything related to money. Taxes, budgeting, student loans, etc.");
        validCategories.add("4`Journal club`Tasks related to journal club");
        validCategories.add("5`Organic Chemistry`Homework, study groups, lab reports, etc, for organic chemistry.");
        validCategories.add("6`Social`Tasks related to semi-important social activities, not related to a higher priority category.");

        errorCategories.add("0``Homework, study groups, lab reports, etc, for physics II");
        errorCategories.add("1``Any kind of household chores.");
        errorCategories.add("2``work related stuff only!");
        errorCategories.add("3``Anything related to money. Taxes, budgeting, student loans, etc.");
        errorCategories.add("4``Tasks related to journal club");
        errorCategories.add("5``Study groups, lab reports, etc, for organic chemistry.");
        errorCategories.add("6``Tasks related to semi-important social activities, not related to a higher priority category.");

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

    public static ArrayList<Category> getValidTestCategoriesAsCategories() throws Exception{
        ArrayList<Category> CategoryArrayList = new ArrayList<Category>();
        for (String s : validCategories) {
            CategoryArrayList.add(CategoryTest.toCategory(s));
        }
        return CategoryArrayList;
    }

    private static Category toCategory(String s) throws Exception{
        String[] CategoryElementArray = s.split("`");
        Category Category = new Category();
        Category.setId(Integer.parseInt(CategoryElementArray[0]));
        Category.setName(CategoryElementArray[1]);
        Category.setDescription(CategoryElementArray[2]);
        return Category;
    }

    private static JSONObject toJson(String stringCategory) {
        String[] CategoryElementArray = stringCategory.split("`");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",          CategoryElementArray[0]);
        jsonObj.put("name",        CategoryElementArray[1]);
        jsonObj.put("description", CategoryElementArray[2]);
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

        for(String s: errorCategories){
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

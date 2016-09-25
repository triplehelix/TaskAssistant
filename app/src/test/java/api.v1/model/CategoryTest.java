package api.v1.model;

import api.v1.UnitTestHelper;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

import static org.springframework.test.util.AssertionErrors.fail;

/**
 * Created by kennethlyon on 6/9/16.
 */
public class CategoryTest extends UnitTestHelper{
    private static Logger LOGGER = LoggerFactory.getLogger(CategoryTest.class);
    private static ArrayList<String> validCategories;
    private static ArrayList<String> validUpdates;

    static {
        /* Add valid Categories. Categories fields are arranged in the order:
         * id, name, description.
         */
        validCategories = new ArrayList<String>();
        validUpdates = new ArrayList<String>();
        validCategories.add("0`0`Mikes work`This is for all of the work Mike does         `[0,1]`[1,5]");
        validCategories.add("1`0`Mikes home`This is for all of the chores Mike never does `[2,3]`[1,2]");
        validCategories.add("2`0`Mikes play`This is for Mike's recreational stuff         `[2,3]`[3,4]");
        validCategories.add("3`1`Ken's work`This is for all of the work Ken never does.   `[4,5]`[1,4]");
        validCategories.add("4`1`ken's home`This is for all of the chores Ken does.       `[6,7]`[1,2]");
        validCategories.add("5`1`Ken's play`This is for the recreational stuff Ken does.  `[6,7]`[4,5]");

        validUpdates.add("0`0`Mikes work`This is for all of the work Mike does         `[0,1]`[]");
        validUpdates.add("1`0`Mikes home`This is for all of the chores Mike never does `[]`[1,2]");
        validUpdates.add("2`0`Mikes play`This is for Mike's recreational stuff         `[2,3]`[4,3]");
        validUpdates.add("0`1`Ken's work`This is for all of the work Ken never does.   `[4,5]`[1,4]");
        validUpdates.add("4`1`ken&amp;s home`This is for all of the chores Ken does.   `[6,7]`[1,2]");
        validUpdates.add("5`0`Ken's play`This is for the recreational stuff Ken does.  `[6,7]`[4,5]");
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
            String[] element = s.split("`");
            Category category = new Category();
            category.setId(Integer.parseInt(element[0]));
            category.setUserId(Integer.parseInt(element[1]));
            category.setName(element[2]);
            category.setDescription(element[3]);
            category.setTaskIds(toIntegerArrayList(element[4]));
            category.setScheduleIds(toIntegerArrayList(element[5]));
            myCategories.add(category);
        }
        return myCategories;
    }

    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        // Verify that clones generated from "validCategories" are identical to themselves:
        ArrayList<Category> myCategories=toCategories(validCategories);
        ArrayList<Category> myUpdates=toCategories(validUpdates);
        Category tempCategory=null;
        LOGGER.info("Verifying object equivalence.");
        for(int i=0; i<myCategories.size(); i++){
            tempCategory=new Category(myCategories.get(i));
            LOGGER.info("Evaluating {} {}",
                    myCategories.get(i),
                    tempCategory);
            if(!myCategories.get(i).equals(tempCategory)){
                LOGGER.error("These objects were evaluated as not equal when they should be: {} {}",
                        myCategories.get(i).toJson(),
                        tempCategory.toJson());
                fail("Error! These objects should be equal!");
            }
        }

        // Verify that instances made from "validCategories" and validUpdates are not equal to eachother.
        LOGGER.info("Verifying object non-equivalence.");
        for(int i=0; i<myCategories.size(); i++){
            LOGGER.info("Evaluating {} {}",
                    myCategories.get(i),
                    myUpdates.get(i));
            if(myCategories.get(i).equals(myUpdates.get(i))){
                LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                        myCategories.get(i).toJson(),
                        myUpdates.get(i).toJson());
                fail("Error! These objects should not be equal!");
            }
        }


        // Verify Gson serialization works properly:
        LOGGER.info("Verifying Gson serialization works properly.");
        Gson gson=new Gson();
        String json="";
        for(int i=0; i<myCategories.size(); i++){
            json=myCategories.get(i).toJson();
            LOGGER.info("Evaluating {} {}", json, (gson.fromJson(json, Category.class)).toJson() );
            if(!myCategories.get(i).equals(gson.fromJson(json, Category.class))){
                LOGGER.info("Error attempting to serialize/deserialize the category {} {}", json, (gson.fromJson(json, Category.class)).toJson() );
            }
        }
    }
}

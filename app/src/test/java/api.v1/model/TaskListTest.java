package api.v1.model;

import api.v1.UnitTestHelper;
import api.v1.error.BusinessException;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;
import java.util.ArrayList;

/**
 * Here we create TaskLists and test their functionality.
 *
 * Created by kennethlyon on 6/9/16.
 */
public class TaskListTest extends UnitTestHelper{
    private static Logger LOGGER = LoggerFactory.getLogger(TaskListTest.class);
    private static ArrayList<String> validTaskLists=new ArrayList<String>();
    private static ArrayList<String> validUpdates=new ArrayList<String>();


    static {
        validTaskLists.add("0`0`Mike's Work Tasks`Tasks for work.                  `[1,5]");
        validTaskLists.add("1`0`Mike's Personal Tasks`Medical, vacay, finance etc. `[1,2]");
        validTaskLists.add("2`0`Mike's Excercize Tasks`This is for Excercize.      `[3,4]");
        validTaskLists.add("3`1`Ken's Work Tasks`Tasks for work.                  `[1,4]");
        validTaskLists.add("4`1`Ken's Personal Tasks`Medical, vacay, finance etc. `[1,2]");
        validTaskLists.add("5`1`Ken's Excercize Tasks`This is for Excercize       `[4,5]");

        validUpdates.add("0`0`Mike's Work Tasks`Tasks for work.                        `[]");
        validUpdates.add("1`0`Mike&amp;s Personal Tasks`Medical, vacay, finance etc.`[1,2]");
        validUpdates.add("2`0`Mike's Excercize Tasks`This is for Excercize.         `[4,3]");
        validUpdates.add("0`1`Ken&amp;s Work Tasks`Tasks for work.                  `[1,4]");
        validUpdates.add("4`1`Ken's Personal Tasks`Medical, vacay, finance etc.     `[0,2]");
        validUpdates.add("5`0`Ken's Excercize Tasks`This is for Excercize           `[4,5]");
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
            taskList.setUserId(Integer.parseInt(elements[1]));
            taskList.setName(elements[2].trim());
            taskList.setDescription(elements[3].trim());
            if(elements.length>4)
                taskList.setTaskIds(toIntegerArrayList(elements[4].trim()));
            myTaskLists.add(taskList);
        }
        return myTaskLists;
    }

    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        // Verify that clones generated from "validTaskLists" are identical to themselves:
        ArrayList<TaskList> myTaskLists=toTaskLists(validTaskLists);
        ArrayList<TaskList> myUpdates=toTaskLists(validUpdates);
        LOGGER.info("Verifying object equivalence.");
        TaskList tempTaskList=null;
        for(int i=0; i<myTaskLists.size(); i++){
            tempTaskList=new TaskList(myTaskLists.get(i));
            LOGGER.info("Evaluating {} {}",
                    myTaskLists.get(i),
                    tempTaskList);
            if(!myTaskLists.get(i).equals(tempTaskList)){
                LOGGER.error("These objects were evaluated as not equal when they should be: {} {}",
                        myTaskLists.get(i).toJson(),
                        tempTaskList.toJson());
                fail("Error! These objects should be equal!");
            }
        }


    // Verify that instances made from "validTaskLists" and validUpdates are not equal to eachother.
        LOGGER.info("Verifying object non-equivalence.");
        for(int i=0; i<myTaskLists.size(); i++){
            LOGGER.info("Evaluating {} {}",
                    myTaskLists.get(i),
                    myUpdates.get(i));
            if(myTaskLists.get(i).equals(myUpdates.get(i))){
                LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                        myTaskLists.get(i).toJson(),
                        myUpdates.get(i).toJson());
                fail("Error! These objects should not be equal!");
            }
        }


        // Verify Gson serialization works properly:
        LOGGER.info("Verifying Gson serialization works properly.");
        Gson gson=new Gson();
        String json="";
        for(int i=0; i<myTaskLists.size(); i++){
            json=myTaskLists.get(i).toJson();
            LOGGER.info("Evaluating {} {}", json, (gson.fromJson(json, TaskList.class)).toJson() );
            if(!myTaskLists.get(i).equals(gson.fromJson(json, TaskList.class))){
                LOGGER.info("Error attempting to serialize/deserialize the taskList {} {}", json, (gson.fromJson(json, TaskList.class)).toJson() );
            }
        }
    }
}

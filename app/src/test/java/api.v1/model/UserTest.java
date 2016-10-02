package api.v1.model;

import api.v1.UnitTestHelper;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;

import java.util.ArrayList;

/**
 *
 * Created by kennethlyon on 6/9/16.
 */
public class UserTest extends UnitTestHelper{
    private static Logger LOGGER = LoggerFactory.getLogger(UserTest.class);
    private static ArrayList<String> validUsers=new ArrayList<String>();
    private static ArrayList<String> validUpdates=new ArrayList<String>();

    static {
        validUpdates.add(     "0`mikehedden@gmail.com`a681wo$dKo` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [2,1,0]");
        validUpdates.add(       "1`kenlyon@gmail.com`Mouwkl87%qo` [2,1,3,4,7] ` [20,30,40,50,60]` [95,96,97,98,99]` [0,1,2]");
        validUpdates.add(           "2`kenlyon@test.com`e-W^2VmQ` [0,1,2,3,5] ` [100,101,102,103]`[11,22,33,44,55]` [0,1,2]");
        validUpdates.add(        "3`fatsteaks@gmail.com`+%D5|x%b` [9,8,7,6,5] ` [40,50,60,70,80]` [11,22,33,44,55]` [0,1,2]");
        validUpdates.add(      "4`yannisgreek@gmail.com`e-W^2VmQ` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]");
        validUpdates.add(    "5`rustypuppy123@gmail.com`3Z^V$xkE` [2,1,3,4,7] ` [20,30,40,50,60]` [11,22,33,44,55]` [0,1,2]");
        validUpdates.add(  "0`yo.momma.so.fat@gmail.com`6PnCK/?8` [0,1,2,3,5] ` [30,40,50,60,70]` [11,22,33,44,55]` [0,1,2]");
        validUpdates.add("1`under_scores_rule@gmail.com`6~Zas2R*` [0,2,1,3,4] ` [40,50,60,70,80]` [11,22,33,44,55]` [0,1,2]");
        validUpdates.add(  "2`test@mikehedden.gmail.com`i2@<uMtJ` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]");

        validUsers.add(     "0`mikehedden@gmail.com`a681wo$dKo` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(       "1`kenlyon@gmail.com`Mouwkl87%qo` [2,1,3,4,7] ` [20,30,40,50,60]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(           "2`kenlyon@test.com`e-W^2VmQ` [0,1,2,3,5] ` [30,40,50,60,70]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(        "3`fatsteaks@gmail.com`+%D5|x%b` [0,2,1,3,4] ` [40,50,60,70,80]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(      "4`yannisgreek@gmail.com`sy@UCL0_` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(       "5`rustypuppy@gmail.com`3Z^V$xkE` [2,1,3,4,7] ` [20,30,40,50,60]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(  "6`yo.momma.so.fat@gmail.com`6PnCK/?8` [0,1,2,3,5] ` [30,40,50,60,70]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add("7`under_scores_rule@gmail.com`6~Zas2R*` [0,2,1,3,4] ` [40,50,60,70,80]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(  "8`test@mikehedden.gmail.com`i2@<uMtJ` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]");
    }

    /**
     * Validate the addSchedule, addCalendar, addCategory & addTaskList.
     */
    private void testAddSchedulesCategoriesTaskListsAndCalendars() throws Exception{

        Calendar calendar=new Calendar();
        Category category=new Category();
        Schedule schedule=new Schedule();
        TaskList taskList=new TaskList();

        ArrayList<User> myUsers=toUsers(validUsers);
        User userCalendar, userCategory, userSchedule, userTaskList;
        userCalendar=new User(myUsers.get(0));
        userCategory=new User(myUsers.get(1));
        userSchedule=new User(myUsers.get(2));
        userTaskList=new User(myUsers.get(3));

        calendar.setId(1);
        category.setId(20);
        schedule.setId(11);
        taskList.setId(0);

        userCalendar.addCalendar(calendar);
        userCategory.addCategory(category);
        userSchedule.addSchedule(schedule);
        userTaskList.addTaskList(taskList);


        if(!myUsers.get(0).equals(userCalendar)){
            LOGGER.error("These objects were evaluated to be not equal when they should be: {} {}",
                    myUsers.get(0).toJson(),
                    userCalendar.toJson());
            fail("Error! These objects should be equal!");
        }
        if(!myUsers.get(1).equals(userCategory)){
            LOGGER.error("These objects were evaluated to be not equal when they should be: {} {}",
                    myUsers.get(1).toJson(),
                    userCategory.toJson());
            fail("Error! These objects should be equal!");
        }
        if(!myUsers.get(2).equals(userSchedule)){
            LOGGER.error("These objects were evaluated to be not equal when they should be: {} {}",
                    myUsers.get(2).toJson(),
                    userSchedule.toJson());
            fail("Error! These objects should be equal!");
        }
        if(!myUsers.get(3).equals(userTaskList)){
            LOGGER.error("These objects were evaluated to be not equal when they should be: {} {}",
                    myUsers.get(3).toJson(),
                    userTaskList.toJson());
            fail("Error! These objects should be equal!");
        }

        calendar.setId(31);
        category.setId(31);
        schedule.setId(31);
        taskList.setId(31);

        userCalendar.addCalendar(calendar);
        userCategory.addCategory(category);
        userSchedule.addSchedule(schedule);
        userTaskList.addTaskList(taskList);

        if(myUsers.get(0).equals(userCalendar)){
            LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                    myUsers.get(0).toJson(),
                    userCalendar.toJson());
            fail("Error! These objects should not be equal!");
        }
        if(myUsers.get(1).equals(userCategory)){
            LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                    myUsers.get(1).toJson(),
                    userCategory.toJson());
            fail("Error! These objects should be not equal!");
        }
        if(myUsers.get(2).equals(userSchedule)){
            LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                    myUsers.get(2).toJson(),
                    userSchedule.toJson());
            fail("Error! These objects should be not equal!");
        }
        if(myUsers.get(3).equals(userTaskList)){
            LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                    myUsers.get(3).toJson(),
                    userTaskList.toJson());
            fail("Error! These objects should be not equal!");
        }

    }

    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        // Verify that clones generated from "validUsers" are identical to themselves:
        ArrayList<User> myUsers=toUsers(validUsers);
        ArrayList<User> myUpdates=toUsers(validUpdates);
        LOGGER.info("Verifying object equivalence.");
        User tempUser=null;
        for(int i=0; i<myUsers.size(); i++){
            tempUser=new User(myUsers.get(i));
            LOGGER.info("Evaluating {} {}",
                    myUsers.get(i),
                    tempUser);
            if(!myUsers.get(i).equals(tempUser)){
                LOGGER.error("These objects were evaluated as not equal when they should be: {} {}",
                        myUsers.get(i).toJson(),
                        tempUser.toJson());
                fail("Error! These objects should be equal!");
            }
        }


        // Verify that instances made from "validUsers" and validUpdates are not equal to eachother:
        LOGGER.info("Verifying object non-equivalence.");
        for(int i=0; i<myUsers.size(); i++){
            LOGGER.info("Evaluating {} {}",
                    myUsers.get(i),
                    myUpdates.get(i));
            if(myUsers.get(i).equals(myUpdates.get(i))){
                LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                        myUsers.get(i).toJson(),
                        myUpdates.get(i).toJson());
                fail("Error! These objects should not be equal!");
            }
        }


        // Verify Gson serialization works properly:
        LOGGER.info("Verifying Gson serialization works properly.");
        Gson gson=new Gson();
        String json="";
        for(int i=0; i<myUsers.size(); i++){
            json=myUsers.get(i).toJson();
            LOGGER.info("Evaluating {} {}", json, (gson.fromJson(json, User.class)).toJson() );
            if(!myUsers.get(i).equals(gson.fromJson(json, User.class))){
                LOGGER.info("Error attempting to serialize/deserialize the user {} {}", json, (gson.fromJson(json, User.class)).toJson() );
            }
        }

        //Finally test the
        testAddSchedulesCategoriesTaskListsAndCalendars();

    }
}

package api.v1.model;

import api.v1.UnitTestHelper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;

import java.util.ArrayList;

/**
 * This class serves a a container for test case proto-reminders.
 * Created by kennethlyon on 6/9/16.
 */
public class ReminderTest extends UnitTestHelper{
    private static Logger LOGGER = LoggerFactory.getLogger(ReminderTest.class);
    private static ArrayList<String> validReminders=new ArrayList<String>();
    private static ArrayList<String> validUpdates=new ArrayList<String>();

    static {
        validReminders.add("0`1`2020-05-28_08:31:01");
        validReminders.add("1`1`2020-05-31_00:00:00");
        validReminders.add("2`2`2016-06-09_18:30:00");
        validReminders.add("3`2`2016-06-12_08:00:00");
        validReminders.add("4`3`2016-06-09_19:00:00");
        validReminders.add("5`4`2020-05-31_00:00:00");

        validUpdates.add("0`6`2020-05-31_00:00:00");
        validUpdates.add("1`5`2016-06-09_18:30:00"); 
        validUpdates.add("2`4`2016-06-09_19:00:00");
        validUpdates.add("3`3`2016-06-12_08:00:00");
        validUpdates.add("4`4`2020-05-28_08:31:01");
        validUpdates.add("5`7`2020-05-31_00:00:00");

    }


    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        // Verify that clones generated from "validReminders" are identical to themselves:
        ArrayList<Reminder> myReminders=toReminders(validReminders);
        ArrayList<Reminder> myUpdates=toReminders(validUpdates);
        LOGGER.info("Verifying object equivalence.");
        Reminder tempReminder=null;
        for(int i=0; i<myReminders.size(); i++){
            tempReminder=new Reminder(myReminders.get(i));
            LOGGER.info("Evaluating {} {}",
                    myReminders.get(i),
                    tempReminder);
            if(!myReminders.get(i).equals(tempReminder)){
                LOGGER.error("These objects were evaluated as not equal when they should be: {} {}",
                        myReminders.get(i).toJson(),
                        tempReminder.toJson());
                fail("Error! These objects should be equal!");
            }
        }


    // Verify that instances made from "validReminders" and validUpdates are not equal to eachother.
        LOGGER.info("Verifying object non-equivalence.");
        for(int i=0; i<myReminders.size(); i++){
            LOGGER.info("Evaluating {} {}",
                    myReminders.get(i),
                    myUpdates.get(i));
            if(myReminders.get(i).equals(myUpdates.get(i))){
                LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                        myReminders.get(i).toJson(),
                        myUpdates.get(i).toJson());
                fail("Error! These objects should not be equal!");
            }
        }
    }
}

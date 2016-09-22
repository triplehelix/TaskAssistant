package api.v1.model;

import api.v1.UnitTestHelper;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.jvm.hotspot.oops.Instance;

import static org.junit.Assert.fail;

import java.time.Instant;
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
        validReminders.add("0`1`2020-05-28T08:31:01.575Z");
        validReminders.add("1`1`2020-05-31T00:00:00.576Z");
        validReminders.add("2`2`2016-06-09T18:30:00.576Z");
        validReminders.add("3`2`2016-06-12T08:00:00.577Z");
        validReminders.add("4`3`2016-06-09T19:00:00.577Z");
        validReminders.add("5`4`2020-05-31T00:00:00.585Z");

        validUpdates.add("0`6`2016-09-22T03:37:55.575Z");
        validUpdates.add("1`5`2016-09-22T03:37:55.576Z"); 
        validUpdates.add("2`4`2016-09-22T03:37:55.576Z");
        validUpdates.add("3`3`2016-09-22T03:37:55.577Z");
        validUpdates.add("4`4`2016-09-22T03:37:55.577Z");
        validUpdates.add("5`7`2016-09-22T03:37:55.585Z");
    }


    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        //Gson gson=new Gson();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        ArrayList<Reminder> reminders=new ArrayList<>();

        for(Reminder r: toReminders(validReminders)) {
            LOGGER.info("Valid reminders: {} {}", r.getInstant().toString(), r.toJson());
            //LOGGER.info("Evaluating 2 {}", (gson.fromJson(r.toJson(), Reminder.class)).toJson());
        }


        for(Reminder r: toReminders(validUpdates)){
            LOGGER.info("Valid updates: {} {}", r.getInstant().toString(), r.toJson());
        }


        LOGGER.info("Is this possible? {}", validReminders.get(1).split("`")[2]);
        Instant instant= Instant.parse("2020-05-28T08:31:01.123Z");

        LOGGER.info("Well, is it? {}", instant.toString());
	//TODO Is there a way for Gson to print out ISO 8601 natively?

// 20:37:56.306 [main] INFO  api.v1.model.ReminderTest - 2016-09-22T03:37:55.575Z {"id":0,"taskId":1,"reminderTime":"May 28, 2020 8:31:01 AM","instant":{"seconds":1474515475,"nanos":575000000}}
// 20:37:56.318 [main] INFO  api.v1.model.ReminderTest - 2016-09-22T03:37:55.576Z {"id":1,"taskId":1,"reminderTime":"May 31, 2020 12:00:00 AM","instant":{"seconds":1474515475,"nanos":576000000}}
// 20:37:56.319 [main] INFO  api.v1.model.ReminderTest - 2016-09-22T03:37:55.576Z {"id":2,"taskId":2,"reminderTime":"Jun 9, 2016 6:30:00 PM","instant":{"seconds":1474515475,"nanos":576000000}}
// 20:37:56.327 [main] INFO  api.v1.model.ReminderTest - 2016-09-22T03:37:55.577Z {"id":3,"taskId":2,"reminderTime":"Jun 12, 2016 8:00:00 AM","instant":{"seconds":1474515475,"nanos":577000000}}
// 20:37:56.329 [main] INFO  api.v1.model.ReminderTest - 2016-09-22T03:37:55.577Z {"id":4,"taskId":3,"reminderTime":"Jun 9, 2016 7:00:00 PM","instant":{"seconds":1474515475,"nanos":577000000}}
// 20:37:56.331 [main] INFO  api.v1.model.ReminderTest - 2016-09-22T03:37:55.585Z {"id":5,"taskId":4,"reminderTime":"May 31, 2020 12:00:00 AM","instant":{"seconds":1474515475,"nanos":585000000}}










































































        /*
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

        // Verify Gson serialization works properly:
        LOGGER.info("Verifying Gson serialization works properly.");
        Gson gson = new Gson();
        String format="yyyy-MM-dd'T'HH:mm:ss";
        //Gson gson = new GsonBuilder().setDateFormat(format).create();
        String json="";
        for(int i=0; i<myReminders.size(); i++){
            json=myReminders.get(i).toJson();
            LOGGER.info("Evaluating 1 {}", json);
            LOGGER.info("Evaluating 2 {}", (gson.fromJson(json, Reminder.class)).toJson());
            if(!myReminders.get(i).equals(gson.fromJson(json, Reminder.class))){
                LOGGER.info("Error attempting to serialize/deserialize the reminder {} {}", json, (gson.fromJson(json, Reminder.class)).toJson() );
            }
        }//*/
    }
}

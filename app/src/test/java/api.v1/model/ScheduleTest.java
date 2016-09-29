package api.v1.model;

import api.v1.UnitTestHelper;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;

import java.util.ArrayList;

/**
 * This class serves a a container for test case proto-Schedules.
 * Created by kennethlyon on 6/9/16.
 */
public class ScheduleTest extends UnitTestHelper{
    private static Logger LOGGER = LoggerFactory.getLogger(ScheduleTest.class);
    private static ArrayList<String> validSchedules=new ArrayList<String>();
    private static ArrayList<String> validUpdates=new ArrayList<String>();

    
    @Before
    public void setUp() throws Exception {

        validSchedules.add("0`0`2016-06-28T18:00:00.000Z`2016-06-28T19:00:00.123Z`DAILY `[0,1]`[0]");  //exercize
        validSchedules.add("1`0`2016-07-03T09:00:00.000Z`2016-06-28T10:00:00.123Z`WEEKLY`[2,3]`[0]");  //church
        validSchedules.add("2`0`2016-06-28T09:00:00.000Z`2016-06-28T17:00:00.123Z`DAILY `[2,3]`[0]");  //workdays
        validSchedules.add("3`1`2016-06-30T18:00:00.000Z`2016-06-28T19:00:00.123Z`WEEKLY`[4,5]`[5]");  //date night.
        validSchedules.add("4`1`2016-07-03T16:00:00.000Z`2016-07-03T15:00:00.123Z`WEEKLY`[6,7]`[5]");  //Tacos!
        validSchedules.add("4`1`2016-07-03T16:00:00.000Z`2016-07-01T15:00:00.123Z`WEEKLY`[6,7]`[5]");  //Wings!

        validUpdates.add("0`0`2016-06-28T18:00:00.000Z`2016-06-28T19:00:00.123Z`DAILY `[]`[0]");     //exercize
        validUpdates.add("1`0`2016-07-03T09:00:00.000Z`2016-06-28T10:00:00.123Z`WEEKLY`[2,3]`[1]");  //church
        validUpdates.add("2`0`2016-06-28T09:00:00.000Z`2016-06-28T17:00:00.123Z`DAILY `[3,2]`[0]");  //workdays
        validUpdates.add("3`1`2016-06-30T18:00:00.000Z`2016-06-28T19:00:00.123Z`NONE`[4,5]`[5]");    //date night.
        validUpdates.add("4`1`2016-07-03T16:00:00.000Z`2016-07-05T15:00:00.123Z`WEEKLY`[6,7]`[5]");  //Tacos!
        validUpdates.add("4`1`2016-07-02T16:00:00.000Z`2016-07-01T15:00:00.123Z`WEEKLY`[6,7]`[5]");  //Wings!
    }

  /**
     * Accept an ArrayList of backtick delimited strings and return an ArrayList of Categories.
     * @param backtickCategories
     * @return ArrayList<Schedule>
     * @throws Exception
     */
    protected static ArrayList<Schedule> toSchedules(ArrayList<String> backtickCategories) throws Exception{
        ArrayList<Schedule> mySchedules = new ArrayList<Schedule>();
        for(String s:backtickCategories){
            String[] elements = s.split("`");
            Schedule schedule = new Schedule();
            schedule.setId(Integer.parseInt(elements[0]));
            schedule.setUserId(Integer.parseInt(elements[1]));
            schedule.setStartDate(parseJsonDateAsDate(elements[2]));
            schedule.setEndDate(parseJsonDateAsDate(elements[3]));
            schedule.setRepeatType(Schedule.RepeatTypes.valueOf(elements[4].trim()));
            schedule.setCategoryIds(toIntegerArrayList(elements[5]));
            schedule.setTaskIds(toIntegerArrayList(elements[6]));
            mySchedules.add(schedule);
        }
        return mySchedules;
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        // Verify that clones generated from "validSchedules" are identical to themselves:
        ArrayList<Schedule> mySchedules=toSchedules(validSchedules);
        ArrayList<Schedule> myUpdates=toSchedules(validUpdates);
        LOGGER.info("Verifying object equivalence.");
        Schedule tempSchedule=null;
        for(int i=0; i<mySchedules.size(); i++){
            tempSchedule=new Schedule(mySchedules.get(i));
            LOGGER.info("Evaluating {} {}",
                    mySchedules.get(i),
                    tempSchedule);
            if(!mySchedules.get(i).equals(tempSchedule)){
                LOGGER.error("These objects were evaluated as not equal when they should be: {} {}",
                        mySchedules.get(i).toJson(),
                        tempSchedule.toJson());
                fail("Error! These objects should be equal!");
            }
        }

        // Verify that instances made from "validSchedules" and validUpdates are not equal to eachother.
        LOGGER.info("Verifying object non-equivalence.");
        for(int i=0; i<mySchedules.size(); i++){
            LOGGER.info("Evaluating {} {}",
                    mySchedules.get(i),
                    myUpdates.get(i));
            if(mySchedules.get(i).equals(myUpdates.get(i))){
                LOGGER.error("These objects were evaluated to be equal when they should not be: {} {}",
                        mySchedules.get(i).toJson(),
                        myUpdates.get(i).toJson());
                fail("Error! These objects should not be equal!");
            }
        }

        // Verify Gson serialization works properly:
        LOGGER.info("Verifying Gson serialization works properly.");
        String format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Gson gson = new GsonBuilder().setDateFormat(format).create();
        String json="";
        for(int i=0; i<mySchedules.size(); i++){
            json=mySchedules.get(i).toJson();
            LOGGER.info("Evaluating {} {}", json, (gson.fromJson(json, Schedule.class)).toJson());
            if(!mySchedules.get(i).equals(gson.fromJson(json, Schedule.class))){
                LOGGER.info("Error attempting to serialize/deserialize the schedule {} {}", json, (gson.fromJson(json, Schedule.class)).toJson() );
            }
        }
    }
}

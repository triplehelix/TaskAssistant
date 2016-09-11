package api.v1.model;

import api.v1.UnitTestHelper;
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

        validSchedules.add("0`0`2016-06-28_18:00:00`2016-06-28_19:00:00`DAILY `[0,1]`[0]");  //exercize
        validSchedules.add("1`0`2016-07-03_09:00:00`2016-06-28_10:00:00`WEEKLY`[2,3]`[0]");  //church
        validSchedules.add("2`0`2016-06-28_09:00:00`2016-06-28_17:00:00`DAILY `[2,3]`[0]");  //workdays
        validSchedules.add("3`1`2016-06-30_18:00:00`2016-06-28_19:00:00`WEEKLY`[4,5]`[5]");  //date night.
        validSchedules.add("4`1`2016-07-03_16:00:00`2016-07-03_15:00:00`WEEKLY`[6,7]`[5]");  //Tacos!
        validSchedules.add("4`1`2016-07-03_16:00:00`2016-07-01_15:00:00`WEEKLY`[6,7]`[5]");  //Wings!

        validUpdates.add("0`0`2016-06-28_18:00:00`2016-06-28_19:00:00`DAILY `[]`[0]");     //exercize
        validUpdates.add("1`0`2016-07-03_09:00:00`2016-06-28_10:00:00`WEEKLY`[2,3]`[1]");  //church
        validUpdates.add("2`0`2016-06-28_09:00:00`2016-06-28_17:00:00`DAILY `[3,2]`[0]");  //workdays
        validUpdates.add("3`1`2016-06-30_18:00:00`2016-06-28_19:00:00`NONE`[4,5]`[5]");    //date night.
        validUpdates.add("4`1`2016-07-03_16:00:00`2016-07-05_15:00:00`WEEKLY`[6,7]`[5]");  //Tacos!
        validUpdates.add("4`1`2016-07-02_16:00:00`2016-07-01_15:00:00`WEEKLY`[6,7]`[5]");  //Wings!
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
            schedule.setRepeatType(elements[4].trim());
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
    }
}

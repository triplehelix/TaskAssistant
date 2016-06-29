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
 * This class serves a a container for test case proto-Schedules.
 * Created by kennethlyon on 6/9/16.
 */
public class ScheduleTest {
    private static Logger LOGGER = LoggerFactory.getLogger(ScheduleTest.class);
    private static ArrayList<String> validSchedules;
    private static ArrayList<String> errorSchedules;

    static {

        /* Add valid Schedules. Schedules fields are arranged in the order:
         * validSchedules.add("int id`);
         * private int id;
         * private Date startDate;
         * private Date endDate;
         * public static enum RepeatTypes {NONE, DAILY, WEEKLY, MONTHLY, YEARLY};
         * private RepeatTypes repeatType;
         */
        validSchedules = new ArrayList<String>();
        errorSchedules = new ArrayList<String>();

        validSchedules.add("0`2016-06-28_18:00:00`2016-06-28_19:00:00`DAILY");   //exercize    
        validSchedules.add("1`2016-07-03_09:00:00`2016-06-28_10:00:00`WEEKLY");	 //church      
        validSchedules.add("2`2016-06-28_09:00:00`2016-06-28_17:00:00`DAILY");	 //workdays    
        validSchedules.add("3`2016-06-30_18:00:00`2016-06-28_19:00:00`WEEKLY");	 //date night. 
        validSchedules.add("4`2016-07-03_16:00:00`2016-07-03_15:00:00`WEEKLY");	 //Tacos!      

        errorSchedules.add("0`2016-02-31_18:00:00`2016-02-31_19:00:00`DAILY");   //exercize
        errorSchedules.add("1`2016-07-03_09:00:00`2016-06-28_10:00:00`SUNDAYS"); //church
        errorSchedules.add("2`2016-06-28_09:00:00`2016-06-28_17:00:00`WEEKDAYS");//workdays
        errorSchedules.add("3`2016-06-30_18:00:00`_`NEVER");                     //date night.
        errorSchedules.add("4`2016-07-03_16:00:00`2016-07-03`WEEKLY");           //Tacos!
    }



    public static ArrayList<JSONObject> getValidTestSchedulesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();

        for (String s : validSchedules)
            jsonObjectArrayList.add(ScheduleTest.toJson(s));
        return jsonObjectArrayList;
    }

    public static ArrayList<JSONObject> getErrorTestSchedulesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : errorSchedules)
            jsonObjectArrayList.add(ScheduleTest.toJson(s));
        return jsonObjectArrayList;

    }
    public static ArrayList<Schedule> getValidTestSchedulesAsSchedules() throws Exception{
        ArrayList<Schedule> ScheduleArrayList = new ArrayList<Schedule>();
        for (String s : validSchedules) {
            ScheduleArrayList.add(ScheduleTest.toSchedule(s));
        }
        return ScheduleArrayList;
    }

    private static Schedule toSchedule(String s) throws Exception{
        String[] scheduleElementArray = s.split("`");
        Schedule schedule = new Schedule();
        schedule.setId(Integer.parseInt(scheduleElementArray[0]));
        schedule.setStartDate(parseJsonDateAsDate(scheduleElementArray[1]));
        schedule.setEndDate(parseJsonDateAsDate(scheduleElementArray[2]));
        schedule.setRepeatType(scheduleElementArray[3]);
        return schedule;
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
            LOGGER.error("Exception while parsing date token: " + stringDate);
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

    private static JSONObject toJson(String stringSchedule) {
        String[] scheduleElementArray = stringSchedule.split("`");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",         scheduleElementArray[0]);
        jsonObj.put("startDate",  scheduleElementArray[1]);
        jsonObj.put("endDate",    scheduleElementArray[2]);
        jsonObj.put("repeatType", scheduleElementArray[3]);
        LOGGER.info("Created request {}", jsonObj.toJSONString());
        return jsonObj;
    }

    /**
     * @throws Exception
     */
    @Test
    public void setUp() throws Exception {
        for(String s: validSchedules){
            ScheduleTest.toSchedule(s);
            LOGGER.info("Valid Schedule {}", toJson(s));
        }

        for(String s: errorSchedules){
            validateErrorSchedule(s);
            LOGGER.info("Error Schedule {}", toJson(s));
        }
    }

    public void validateErrorSchedule(String s){
        boolean error=false;
        try{
            ScheduleTest.toSchedule(s);
        }catch(Exception e){
            error=true;
            LOGGER.info("Invalid Schedule returned error. " + e.getMessage(), e);
        }
        if(!error){
            fail("Success returned for invalid Schedule: " + s);
        }
    }
}







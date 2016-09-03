package api.v1;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import api.v1.repo.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import api.v1.error.BusinessException;
import org.slf4j.LoggerFactory;
import api.v1.error.Error;
import org.slf4j.Logger;

/**
 * This class is used by all handlers to parse the JSONObject referred
 * to as the JSONObject.
 * @author kennethlyon
 *
 */
public class BaseRequestHandler extends HttpServlet{

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRequestHandler.class);
    private final static String DATE_FORMAT_KEY="yyyy-MM-dd_HH:mm:ss";
    protected static TaskRepository taskRepository;
    protected static UserRepository userRepository;
    protected static TaskListRepository taskListRepository;
    protected static ReminderRepository reminderRepository;
    protected static CategoryRepository categoryRepository;
    protected static ScheduleRepository scheduleRepository;

    static {
        taskRepository = new TaskRepository();
        userRepository=new UserRepository();
        taskListRepository = new TaskListRepository();
        reminderRepository = new ReminderRepository();
        categoryRepository = new CategoryRepository();
        scheduleRepository = new ScheduleRepository();
    }

    public static TaskRepository getTaskRepository(){ return taskRepository; }
    public UserRepository getUserRepository(){ return userRepository; }
    public static TaskListRepository getTaskListRepository() { return taskListRepository; }
    public static ReminderRepository getReminderRepository() { return reminderRepository; }
    public static CategoryRepository getCategoryRepository() { return categoryRepository; }
    public static ScheduleRepository getScheduleRepository() { return scheduleRepository; }

    /**
     *
     * @param requestString
     * @return
     * @throws BusinessException
     */
    protected JSONObject parseRequest(String requestString)  throws BusinessException {
        JSONObject param = null;
        LOGGER.info("This is the JSON reqest: " + requestString);
        try{
            JSONParser parser = new JSONParser();
            param =  (JSONObject) parser.parse(requestString);
        }catch(ParseException e){
            LOGGER.error("Exception while parsing request: " + requestString);
            throw new BusinessException ("Error caused by: " + requestString, Error.valueOf("PARSE_JSON_ERROR"));
        }
        return param;
    }

    /**
     * Parse a String representing a given date and return a Date object.
     * String must be in the format: yyyy-MM-dd_HH:mm:ss
     * @param stringDate
     * @return
     */
    protected Date parseJsonDateAsDate(String stringDate) throws  BusinessException{
        DateFormat df = new SimpleDateFormat(DATE_FORMAT_KEY);
        df.setLenient(false);
        Date result = null;
        try{
            result = df.parse(stringDate);
        } catch (java.text.ParseException e) {
            LOGGER.error("Exception while parsing date token: " + stringDate);
            throw new BusinessException("Error caused by the String date: " + stringDate, Error.valueOf("PARSE_DATE_ERROR"));
        }
        return result;
    }
    
    /**
     * Parse a String representation of an integer as an Integer object. A
     * null Integer indicates that a NumberFormatException has occurred.
     * 
     * @param i
     * @return
     */
    protected Integer parseJsonIntAsInt(String i) throws BusinessException {
        Integer myInt=0;
        String nfeError="Exception while parsing the token as an integer: " + i;
        try{
            myInt = Integer.parseInt(i);
        }catch(NumberFormatException e){
            LOGGER.error(nfeError);
            throw new BusinessException(nfeError, Error.valueOf("PARSE_INTEGER_ERROR"));
        }
        return myInt;
    }

    /**
     * This method sends success/failure response back to the web layer that 
     * called the given servlet subclass. It also logs an error. 
     * 
     * @param error
     * @param message
     * @param response
     */
    @SuppressWarnings("unchecked")
    protected static void sendResponse(boolean error, String message, HttpServletResponse response) throws IOException{
        JSONObject obj = new JSONObject();
        obj.put("error", error);
        obj.put("errorMsg", message);
        PrintWriter out = response.getWriter();
        out.println(obj);
    }

    /**
     *
     * @param response
     * @param httpResponse
     * @throws IOException
     */
    protected static void sendMessage(JSONObject response, HttpServletResponse httpResponse) throws IOException{
        PrintWriter out = httpResponse.getWriter();
        out.println(response);
    }

    /**
     * Parse a String as a long integer.
     * @param l
     * @return
     */
    protected long parseJsonLongAsLong(String l) throws BusinessException{
        l=l.trim();
        long myLong=0;
        String nfeError="Exception while parsing the token as a long integer: " + l;
        try{
            myLong=java.lang.Long.parseLong(l);
        }catch(NumberFormatException nfe){
            LOGGER.error(nfeError);
            throw new BusinessException(nfeError, Error.valueOf("PARSE_LONG_INTEGER_ERROR"));
        }
        return myLong;
    }

    /**
     * Parse string as boolean.
     * @param b
     * @return
     */
    protected boolean parseJsonBooleanAsBoolean(String b) throws BusinessException{
        b = b.trim().toUpperCase();
        if (b.equals("TRUE"))
            return true;
        else if(b.equals("FALSE"))
            return false;
        else
            throw new BusinessException("Invalid boolean value: " + b, Error.valueOf("PARSE_BOOLEAN_ERROR"));
    }

    /**
     * Parse a JSON derived array and return an ArrayList of integers.
     * @param s
     * @return
     */
    protected static ArrayList<Integer> toIntegerArrayList(String s) {
        ArrayList<Integer> myIntegers = new ArrayList<Integer>();
        s=s.trim();
        if(s.equals("[]"))
            return null;
        s=s.substring(1,s.length()-1);
        String[] elements = s.split(",");
        for(String i: elements)
            myIntegers.add(Integer.parseInt(i));
        return myIntegers;
    }
}
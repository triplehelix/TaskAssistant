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
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import com.google.appengine.repackaged.com.google.gson.JsonSyntaxException;
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
    private final static String DATE_FORMAT_KEY="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
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
    public static UserRepository getUserRepository(){ return userRepository; }
    public static TaskListRepository getTaskListRepository() { return taskListRepository; }
    public static ReminderRepository getReminderRepository() { return reminderRepository; }
    public static CategoryRepository getCategoryRepository() { return categoryRepository; }
    public static ScheduleRepository getScheduleRepository() { return scheduleRepository; }


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
     * Accept a json String and an instance of the Object being made.  Return
     * an instance of Object created from the Json String.
     * @param obj
     * @return
     */
     protected Object getMyObject(String json, Object obj) throws BusinessException{
         LOGGER.info("Here is the Json object {} ", json);
         String message="An error occurred while deserializing the JSON object.";
         GsonBuilder gsonBuilder = new GsonBuilder();
         Gson gson = gsonBuilder.setDateFormat(DATE_FORMAT_KEY).create();
         try {
             obj = gson.fromJson(json, obj.getClass());

         }catch (NullPointerException npe){
             LOGGER.error(message, npe);
             throw new BusinessException(message, Error.valueOf("DESERIALIZATION_NULL_VALUE_ERROR"));
         }catch (NumberFormatException nfe){
             LOGGER.error(message, nfe);
             throw new BusinessException(message, Error.valueOf("DESERIALIZATION_NOT_A_NUMBER_ERROR"));
         }catch (JsonSyntaxException jse){
             LOGGER.error(message, jse);
             throw new BusinessException(message, Error.valueOf("DESERIALIZATION_JSON_SYNTAX_ERROR"));
         }
         return obj;
     }
}
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
     * Accept a 
     * @param obj
     * @return
     */
     protected Object getMyObject(String json, Object obj){
         LOGGER.info("Here is the Json object {} ", json);
         Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT_KEY).create();
         obj=gson.fromJson(json, obj.getClass());
         return obj;
    }
}
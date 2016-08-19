package api.v1.reminder;

import api.v1.model.Reminder;
import api.v1.model.Task;
import api.v1.model.User;
import api.v1.repo.ReminderRepository;
import api.v1.repo.TaskRepository;
import api.v1.repo.UserRepository;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.ArrayList;

/**
 * This class tests the AddReminder Class.
 * @author kennethlyon
 */
public class AddReminderTest extends ReminderApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(AddReminderTest.class);
    private static AddReminder addReminderInstance;
    private static ReminderRepository reminderRepository;
    private static TaskRepository taskRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> sampleTasks = new ArrayList<String>();
    private static ArrayList<String> validReminders=new ArrayList<String>();
    private static ArrayList<String> errorReminders=new ArrayList<String>();

    /**
     * First create a new Instance of AddReminder() object, then add new
     * reminder test cases to validRequestList and errorRequestList.
     * 
     * Verify that Reminders are only set for Task that exist. 
     * Verify that Reminders are actually placed in the repository.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        //1. Instantiate static members.
        addReminderInstance = new AddReminder();
        reminderRepository=addReminderInstance.getReminderRepository();
        taskRepository=addReminderInstance.getTaskRepository();

        //2. populate the TaskRepository with valid Tasks.
        sampleTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   []
        sampleTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [0,1]
        sampleTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [2,3]
        sampleTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [4]
        sampleTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [5]
        sampleTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   []
        sampleTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   []
        sampleTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   []
        for(Task task: ReminderApiHelper.toTasks(sampleTasks))
            taskRepository.add(task);

        validReminders.add("-1`1`2020-05-28_08:31:01"); //
        validReminders.add("-1`1`2020-05-31_00:00:00"); //
        validReminders.add("-1`2`2016-06-09_18:30:00"); //
        validReminders.add("-1`2`2016-06-12_08:00:00"); //
        validReminders.add("-1`3`2016-06-09_19:00:00"); //
        validReminders.add("-1`4`2020-05-31_00:00:00"); //

        errorReminders.add("0`1`0");
        errorReminders.add("1`1` ");
        errorReminders.add("2`2`yyyy-MM-dd_HH:mm:ss");
        errorReminders.add("3`2`2020-18-31_00:00:00");
        errorReminders.add("4`-3`2016-06-09_19:00:00");
        errorReminders.add("5`40`2020-05-31_00:00:00");

        //3. Create valid mock reminders.
        for(JSONObject jsonObj: ReminderApiHelper.toJSONObject(validReminders))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        //4. Create invalid mock reminders.
        for(JSONObject jsonObj: ReminderApiHelper.toJSONObject(errorReminders))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, empty the Task and Reminder repositories. Then,
     * set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        Reminder reminder=new Reminder();
        for(int i=0;i<validRequestList.size();i++) {
            reminder.setId(i);
            reminderRepository.delete(reminder);
        }
        for(Task task: ReminderApiHelper.toTasks(sampleTasks))
            taskRepository.delete(task);
        addReminderInstance = null;
        validRequestList = null;
        errorRequestList = null;
        sampleTasks=null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to AddReminder then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addReminderInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addReminderInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
        Reminder reminder=new Reminder();
        LOGGER.info("Verifying reminders were placed in the repository...");
        for(int i=0;i<validRequestList.size();i++) {
            reminder.setId(i);
            LOGGER.info(reminderRepository.get(reminder).toJson());
        }

        LOGGER.info("Verifying Tasks were updated in the repository...");
        for(Task task: toTasks(sampleTasks)){
            LOGGER.info(taskRepository.get(task).toJson());
        }

    }
}

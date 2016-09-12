package api.v1.reminder;

import api.v1.model.Reminder;
import api.v1.model.Task;
import api.v1.repo.ReminderRepository;
import api.v1.repo.TaskRepository;
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
 * This class tests the AddReminder Class
 * @author kennethlyon
 */
public class UpdateReminderTest extends ReminderApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(UpdateReminderTest.class);
    private static UpdateReminder updateReminderInstance;
    private static ReminderRepository reminderRepository;
    private static TaskRepository taskRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validReminders=new ArrayList<String>();
    private static ArrayList<String> validUpdates=new ArrayList<String>();
    private static ArrayList<String> errorUpdates=new ArrayList<String>();
    private static ArrayList<String> sampleTasks=new ArrayList<String>();

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
        updateReminderInstance = new UpdateReminder();
        reminderRepository=updateReminderInstance.getReminderRepository(); 
        taskRepository=updateReminderInstance.getTaskRepository();

        //2. Populate the repository with sample tasks.                                                                               // before   after                                                       
        sampleTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[]");    // []       []   
        sampleTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[0,1]"); // [0,1]    []   
        sampleTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[2,3]"); // [2,3]    []   
        sampleTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[4]");   // [4]      [3]  
        sampleTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[5]");   // [5]      [2,4]   
        sampleTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[]");    // []       [1]  
        sampleTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[]");    // []       [0]  
        sampleTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[]");    // []       [5]  
        for(Task task: ReminderApiHelper.toTasks(sampleTasks))
            taskRepository.add(task);

        //3. Populate the repository with valid reminders.
        validReminders.add("0`1`2020-05-28_08:31:01");
        validReminders.add("1`1`2020-05-31_00:00:00");
        validReminders.add("2`2`2016-06-09_18:30:00");
        validReminders.add("3`2`2016-06-12_08:00:00");
        validReminders.add("4`3`2016-06-09_19:00:00");
        validReminders.add("5`4`2020-05-31_00:00:00");
        for(Reminder reminder: toReminders(validReminders))
            reminderRepository.add(reminder);

        //4. Create invalid mock updates.
        errorUpdates.add("1`1`YYYY-MM-DD_hh:mm:ss");
        errorUpdates.add("1`1`2020-05-35_00:00:00");
        errorUpdates.add("1`20`2016-06-09_18:30:00");
        errorUpdates.add("4`M`2020-05-28_08:31:01");
        errorUpdates.add("K`4`2020-05-28_08:31:01");
        errorUpdates.add("-15`4`2020-05-31_00:00:00");
        for(JSONObject jsonObj: ReminderApiHelper.toJSONObject(errorUpdates))
            errorRequestList.add(createDoPostMockRequest(jsonObj));

        //5. Create valid mock updates.
        validUpdates.add("0`6`2020-05-31_00:00:00");
        validUpdates.add("1`5`2016-06-09_18:30:00"); // This is the update that breaks the API.
        validUpdates.add("2`4`2016-06-09_19:00:00");
        validUpdates.add("3`3`2016-06-12_08:00:00");
        validUpdates.add("4`4`2020-05-28_08:31:01");
        validUpdates.add("5`7`2020-05-31_00:00:00");
        for(JSONObject jsonObj: ReminderApiHelper.toJSONObject(validUpdates))
            validRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, empty the Task and Reminder repositories. Then,
     * set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(Reminder reminder: ReminderApiHelper.toReminders(validReminders))
            reminderRepository.delete(reminder);
        for(Task task: ReminderApiHelper.toTasks(sampleTasks))
            taskRepository.delete(task);

        verifyRepositoriesAreClean();
        updateReminderInstance = null;
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
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateReminderInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateReminderInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        Reminder reminder=new Reminder();
        LOGGER.info("Verifying reminders were updated...");
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

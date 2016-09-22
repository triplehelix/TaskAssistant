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
 * This class tests the DeleteReminder Class
 * @author kennethlyon
 */
public class DeleteReminderTest extends ReminderApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(DeleteReminderTest.class);
    private static DeleteReminder deleteReminderInstance;
    private static ReminderRepository reminderRepository;
    private static TaskRepository taskRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validReminders=new ArrayList<String>();
    private static ArrayList<String> errorReminders=new ArrayList<String>();
    private static ArrayList<String> sampleTasks=new ArrayList<String>();

    /**
     * First create a new Instance of DeleteReminder() object, then add
     * new reminder test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        deleteReminderInstance = new DeleteReminder();
        reminderRepository=deleteReminderInstance.getReminderRepository();
        taskRepository=deleteReminderInstance.getTaskRepository();

        //2. Populate the repository with sample tasks.                                                                                    // before   after  
        sampleTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[]");    // []       []   
        sampleTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[0,1]"); // [0,1]    []   
        sampleTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[2,3]"); // [2,3]    []   
        sampleTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[4]");   // [4]      [3]  
        sampleTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[5]");   // [5]      [2,4]   
        sampleTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[]");    // []       [1]  
        sampleTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[]");    // []       [0]  
        sampleTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[]");    // []       [5]
        for(Task task: ReminderApiHelper.toTasks(sampleTasks))
            taskRepository.add(task);

        //3. Populate the repository with valid reminders.
        validReminders.add("0`1`2020-05-28T08:31:01.123Z");
        validReminders.add("1`1`2020-05-31T00:00:00.123Z");
        validReminders.add("2`2`2016-06-09T18:30:00.123Z");
        validReminders.add("3`2`2016-06-12T08:00:00.123Z");
        validReminders.add("4`3`2016-06-09T19:00:00.123Z");
        validReminders.add("5`4`2020-05-31T00:00:00.123Z");
        for(Reminder reminder: toReminders(validReminders))
            reminderRepository.add(reminder);

        //4. Create invalid mock Reminders.
        errorReminders.add("-11`1`2020-05-35T00:00:00.123Z");
        errorReminders.add("100`1`2020-05-35T00:00:00.123Z");
        errorReminders.add("-15`4`2020-05-31T00:00:00.123Z");
        for(JSONObject jsonObj: ReminderApiHelper.toJSONObject(errorReminders))
            errorRequestList.add(createDoPostMockRequest(jsonObj));

        for(JSONObject jsonObj: ReminderApiHelper.toJSONObject(validReminders))
            validRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(Task task: ReminderApiHelper.toTasks(sampleTasks))
            taskRepository.delete(task);

        verifyRepositoriesAreClean();
        deleteReminderInstance = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to DeleteReminder then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            deleteReminderInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            deleteReminderInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        LOGGER.info("Verifying Tasks were updated in the repository...");
        for(Task task: toTasks(sampleTasks)){
            LOGGER.info(taskRepository.get(task).toJson());
        }
    }
}

package api.v1.reminder;

import api.v1.ApiTest;
import api.v1.model.Reminder;
import api.v1.model.ReminderTest;
import api.v1.model.Task;
import api.v1.model.TaskTest;
import api.v1.repo.ReminderRepository;
import api.v1.repo.TaskRepository;
import api.v1.task.TaskApiHelper;
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
public class UpdateReminderTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(UpdateReminderTest.class);
    private static UpdateReminder updateReminderInstance;
    private static ReminderRepository reminderRepository;
    private static TaskRepository taskRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validReminders;
    private static ArrayList<String> validUpdates;
    private static ArrayList<String> errorUpdates;
    private static ArrayList<String> validTasks;
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

        validTasks = new ArrayList<String>();
        validTasks.add("0`0`Feed dog`TRUE`Dog eats kibble.`60000`0`TRUE`2020-05-28_08:31:01`NEW");
        validTasks.add("1`0`Create AddTask unit test`TRUE`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS");
        validTasks.add("2`0`Buy beer`TRUE`Pick up some IPAs on the way home from work. Edit: Bill said he would pick up beers instead.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");
        validTasks.add("3`0`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`3600000`0`FALSE`2016-06-12_08:00:00`DEFERRED");
        validTasks.add("4`0`Shave`FALSE`GF said I need to shave.`180000`0`TRUE`2016-06-09_19:00:00`DONE");
        validTasks.add("5`0`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`TRUE`2016-06-09_19:00:00`NEW");
        validTasks.add("6`0`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW");
        validTasks.add("7`0`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED");
        validTasks.add("8`0`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS");

        validReminders=new ArrayList<String>();
        validReminders.add("0`1`2020-05-28_08:31:01");
        validReminders.add("1`1`2020-05-31_00:00:00");
        validReminders.add("2`2`2016-06-09_18:30:00");
        validReminders.add("3`2`2016-06-12_08:00:00");
        validReminders.add("4`3`2016-06-09_19:00:00");
        validReminders.add("5`4`2020-05-31_00:00:00");

        validUpdates=new ArrayList<String>();
        validUpdates.add("0`6`2020-05-31_00:00:00");
        validUpdates.add("1`5`2016-06-09_18:30:00");
        validUpdates.add("2`4`2016-06-09_19:00:00");
        validUpdates.add("3`3`2016-06-12_08:00:00");
        validUpdates.add("4`8`2020-05-28_08:31:01");
        validUpdates.add("5`7`2020-05-31_00:00:00");

        errorUpdates=new ArrayList<String>();
        errorUpdates.add("1`1`YYYY-MM-DD_hh:mm:ss");
        errorUpdates.add("1`1`2020-05-35_00:00:00");
        errorUpdates.add("1`20`2016-06-09_18:30:00");
        errorUpdates.add("4`M`2020-05-28_08:31:01");
        errorUpdates.add("K`4`2020-05-28_08:31:01");
        errorUpdates.add("-15`4`2020-05-31_00:00:00");


        //2. populate the TaskRepository and ReminderRepository.
        for(Task task: ReminderApiHelper.toTasks(validTasks))
                taskRepository.add(task);

        for(Reminder reminder: ReminderApiHelper.toReminders(validReminders))
                reminderRepository.add(reminder);

        //3. Create valid mock reminders.
        for(JSONObject jsonObj: ReminderApiHelper.toJSONObject(validReminders))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        //4. Create invalid mock reminders.
        for(JSONObject jsonObj: ReminderApiHelper.toJSONObject(errorUpdates))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
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
        for(Task task: ReminderApiHelper.toTasks(validTasks))
            taskRepository.delete(task);

        updateReminderInstance = null;
        validRequestList = null;
        errorRequestList = null;
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
            updateReminderInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        Reminder reminder=new Reminder();
        LOGGER.info("Verifying reminders were placed in the repository...");
        for(int i=0;i<validRequestList.size();i++) {
            reminder.setId(i);
            LOGGER.info(reminderRepository.get(reminder).toJson());
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateReminderInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
    }

    /**
     * Pass this method a json object to return a MockHttpServletRequest.
     * @param jsonObj
     * @return
     */
    private MockHttpServletRequest createDoPostMockRequest(JSONObject jsonObj){
        MockHttpServletRequest request = new MockHttpServletRequest();
        LOGGER.info("Created request {}",jsonObj.toJSONString());
        request.addParameter("params", jsonObj.toJSONString());
        return request;
    }
}

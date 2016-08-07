package api.v1.reminder;

import api.v1.ApiTest;
import api.v1.model.Reminder;
import api.v1.model.ReminderTest;
import api.v1.model.Task;
import api.v1.model.TaskTest;
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
public class AddReminderTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(AddReminderTest.class);
    private static AddReminder addReminderInstance;
    private static ReminderRepository reminderRepository;
    private static TaskRepository taskRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

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
        for(Task task: TaskTest.getValidTestTasksAsTasks())
                taskRepository.add(task);

        //3. Create valid mock reminders.
        for(JSONObject jsonObj: ReminderTest.getValidTestRemindersAsJson())
            validRequestList.add(createDoPostMockRequest(jsonObj));

        //4. Create invalid mock reminders.
        for(JSONObject jsonObj: ReminderTest.getErrorTestRemindersAsJson())
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, empty the Task and Reminder repositories. Then,
     * set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(Reminder reminder: ReminderTest.getValidTestRemindersAsReminders())
            reminderRepository.delete(reminder);
        for(Task task: TaskTest.getValidTestTasksAsTasks())
            taskRepository.delete(task);

        addReminderInstance = null;
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
            addReminderInstance.doPost(request, response);
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
            addReminderInstance.doPost(request, response);
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

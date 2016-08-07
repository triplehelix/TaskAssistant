package api.v1.reminder;

import api.v1.ApiTest;
import api.v1.model.Reminder;
import api.v1.model.ReminderTest;
import api.v1.repo.ReminderRepository;
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
 * This class tests the GetReminder Class
 * @author kennethlyon
 */
public class GetReminderTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(GetReminderTest.class);
    private static GetReminder getReminderInstance;
    private static ReminderRepository reminderRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

    /**
     * First create a new Instance of GetReminder() object, then add new
     * reminder test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        getReminderInstance = new GetReminder();
        reminderRepository=getReminderInstance.getReminderRepository();

        // Populate the Reminder repository with valid Reminders.
        for(Reminder reminder: ReminderTest.getValidTestRemindersAsReminders())
            reminderRepository.add(reminder);

        // Create valid mock reminders.
        for(JSONObject jsonObj: ReminderTest.getValidTestRemindersAsJson())
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create error mock reminders.
        for(JSONObject jsonObj: ReminderTest.getErrorTestReminderUpdatesAsJson())
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, empty the repository and set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(Reminder reminder: ReminderTest.getValidTestRemindersAsReminders())
            reminderRepository.delete(reminder);
        getReminderInstance = null;
        validRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to GetReminder then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getReminderInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getReminderInstance.doPost(request, response);
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

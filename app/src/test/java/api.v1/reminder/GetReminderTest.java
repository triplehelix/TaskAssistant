package api.v1.reminder;

import api.v1.model.Reminder;
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
public class GetReminderTest extends ReminderApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(GetReminderTest.class);
    private static GetReminder getReminderInstance;
    private static ReminderRepository reminderRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validReminders;
    private static ArrayList<String> errorReminders;

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

        validReminders=new ArrayList<String>();
        validReminders.add("0`1`2020-05-28_08:31:01");
        validReminders.add("1`1`2020-05-31_00:00:00");
        validReminders.add("2`2`2016-06-09_18:30:00");
        validReminders.add("3`2`2016-06-12_08:00:00");
        validReminders.add("4`3`2016-06-09_19:00:00");
        validReminders.add("5`4`2020-05-31_00:00:00");

        errorReminders=new ArrayList<String>();
        errorReminders.add("-1`1`2020-05-28_08:31:01");
        errorReminders.add("10`1`2020-05-31_00:00:00");
        errorReminders.add("20`2`2016-06-09_18:30:00");
        errorReminders.add("50`2`2016-06-12_08:00:00");
        errorReminders.add("-4`3`2016-06-09_19:00:00");
        errorReminders.add("6`4`2020-05-31_00:00:00");

        // Populate the Reminder repository with valid Reminders.
        for(Reminder reminder: ReminderApiHelper.toReminders(validReminders))
            reminderRepository.add(reminder);

        // Create valid mock reminders.
        for(JSONObject jsonObj: ReminderApiHelper.toJSONObject(validReminders))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create error mock reminders.
        for(JSONObject jsonObj: ReminderApiHelper.toJSONObject(errorReminders))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, empty the repository and set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(Reminder reminder: ReminderApiHelper.toReminders(validReminders))
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
}

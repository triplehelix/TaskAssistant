package api.v1.schedule;

import api.v1.model.*;
import api.v1.repo.ScheduleRepository;
import api.v1.repo.TaskListRepository;
import api.v1.repo.TaskRepository;
import api.v1.repo.UserRepository;
import api.v1.repo.CategoryRepository;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.ArrayList;

import static org.springframework.test.util.AssertionErrors.fail;

/**
 * This class tests the GetSchedule Class.
 * @author kennethlyon
 */
public class GetScheduleTest extends ScheduleApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(AddScheduleTest.class);
    private static GetSchedule getScheduleInstance;
    private static ScheduleRepository scheduleRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validSchedules=new ArrayList<String>();
    private static ArrayList<String> errorSchedules=new ArrayList<String>();

    /**
     * First create a new Instance of AddSchedule() object, then add new
     * schedule test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        getScheduleInstance=new GetSchedule();
        scheduleRepository=getScheduleInstance.getScheduleRepository();

                                                                            // Tasks Categories  
        validSchedules.add("0`0`2016-06-28_18:00:00`2016-06-28_19:00:00`DAILY `[0,1]`[]");
        validSchedules.add("1`0`2016-07-03_09:00:00`2016-06-28_10:00:00`WEEKLY`[2,3]`[0,1,2]");
        validSchedules.add("2`0`2016-06-28_09:00:00`2016-06-28_17:00:00`DAILY `[2,3]`[]");
        validSchedules.add("3`1`2016-06-30_18:00:00`2016-06-28_19:00:00`WEEKLY`[4,5]`[]");
        validSchedules.add("4`1`2016-07-03_16:00:00`2016-07-03_15:00:00`WEEKLY`[6,7]`[3,4,5]");
        validSchedules.add("5`1`2016-07-03_16:00:00`2016-07-01_15:00:00`WEEKLY`[6,7]`[]");
        for(Schedule schedule: toSchedules(validSchedules))
            scheduleRepository.add(schedule);

        /* Do not challenge the GetSchedule API with pointers to Tasks,
         * Users, Categories, that it does not have permission to edit. It
         * will not look at them anyway since it retrieves these references
         * from the repository.
         */
        errorSchedules.add("-1`0`2016-07-03_09:00:00`2016-06-28_10:00:00`WEEKLY");
        errorSchedules.add("6`0`2016-06-28_09:00:00`2016-06-28_17:00:00`DAILY");
        errorSchedules.add("600`0`2016-06-28_09:00:00`2016-06-28_17:00:00`DAILY");

        for(JSONObject jsonObj: ScheduleApiHelper.toJSONObject(errorSchedules))
            errorRequestList.add(createDoPostMockRequest(jsonObj));

        for(JSONObject jsonObj: ScheduleApiHelper.toJSONObject(validSchedules))
            validRequestList.add(createDoPostMockRequest(jsonObj));

    }

    /**
     * After doPost runs, remove schedules from the repository and set
     * pertinent objects to null.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
	for(Schedule schedule: toSchedules(validSchedules))
            scheduleRepository.delete(schedule);
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to AddSchedule then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getScheduleInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getScheduleInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }
    }
}

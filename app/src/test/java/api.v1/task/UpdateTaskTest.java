package api.v1.task;

import api.v1.model.Schedule;
import api.v1.model.Task;
import api.v1.model.TaskList;
import api.v1.repo.CategoryRepository;
import api.v1.repo.ScheduleRepository;
import api.v1.repo.TaskListRepository;
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
 * This class tests the output of the UpdateTask api.
 * Created by kennethlyon on 6/15/16.
 */
public class UpdateTaskTest extends TaskApiHelper{
    private Logger LOGGER = LoggerFactory.getLogger(UpdateTaskTest.class);
    private static UpdateTask updateTaskInstance;
    private static TaskRepository taskRepository;
    private static CategoryRepository categoryRepository;
    private static ScheduleRepository scheduleRepository;
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validUpdateTaskRequestList = new ArrayList<MockHttpServletRequest>();
    private static ArrayList<MockHttpServletRequest> errorUpdateTaskRequestList = new ArrayList<MockHttpServletRequest>();
    private static ArrayList<String> sampleTaskLists=new ArrayList<String>();
    private static ArrayList<String> sampleSchedules=new ArrayList<String>();
    private static ArrayList<String> sampleCategories=new ArrayList<String>();
    private static ArrayList<String> validTasks=new ArrayList<String>();
    private static ArrayList<String> validUpdates=new ArrayList<String>();
    private static ArrayList<String> errorUpdates=new ArrayList<String>();

    /**
     * First create a new Instance of AddTask and UpdateTask.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * After doPost runs, remove Tasks and TaskLists from the TaskRepository and
     * TaskListRepository. Set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to UpdateTask then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        LOGGER.debug("// 5. Post valid mock requests...");
        for (MockHttpServletRequest request : validUpdateTaskRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateTaskInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }
        LOGGER.debug("// 6. Post error mock requests...");
        for (MockHttpServletRequest request : errorUpdateTaskRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateTaskInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
    }
}

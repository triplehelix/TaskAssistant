package api.v1.task;

import api.v1.ApiTest;
import api.v1.model.TaskTest;
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
public class UpdateTaskTest extends ApiTest{
    private Logger LOGGER = LoggerFactory.getLogger(UpdateTaskTest.class);
    private static AddTask addTaskInstance;
    private static UpdateTask updateTaskInstance;
    private static ArrayList<MockHttpServletRequest> validUpdateTaskRequestList = new ArrayList<MockHttpServletRequest>();
    private static ArrayList<MockHttpServletRequest> errorUpdateTaskRequestList = new ArrayList<MockHttpServletRequest>();

    /**
     * First create a new Instance of AddTask and UpdateTask.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        LOGGER.debug("");
        LOGGER.debug("*********** Starting @Before ***********");
        LOGGER.debug("// 1. Start by creating AddTask and UpdateTask Objects.");
        addTaskInstance = new AddTask();
        updateTaskInstance = new UpdateTask();
        validUpdateTaskRequestList=new ArrayList<MockHttpServletRequest>();
        errorUpdateTaskRequestList=new ArrayList<MockHttpServletRequest>();

        LOGGER.debug("// 2. Next, use AddTask to populate the TaskRepository with Valid Tasks.");
        populateTaskRepositoryWithValidTasks();



        /* Use the TaskTest.validUpdates ArrayList to populate the
        * validUpdateTaskRequestList.
        */
        LOGGER.debug("// 3. Fetch completed tasks from the repository and create valid mock requests.");
        for(JSONObject jsonObj: TaskTest.getValidTestTasksAsJson())
            validUpdateTaskRequestList.add(createDoPostMockRequest(jsonObj));


        /* Use the TaskTest.errorTasks ArrayList to populate the
        * errorUpdateTaskRequestList.
        */
        LOGGER.debug("// 4. Create invalid mock requests.");
        for(JSONObject jsonObj: TaskTest.getErrorTestTasksAsJson())
            errorUpdateTaskRequestList.add(createDoPostMockRequest(jsonObj));



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

    /**
     * After doPost runs, set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        LOGGER.debug("@After: " + validUpdateTaskRequestList.size() + " " + errorUpdateTaskRequestList.size() + " ");
        addTaskInstance=null;
        updateTaskInstance=null;
        validUpdateTaskRequestList=null;
        errorUpdateTaskRequestList=null;
    }


    /**
     * This method populates the task repository with valid tasks.
     * @throws Exception
     */
    private void populateTaskRepositoryWithValidTasks() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        for(JSONObject jsonTask: TaskTest.getValidTestTasksAsJson()){
            request=createDoPostMockRequest(jsonTask);
            addTaskInstance.doPost(request, response);
        }
    }

    /**
     * This class creates a mock http servlet request from a string
     * task object such as those found in TaskTest.errorTasks.
     * @param jsonObj
     * @return
     */
    private MockHttpServletRequest createDoPostMockRequest(JSONObject jsonObj) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        LOGGER.info("Created request {}",jsonObj.toJSONString());
        request.addParameter("params",   jsonObj.toJSONString());
        return request;
    }
}

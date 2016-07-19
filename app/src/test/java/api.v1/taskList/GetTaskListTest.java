package api.v1.taskList;

import api.v1.ApiTest;
import api.v1.model.TaskList;
import api.v1.model.TaskListTest;
import api.v1.repo.TaskListRepository;
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
 *
 * @author kennethlyon on 20160711.
 */
public class GetTaskListTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(GetTaskListTest.class);
    private static GetTaskList getTaskListInstance;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

    /********************************************************************************
     * 20160713
     * Okay, so concerning the GetTaskList response does the current configuration
     * work? At present, that class creates a TaskList object and serializes it
     * using TaskList.toJson():
     *
     * JSONObject jsonResponse = new JSONObject();
     * if (error) {
     *     jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
     * } else {
     *     jsonResponse.put("success", true);
     *     jsonResponse.put("TaskList", taskList.toJson());
     * }
     * sendMessage(jsonResponse, response);
     * 
     * So, here we are attempting to add a JSON String to the JsonResponse object.
     * I am not sure how this will actually unfold. Make sure that the TaskList is 
     * actually returned via the PrintStream object, also ask Mike if we need to 
     * send a success message even if a valid response is sent.
     ********************************************************************************/

    /**
     * Create a new Instance of GetTaskList() object, then add new
     * taskList test cases to validRequestList and errorRequestList.
     *
     *
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Create a GetTaskList object.
        getTaskListInstance=new GetTaskList();

        //get the TaskListRepository and place valid TaskLists within it.
        TaskListRepository taskListRepository=getTaskListInstance.getTaskListRepository();
        for(TaskList taskList: TaskListTest.getValidTestTaskListsAsTaskLists())
            taskListRepository.add(taskList);

        for(JSONObject jsonObj: TaskListTest.getValidTestTaskListsAsJson())
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create invalid mock TaskLists.
        for(JSONObject jsonObj: TaskListTest.getErrorTestTaskListUpdatesAsJson())
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, set pertinent objects to null.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        getTaskListInstance = null;
        validRequestList = null;
        errorRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to GetTaskList then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     *
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getTaskListInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getTaskListInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
    }

    /**
     * Pass this method a json object to return a MockHttpServletRequest.
     *
     * @param jsonObj
     * @return
     */
    private MockHttpServletRequest createDoPostMockRequest(JSONObject jsonObj) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        LOGGER.info("Created request {}", jsonObj.toJSONString());
        request.addParameter("params", jsonObj.toJSONString());
        return request;
    }
}

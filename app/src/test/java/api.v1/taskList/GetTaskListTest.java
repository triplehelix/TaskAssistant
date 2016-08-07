package api.v1.taskList;

import api.v1.ApiTest;
import api.v1.model.TaskList;
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
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validTaskLists;
    private static ArrayList<String> errorTaskLists;
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

        validTaskLists=new ArrayList<String>();
        validTaskLists.add("0`TaskList 0 created from ValidTasks`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksAsTasks().");
        validTaskLists.add("1`TaskList 1 created from ValidTaskUpdates`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksUpdatesAsTasks().");

        errorTaskLists=new ArrayList<String>();
        errorTaskLists.add("-9`Invalid Id TaskList`This has an invalid id.");
        errorTaskLists.add("2`non existent id`This is an invalid TaskList because the ID should not exist in the repository.");

        //get the TaskListRepository and place valid TaskLists within it.
        taskListRepository=getTaskListInstance.getTaskListRepository();
        for(TaskList taskList: TaskListApiHelper.toTaskLists(validTaskLists))
            taskListRepository.add(taskList);


        for(JSONObject jsonObj: TaskListApiHelper.toJSONObjects(validTaskLists))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create invalid mock TaskLists.
        for(JSONObject jsonObj: TaskListApiHelper.toJSONObjects(errorTaskLists))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, remove test TaskLists from the repository. Set pertinent
     * objects to null.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(TaskList taskList: TaskListApiHelper.toTaskLists(validTaskLists))
            taskListRepository.delete(taskList);
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

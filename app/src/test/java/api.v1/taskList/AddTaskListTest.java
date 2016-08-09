package api.v1.taskList;

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
 * @author kennethlyon on 20160711.
 */
public class AddTaskListTest extends TaskListApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(AddTaskListTest.class);
    private static AddTaskList addTaskListInstance;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validTaskLists;
    private static ArrayList<String> errorTaskLists;

    /**
     * First create a new Instance of AddTaskList() object, then add new
     * taskList test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        addTaskListInstance=new AddTaskList();
        validTaskLists=new ArrayList<String>();
        validTaskLists.add("0`TaskList 0 created from ValidTasks`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksAsTasks().");
        validTaskLists.add("1`TaskList 1 created from ValidTaskUpdates`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksUpdatesAsTasks().");
        errorTaskLists=new ArrayList<String>();
        errorTaskLists.add("0`  `This TaskList has no name.");
        errorTaskLists.add("1``This is an invalid TaskList composed of Task ids from: TaskTest.getValidTestTasksUpdatesAsTasks().");


        for(JSONObject jsonObj: TaskListApiHelper.toJSONObjects(validTaskLists))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        for(JSONObject jsonObj: TaskListApiHelper.toJSONObjects(errorTaskLists))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, set pertinent objects to null. Also be sure to
     * remove all of the previously added tasks from the repository.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        TaskListRepository taskListRepository = addTaskListInstance.getTaskListRepository();
        for(TaskList taskList: TaskListApiHelper.toTaskLists(validTaskLists))
            taskListRepository.delete(taskList);
        addTaskListInstance = null;
        validRequestList = null;
        errorRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to AddTaskList then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     *
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        LOGGER.debug("The length of validRequestList is: " + validRequestList.size());
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addTaskListInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        LOGGER.debug("The length of errorRequestList is: " + errorRequestList.size());
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addTaskListInstance.doPost(request, response);
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

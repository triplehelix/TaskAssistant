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
 * @author kennethlyon on 20160711.
 */
public class DeleteTaskListTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(DeleteTaskListTest.class);
    private static DeleteTaskList deleteTaskListInstance;
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

    /**
     * First create a new Instance of DeleteTaskList() object, then add new
     * taskList test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        //First instantiate DeleteTaskList, then fetch TaskListRepository.
        deleteTaskListInstance=new DeleteTaskList();
        taskListRepository=DeleteTaskList.getTaskListRepository();
        // Populate the TaskListRepository with valid TaskLists.
        for(TaskList taskList: TaskListTest.getValidTestTaskListsAsTaskLists())
            taskListRepository.add(taskList);

        //Finally, create Mock HTTP Servlet Requests.
        for(JSONObject jsonObj: TaskListTest.getValidTestTaskListsAsJson())
            validRequestList.add(createDoPostMockRequest(jsonObj));
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
        deleteTaskListInstance = null;
        validRequestList = null;
        errorRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to DeleteTaskList then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     *
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        // First delete TaskLists that have been added to the repository.
        LOGGER.info("First delete TaskLists that have been added to the repository.");
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            deleteTaskListInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        // Next, try to delete them again, this time we should get errors.
        LOGGER.info("Next, try to delete them again, this time we should get errors.");
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            deleteTaskListInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        //Finally, we try to delete TaskLists that belong to the error updates list.
        LOGGER.debug("// Finally, we try to delete TaskLists that belong to the error updates list.");
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            deleteTaskListInstance.doPost(request, response);
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

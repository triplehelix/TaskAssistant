package api.v1.taskList;

import api.v1.ApiTest;
import api.v1.model.Task;
import api.v1.model.TaskList;
import api.v1.model.TaskListTest;
import api.v1.model.TaskTest;
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
 * This class is intended to test the taskList api for retrieving tasks that belong to
 * a given TaskList. To verify that this API works properly: 
 *    1. Add valid Tasks to the TaskRepository.
 *    2. Make requests for valid Tasks.
 *    3. Request tasks for a TaskList that does not exist.
 *
 * @author kennethlyon on 20160718.
 */
public class GetTasksTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(UpdateTaskListTest.class);
    private static GetTasks getTasksInstance;
    private static TaskRepository taskRepository;
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

    /**
     * Create a GetTask instance and fetch the TaskRepository. Populate
     * the repository with valid Tasks. create valid and error TaskLists.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Instantiate GetTasks, TaskRepository and TaskListRepository:
        getTasksInstance=new GetTasks();
        taskRepository=GetTasks.getTaskRepository();
        taskListRepository=GetTasks.getTaskListRepository();

        // Populate the TaskRepository:
        for(Task task: TaskTest.getValidTestTasksAsTasks())
            taskRepository.add(task);
        for(Task task: TaskTest.getValidTestTasksUpdatesAsTasks())
            taskRepository.add(task);

        //Populate the TaskListRepository:
        for(TaskList taskList: TaskListTest.getValidTestTaskListsAsTaskLists())
            taskListRepository.add(taskList);

        //Create valid Mock HTTP Servlet Requests:
        for(JSONObject j:TaskListTest.getValidTestTaskListsAsJson())
            validRequestList.add(createDoPostMockRequest(j));
        for(JSONObject j:TaskListTest.getValidTestTaskListUpdatesAsJson())
            validRequestList.add(createDoPostMockRequest(j));

        //Create error Mock HTTP Servlet Requests:
        for(JSONObject j:TaskListTest.getErrorTestTaskListUpdatesAsJson())
            errorRequestList.add(createDoPostMockRequest(j));
    }

    /**
     * After doPost runs, set pertinent objects to null.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        getTasksInstance=null;
        taskRepository=null;
        validRequestList=null;
        errorRequestList=null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to GetTasks then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     *
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getTasksInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getTasksInstance.doPost(request, response);
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

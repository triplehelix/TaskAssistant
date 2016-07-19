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
public class UpdateTaskListTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(UpdateTaskListTest.class);
    private static UpdateTaskList updateTaskListInstance;
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

    /**
     * Create a new Instance of UpdateTaskList() object, then add new
     * taskList test cases to validRequestList and errorRequestList.
     *
     * Test cases:
     * Add valid TaskLists to the repository.
     * Make valid updates to the repo. Verify that they have changed.
     * Make invalid updates to the repo. Verify that an exception is thrown.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Create a UpdateTaskList object.
        updateTaskListInstance=new UpdateTaskList();

        //get the TaskListRepository and place valid TaskLists within it.
        LOGGER.info("Here are the valid TaskLists being added to the Repository: ");
        taskListRepository=updateTaskListInstance.getTaskListRepository();
        for(TaskList taskList: TaskListTest.getValidTestTaskListsAsTaskLists())
            taskListRepository.add(taskList);

        for(JSONObject jsonObj: TaskListTest.getValidTestTaskListUpdatesAsJson())
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
        updateTaskListInstance = null;
        validRequestList = null;
        errorRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to UpdateTaskList then forward responses
     * to validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     *
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        MockHttpServletRequest  request=null;
        MockHttpServletResponse response=null;
        TaskList originalTaskList, updatedTaskList;
        for(int i=0;i<validRequestList.size();i++){
            request=validRequestList.get(i);
            //First get this particular TaskList from the repository.
            originalTaskList=new TaskList();
            originalTaskList.setId(i);
            originalTaskList=taskListRepository.get(originalTaskList);

            //Next, apply the update:
            response = new MockHttpServletResponse();
            updateTaskListInstance.doPost(request, response);

            //Now get the new TaskList from the repository.
            updatedTaskList=new TaskList();
            updatedTaskList.setId(i);
            updatedTaskList=taskListRepository.get(updatedTaskList);

            //Verify that they ARE different.
            LOGGER.info("Original TaskList: " +originalTaskList.toJson());
            LOGGER.info("Updated  TaskList: " +updatedTaskList.toJson());
            if(originalTaskList.toJson().equals(updatedTaskList.toJson()))
                throw new Exception("Error! TaskList was not updated!");
            validateDoPostValidResponse(response);
        }

        for(int i=0;i<errorRequestList.size();i++){
            request=errorRequestList.get(i);
            //First get this particular TaskList from the repository.
            originalTaskList=new TaskList();
            originalTaskList.setId(i);
            originalTaskList=taskListRepository.get(originalTaskList);

            //Next, apply the update:
            response = new MockHttpServletResponse();
            updateTaskListInstance.doPost(request, response);

            //Now get the "new" TaskList from the repository.
            updatedTaskList=new TaskList();
            updatedTaskList.setId(i);
            updatedTaskList=taskListRepository.get(updatedTaskList);

            //Verify that the TaskList in the repository HAS NOT been updated.
            LOGGER.info("Original TaskList: " +originalTaskList.toJson());
            LOGGER.info("Updated  TaskList: " +updatedTaskList.toJson());
            if(!originalTaskList.toJson().equals(updatedTaskList.toJson()))
                throw new Exception("Error! TaskList was not updated!");
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

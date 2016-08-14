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
 *
 * @author kennethlyon on 20160711.
 */
public class UpdateTaskListTest extends TaskListApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(UpdateTaskListTest.class);
    private static UpdateTaskList updateTaskListInstance;
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validTaskLists;
    private static ArrayList<String> validTaskListUpdates;
    private static ArrayList<String> errorTaskListUpdates;
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
        validTaskLists=new ArrayList<String>();
        validTaskLists.add("0`TaskList 0 created from ValidTasks`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksAsTasks().");
        validTaskLists.add("1`TaskList 1 created from ValidTaskUpdates`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksUpdatesAsTasks().");

        validTaskListUpdates=new ArrayList<String>();
        validTaskListUpdates.add("0`TaskList 0 created from ValidTasks`This is a valid update.");
        validTaskListUpdates.add("1`TaskList 1 created from ValidTaskUpdates`This is another valid update. ");

        errorTaskListUpdates=new ArrayList<String>();
        errorTaskListUpdates.add("-9`Invalid Id TaskList`This is an invalid TaskList because it has an invalid id.");
        errorTaskListUpdates.add("10` `This is an invalid TaskList because it has an invalid name.");



        for(TaskList taskList: TaskListApiHelper.toTaskLists(validTaskLists))
            taskListRepository.add(taskList);

        for(JSONObject jsonObj: TaskListApiHelper.toJSONObjects(validTaskListUpdates))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create invalid mock TaskLists.
        for(JSONObject jsonObj: TaskListApiHelper.toJSONObjects(errorTaskListUpdates))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, remove TaskLists from the repository, then set
     * pertinent objects to null.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(TaskList taskList: TaskListApiHelper.toTaskLists(validTaskLists))
            taskListRepository.delete(taskList);
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
}

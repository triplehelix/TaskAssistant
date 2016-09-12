package api.v1.taskList;

import api.v1.model.Task;
import api.v1.model.TaskList;
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
public class GetTasksTest extends TaskListApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(GetTasksTest.class);
    private static GetTasks getTasksInstance;
    private static TaskRepository taskRepository;
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validTasks;
    private static ArrayList<String> validTaskLists;
    private static ArrayList<String> errorTaskLists;

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
        /* Add valid tasks. Tasks fields are arranged in the order:
         * validTasks.add("int id` String name` boolean important` String note` long estimatedTime` long investedTime` boolean urgent` Date dueDate` State status");
         */
        validTasks = new ArrayList<String>();
        validTasks.add("0`0`Feed dog`TRUE`Dog eats kibble.`60000`0`TRUE`2020-05-28_08:31:01`NEW");
        validTasks.add("1`0`Create AddTask unit test`TRUE`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS");
        validTasks.add("2`0`Buy beer`TRUE`Pick up some IPAs on the way home from work. Edit: Bill said he would pick up beers instead.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");
        validTasks.add("3`0`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`3600000`0`FALSE`2016-06-12_08:00:00`DEFERRED");
        validTasks.add("4`0`Shave`FALSE`GF said I need to shave.`180000`0`TRUE`2016-06-09_19:00:00`DONE");
        validTasks.add("5`1`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`TRUE`2016-06-09_19:00:00`NEW");
        validTasks.add("6`1`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW");
        validTasks.add("7`1`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED");
        validTasks.add("8`1`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS");

        validTaskLists=new ArrayList<String>();
        validTaskLists.add("0`0`TaskList 0 created from ValidTasks`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksAsTasks().");
        validTaskLists.add("1`1`TaskList 1 created from ValidTaskUpdates`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksUpdatesAsTasks().");

        errorTaskLists=new ArrayList<String>();
        errorTaskLists.add("-9`0`Invalid Id TaskList`This is an invalid TaskList because it has an invalid id.");
        errorTaskLists.add("10`1` `This is an invalid TaskList because it has an invalid name.");

        // Populate the TaskRepository:
        for(Task task: TaskListApiHelper.toTasks(validTasks))
            taskRepository.add(task);

        //Populate the TaskListRepository:
        for(TaskList taskList: TaskListApiHelper.toTaskLists(validTaskLists))
            taskListRepository.add(taskList);

        //Create valid Mock HTTP Servlet Requests:
        for(JSONObject j:TaskListApiHelper.toJSONObjects(validTaskLists))
            validRequestList.add(createDoPostMockRequest(j));

        //Create error Mock HTTP Servlet Requests:
        for(JSONObject j:TaskListApiHelper.toJSONObjects(errorTaskLists))
            errorRequestList.add(createDoPostMockRequest(j));
    }

    /**
     * After doPost runs, remove Tasks and TaskLists from their respective
     * repositories. Then set pertinent objects to null.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(TaskList taskList: TaskListApiHelper.toTaskLists(validTaskLists))
            taskListRepository.delete(taskList);

        for(Task task: TaskListApiHelper.toTasks(validTasks))
            taskRepository.delete(task);

        getTasksInstance=null;
        taskRepository=null;
        validRequestList=null;
        errorRequestList=null;
        verifyRepositoriesAreClean();
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
}

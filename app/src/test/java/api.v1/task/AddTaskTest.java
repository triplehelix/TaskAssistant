package api.v1.task;

import api.v1.ApiTest;
import api.v1.model.Task;
import api.v1.model.TaskList;
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
 * This class tests the AddTask Class
 * @author kennethlyon
 */
public class AddTaskTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(AddTaskTest.class);
    private static AddTask addTaskInstance;
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validTaskLists;
    private static ArrayList<String> validTasks;
    private static ArrayList<String> errorTasks;
    /**
     * First create a new Instance of AddTask() object, then add new
     * task test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        addTaskInstance = new AddTask();
        taskListRepository=addTaskInstance.getTaskListRepository();
        validTaskLists=new ArrayList<String>();
        validTaskLists.add("0`TaskList 0 created from ValidTasks`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksAsTasks().");
        validTaskLists.add("1`TaskList 1 created from ValidTaskUpdates`This is a valid TaskList composed of Tasks from: TaskTest.getValidTestTasksUpdatesAsTasks().");

        /* Add valid tasks. Tasks fields are arranged in the order:
         * validTasks.add("int id` String name` boolean important` String note` long estimatedTime` long investedTime` boolean urgent` Date dueDate` State status");
         */
        validTasks = new ArrayList<String>();
        validTasks.add("0`0`Feed dog`TRUE`Dog eats kibble.`60000`0`TRUE`2020-05-28_08:31:01`NEW");
        validTasks.add("1`0`Create AddTask unit test`TRUE`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS");
        validTasks.add("2`0`Buy beer`TRUE`Pick up some IPAs on the way home from work. Edit: Bill said he would pick up beers instead.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");
        validTasks.add("3`0`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`3600000`0`FALSE`2016-06-12_08:00:00`DEFERRED");
        validTasks.add("4`0`Shave`FALSE`GF said I need to shave.`180000`0`TRUE`2016-06-09_19:00:00`DONE");
        validTasks.add("5`0`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`TRUE`2016-06-09_19:00:00`NEW");
        validTasks.add("6`0`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW");
        validTasks.add("7`0`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED");
        validTasks.add("8`0`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS");

        // Add error causing tasks.
        errorTasks = new ArrayList<String>();
        errorTasks.add("0`1`Call Attorney J.P. Coleostomy`TRUE`Bring photographic proof!`3600000`0`YES`2016-06-14_15:15:00`NEW");
        errorTasks.add("1`1`Fix mom's computer.`TRUE`Again!?!`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorTasks.add("2`1`Prepare for apocalyptic zombie-cat-hoard.`TRUE`Need cat nip and shotguns.`4200000`0`TRUE`yyyy-MM-dd_HH:mm:ss`NEW");
        errorTasks.add("3`1`Vaccinate cat against zombie cat syndrome.`TRUE`Don't forget that Mr Bigglesworth doesn't like shots.`1 hour`0`TRUE`2020-08-31_00:00:00`NEW");
        errorTasks.add("4`1`Change motor oil`BLUE`Use quicky-lube coupon.`1600000`0`FALSE`2020-05-31_22:00:00`NEW");
        errorTasks.add("5`1`merge git conflicts`TRUE`I really need to learn how to use git.`180000`0`TRUE`2020-05-31_03:00:00`incomplete");
        errorTasks.add("6`1`Refinish porch`FALSE``210000`0`TRUE`2020-09-31_00:00:00`NEW");
        errorTasks.add("7`1``TRUE`THIS TASK HAS NO NAME`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorTasks.add("8`-9`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS");


        //get the TaskListRepository and place valid TaskLists within it.
        for(TaskList taskList: TaskApiHelper.toTaskLists(validTaskLists))
            taskListRepository.add(taskList);

        // Create valid mock tasks.
        for(JSONObject jsonObj: TaskApiHelper.toJSONObjects(validTasks))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create invalid mock tasks.
        for(JSONObject jsonObj: TaskApiHelper.toJSONObjects(errorTasks))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, remove Tasks and TaskLists from the TaskRepository and
     * TaskListRepository. Set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(TaskList taskList: TaskApiHelper.toTaskLists(validTaskLists))
            taskListRepository.delete(taskList);
        TaskRepository taskRepository = addTaskInstance.getTaskRepository();
        for(Task task: TaskApiHelper.toTasks(validTasks))
            taskRepository.delete(task);
        addTaskInstance = null;
        validRequestList = null;
        errorRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to AddTask then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addTaskInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addTaskInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
    }

    /**
     * Pass this method a json object to return a MockHttpServletRequest.
     * @param jsonObj
     * @return
     */
    private MockHttpServletRequest createDoPostMockRequest(JSONObject jsonObj){
        MockHttpServletRequest request = new MockHttpServletRequest();
        LOGGER.info("Created request {}",jsonObj.toJSONString());
        request.addParameter("params", jsonObj.toJSONString());
        return request;
    }
}

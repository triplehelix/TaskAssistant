package api.v1.task;

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
 * This class tests the output of the UpdateTask api.
 * Created by kennethlyon on 6/15/16.
 */
public class UpdateTaskTest extends TaskApiHelper{
    private Logger LOGGER = LoggerFactory.getLogger(UpdateTaskTest.class);
    private static UpdateTask updateTaskInstance;
    private static TaskRepository taskRepository;
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validUpdateTaskRequestList = new ArrayList<MockHttpServletRequest>();
    private static ArrayList<MockHttpServletRequest> errorUpdateTaskRequestList = new ArrayList<MockHttpServletRequest>();
    private static ArrayList<String> validTaskLists;
    private static ArrayList<String> validTasks;
    private static ArrayList<String> validUpdates;
    private static ArrayList<String> errorUpdates;

    /**
     * First create a new Instance of AddTask and UpdateTask.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        LOGGER.debug("// 1. Start by creating UpdateTask and fetching the TaskRepository and TaskListRepository.");
        updateTaskInstance = new UpdateTask();
        taskRepository = updateTaskInstance.getTaskRepository();
        taskListRepository = updateTaskInstance.getTaskListRepository();

        validTaskLists = new ArrayList<String>();
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

        // Add valid mutations to valid tasks.
        validUpdates = new ArrayList<String>();
        validUpdates.add("0`1`Feed dog`TRUE`Give food to the fluff.`60000`0`TRUE`2020-05-28_08:31:01`NEW");
        validUpdates.add("1`1`Create AddTask unit test`false`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS");
        validUpdates.add("2`1`Buy beer`TRUE`Bill is getting IPAs for the party.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");
        validUpdates.add("3`1`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`1800000`0`FALSE`2016-06-12_08:00:00`DEFERRED");
        validUpdates.add("4`1`Shave`FALSE`GF said I need to shave.`180000`90000`TRUE`2016-06-09_19:00:00`DONE");
        validUpdates.add("5`1`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`false`2016-06-09_19:00:00`NEW");
        validUpdates.add("6`1`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW");
        validUpdates.add("7`1`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED");
        validUpdates.add("8`1`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`DONE");

        // Add invalid mutations to valid tasks.
        errorUpdates = new ArrayList<String>();
        errorUpdates.add("0`3`Call Attorney J.P. Coleostomy`TRUE`Bring photographic proof!`3600000`0`YES`2016-06-14_15:15:00`NEW");
        errorUpdates.add("1`3`Fix mom's computer.`TRUE`Again!?!`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorUpdates.add("2`3`Prepare for apocalyptic zombie-cat-hoard.`TRUE`Need cat nip and shotguns.`4200000`0`TRUE`yyyy-MM-dd_HH:mm:ss`NEW");
        errorUpdates.add("3`3`Vaccinate cat against zombie cat syndrome.`TRUE`Don't forget that Mr Bigglesworth doesn't like shots.`1 hour`0`TRUE`2020-08-31_00:00:00`NEW");
        errorUpdates.add("4`3`Change motor oil`BLUE`Use quicky-lube coupon.`1600000`0`FALSE`2020-05-31_22:00:00`NEW");
        errorUpdates.add("5`3`merge git conflicts`TRUE`I really need to learn how to use git.`180000`0`TRUE`2020-05-31_03:00:00`incomplete");
        errorUpdates.add("6`3`Refinish porch`FALSE``210000`0`TRUE`2020-09-31_00:00:00`NEW");
        errorUpdates.add("7`3``TRUE`THIS TASK HAS NO NAME`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorUpdates.add("8`100`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`DONE");

        LOGGER.debug("// 2. Next, use AddTask to populate the TaskRepository and TaskListRepository with Valid Tasks and TaskLists.");
        // Get the TaskListRepository and place valid TaskLists within it.
        for (TaskList taskList : TaskApiHelper.toTaskLists(validTaskLists))
            taskListRepository.add(taskList);

        // Place valid Tasks in the TaskList repository.
        for (Task task : TaskApiHelper.toTasks(validTasks))
            taskRepository.add(task);

        /* Use the TaskTest.validUpdates ArrayList to populate the
        * validUpdateTaskRequestList.
        */
        LOGGER.debug("// 3. Fetch completed tasks from the repository and create valid mock requests.");
        for (JSONObject jsonObj : TaskApiHelper.toJSONObjects(validUpdates))
            validUpdateTaskRequestList.add(createDoPostMockRequest(jsonObj));

        /* Use the TaskTest.errorTasks ArrayList to populate the
        * errorUpdateTaskRequestList.
        */
        LOGGER.debug("// 4. Create invalid mock requests.");
        for (JSONObject jsonObj : TaskApiHelper.toJSONObjects(errorUpdates))
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
     * After doPost runs, remove Tasks and TaskLists from the TaskRepository and
     * TaskListRepository. Set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        LOGGER.debug("@After: " + validUpdateTaskRequestList.size() + " " + errorUpdateTaskRequestList.size() + " ");
        for(TaskList taskList: TaskApiHelper.toTaskLists(validTaskLists))
            taskListRepository.delete(taskList);

        for(Task task: TaskApiHelper.toTasks(validUpdates))
            taskRepository.delete(task);

        updateTaskInstance=null;
        taskRepository=null;
        taskListRepository=null;
        validUpdateTaskRequestList=null;
        errorUpdateTaskRequestList=null;

    }
}

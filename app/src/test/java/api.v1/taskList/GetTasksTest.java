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
        validTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[0]`[0,1,2]");
        validTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[1]`[1,2]");
        validTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[2]`[1,2]");
        validTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[0]`[2,0]");
        validTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[3]`[3,4,5]");
        validTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[4]`[]");
        validTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[5]`[]");
        validTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[3]`[]");
        validTasks.add("8`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[3]`[]");



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

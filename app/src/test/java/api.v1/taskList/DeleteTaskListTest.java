package api.v1.taskList;

import api.v1.error.BusinessException;
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

import static org.springframework.test.util.AssertionErrors.fail;


/**
 * @author kennethlyon on 20160711.
 */
public class DeleteTaskListTest extends TaskListApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(DeleteTaskListTest.class);
    private static DeleteTaskList deleteTaskListInstance;
    private static TaskListRepository taskListRepository;
    private static TaskRepository taskRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validTaskLists=new ArrayList<String>();
    private static ArrayList<String> errorTaskLists=new ArrayList<String>();
    private static ArrayList<String> sampleTasks=new ArrayList<String>();

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
        taskRepository=DeleteTaskList.getTaskRepository();

        validTaskLists.add("0`TaskList 0`This is a valid TaskList.`[0,1,2,3]");
        validTaskLists.add("1`TaskList 1`This is a valid TaskList.`[4,5,6,7]");
        // Populate the TaskListRepository with valid TaskLists.
        for(TaskList taskList: TaskListApiHelper.toTaskLists(validTaskLists))
            taskListRepository.add(taskList);

        errorTaskLists.add("4`TaskList 4` There is no TaskList 4.");
        errorTaskLists.add("-1`TaskList -1 `-1 is an invalid object id.");

        sampleTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[0]");  //   [0]
        sampleTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[0]");  //   [0]
        sampleTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[1,2]");//   [1,2]
        sampleTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[1,2]");//   [1,2]
        sampleTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[3]");  //   [3] 
        sampleTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[3]");  //   [3] 
        sampleTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[4,5]");//   [4,5]
        sampleTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[4,5]");//   [4,5]
        for(Task task: toTasks(sampleTasks))
            taskRepository.add(task);

        //Finally, create Mock HTTP Servlet Requests.
        for(JSONObject jsonObj: TaskListApiHelper.toJSONObjects(validTaskLists))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        for(JSONObject jsonObj: TaskListApiHelper.toJSONObjects(errorTaskLists))
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
        // Finally, we try to delete TaskLists that belong to the error updates list.
        LOGGER.debug("Delete TaskLists that belong to the error updates list.");
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            deleteTaskListInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

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


        LOGGER.info("Verifying Tasks have been cleaned...");
        for(Task task: toTasks(sampleTasks)) {
        boolean taskDeleted=false;
            try {
                taskRepository.delete(task);

            }catch (BusinessException e){
                taskDeleted=true;
            }

            if(!taskDeleted) {
                LOGGER.error("The Task Object was never removed from the database! {}", task.toJson());
                fail("DeleteTaskList failed because of cleanup.");
            }
        }
        LOGGER.info("Tasks cleaned successfully.");
    }
}

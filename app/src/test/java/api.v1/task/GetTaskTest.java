package api.v1.task;

import api.v1.model.Task;
import api.v1.model.TaskList;
import api.v1.repo.CategoryRepository;
import api.v1.repo.ScheduleRepository;
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
public class GetTaskTest extends TaskApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(AddTaskTest.class);
    private static GetTask getTaskInstance;
    private static TaskRepository taskRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validTasks=new ArrayList<String>();
    private static ArrayList<String> errorTasks=new ArrayList<String>();
    /**
     * First create a new Instance of AddTask() object, then add new
     * task test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        getTaskInstance = new GetTask();
        taskRepository=getTaskInstance.getTaskRepository();

        validTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW"); //   [0]  
        validTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW"); //   [0]  
        validTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW"); //   [1,2]
        validTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW"); //   [1,2]
        validTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW"); //   [3]  
        validTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW"); //   [3]  
        validTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW"); //   [4,5] 
        validTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW"); //   [4,5]
        for(Task task: TaskApiHelper.toTasks(validTasks))
            taskRepository.add(task);

        errorTasks.add("8`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW");
        errorTasks.add("-10`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW");
        errorTasks.add("200`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW");
        
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
        for(Task task: toTasks(validTasks))
            taskRepository.delete(task);

        verifyRepositoriesAreClean();
        getTaskInstance=null;
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
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getTaskInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getTaskInstance.doPost(request, response);
            LOGGER.info("Here we are executing valid requests...");
            validateDoPostValidResponse(response);
        }
    }
}

package api.v1.taskList;

import api.v1.model.TaskList;
import api.v1.model.User;
import api.v1.repo.TaskListRepository;
import api.v1.repo.UserRepository;
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
public class AddTaskListTest extends TaskListApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(AddTaskListTest.class);
    private static AddTaskList addTaskListInstance;
    private static UserRepository userRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validTaskLists=new ArrayList<String>();
    private static ArrayList<String> errorTaskLists=new ArrayList<String>();
    private static ArrayList<String> sampleUsers=new ArrayList<String>();


    /**
     * First create a new Instance of AddTaskList() object, then add new
     * taskList test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        addTaskListInstance=new AddTaskList();
        validTaskLists.add("0`0`TaskList 0`This is a valid TaskList.");
        validTaskLists.add("1`1`TaskList 1`This is a valid TaskList.");
        errorTaskLists.add("0`0` `This TaskList has no name.");
        errorTaskLists.add("1`1` `This TaskList has no name.");
        userRepository=DeleteTaskList.getUserRepository();

        sampleUsers.add("0`mikehedden@gmail.com`a681wo$dKo`[]`[]`[]`[0]");
        sampleUsers.add("1`kenlyon@gmail.com`Mouwkl87%qo`[]`[]`[]`[6,2,3,4,5,7]");
        for(User user: TaskListApiHelper.toUsers(sampleUsers))
            userRepository.add(user);


        for(JSONObject jsonObj: TaskListApiHelper.toJSONObjects(validTaskLists))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        for(JSONObject jsonObj: TaskListApiHelper.toJSONObjects(errorTaskLists))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, set pertinent objects to null. Also be sure to
     * remove all of the previously added tasks from the repository.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        TaskListRepository taskListRepository = addTaskListInstance.getTaskListRepository();
        for(TaskList taskList: toTaskLists(validTaskLists))
            taskListRepository.delete(taskList);
        for(User user: toUsers(sampleUsers))
            userRepository.delete(user);
        addTaskListInstance = null;
        validRequestList = null;
        errorRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to AddTaskList then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     *
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        LOGGER.debug("The length of validRequestList is: " + validRequestList.size());
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addTaskListInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        LOGGER.debug("The length of errorRequestList is: " + errorRequestList.size());
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addTaskListInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        // Verify that the User has been updated.
        for(User user: toUsers(sampleUsers))
            if(user.equals(userRepository.get(user))) {
                LOGGER.error("This user failed to update {}", user);
                fail("This user was not updated!");
            }else
                LOGGER.info("Updated User: {}", userRepository.get(user).toJson());
    }
}

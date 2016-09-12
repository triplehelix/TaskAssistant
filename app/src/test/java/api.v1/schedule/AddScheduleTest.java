package api.v1.schedule;

import api.v1.model.*;
import api.v1.repo.ScheduleRepository;
import api.v1.repo.TaskListRepository;
import api.v1.repo.TaskRepository;
import api.v1.repo.UserRepository;
import api.v1.repo.CategoryRepository;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.ArrayList;

import static org.springframework.test.util.AssertionErrors.fail;

/**
 * This class tests the AddSchedule Class.
 * @author kennethlyon
 */
public class AddScheduleTest extends ScheduleApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(AddScheduleTest.class);
    private static AddSchedule addScheduleInstance;
    private static CategoryRepository categoryRepository;
    private static ScheduleRepository scheduleRepository;
    private static TaskListRepository taskListRepository;
    private static TaskRepository taskRepository;
    private static UserRepository userRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validSchedules=new ArrayList<String>();
    private static ArrayList<String> errorSchedules=new ArrayList<String>();
    private static ArrayList<String> sampleTasks=new ArrayList<String>();
    private static ArrayList<String> sampleUsers=new ArrayList<String>();
    private static ArrayList<String> sampleCategories=new ArrayList<String>();
    private static ArrayList<String> sampleTaskLists=new ArrayList<String>();

    /**
     * First create a new Instance of AddSchedule() object, then add new
     * schedule test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        addScheduleInstance=new AddSchedule();
        categoryRepository=addScheduleInstance.getCategoryRepository();
        scheduleRepository=addScheduleInstance.getScheduleRepository();
        taskListRepository=addScheduleInstance.getTaskListRepository();
        taskRepository=addScheduleInstance.getTaskRepository();
        userRepository=addScheduleInstance.getUserRepository();

        sampleUsers.add("0`mikehedden@gmail.com`a681wo$dKo");
        sampleUsers.add("1`kenlyon@gmail.com`Mouwkl87%qo");
        for(User user: ScheduleApiHelper.toUsers(sampleUsers))
            userRepository.add(user);

        sampleTaskLists.add("0`0`Mike's TaskList.`This is Mike's  TaskList.");
        sampleTaskLists.add("1`1`Ken's  TaskList.`This is Kenny's TaskList.");
        for(TaskList taskList: ScheduleApiHelper.toTaskLists(sampleTaskLists))
            taskListRepository.add(taskList);

        sampleTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [0]  
        sampleTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [0]  
        sampleTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [1,2]
        sampleTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [1,2]
        sampleTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [3]  
        sampleTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [3]  
        sampleTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [4,5] 
        sampleTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW"); //   [4,5]
        for(Task task: ScheduleApiHelper.toTasks(sampleTasks))
            taskRepository.add(task);
 
        sampleCategories.add("0`0`Mikes work`This is for all of the work Mike does        "); // [0,1]
        sampleCategories.add("1`0`Mikes home`This is for all of the chores Mike never does"); // [2,3]
        sampleCategories.add("2`0`Mikes play`This is for Mike's recreational stuff        "); // [2,3]
        sampleCategories.add("3`1`Ken's work`This is for all of the work Ken never does.  "); // [4,5]
        sampleCategories.add("4`1`ken's home`This is for all of the chores Ken does.      "); // [6,7]
        sampleCategories.add("5`1`Ken's play`This is for the recreational stuff Ken does. "); // [6,7]
        for(Category category: ScheduleApiHelper.toCategories(sampleCategories))
            categoryRepository.add(category);
                                                                          // Tasks Categories  
        validSchedules.add("0`0`2016-06-28_18:00:00`2016-06-28_19:00:00`DAILY `[0,1]`[0]");  //exercize
        validSchedules.add("1`0`2016-07-03_09:00:00`2016-06-28_10:00:00`WEEKLY`[2,3]`[1]");  //church
        validSchedules.add("2`0`2016-06-28_09:00:00`2016-06-28_17:00:00`DAILY `[2,3]`[2]");  //workdays
        validSchedules.add("3`1`2016-06-30_18:00:00`2016-06-28_19:00:00`WEEKLY`[4,5]`[3]");  //date night.
        validSchedules.add("4`1`2016-07-03_16:00:00`2016-07-03_15:00:00`WEEKLY`[6,7]`[4]");  //Tacos!
        validSchedules.add("5`1`2016-07-03_16:00:00`2016-07-01_15:00:00`WEEKLY`[6,7]`[5]");  //Wings!
        for(JSONObject jsonObj: ScheduleApiHelper.toJSONObject(validSchedules))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        errorSchedules.add("0`1`2016-02-31_18:00:00`2016-02-31_19:00:00`DAILY   `[]`[]"); //Invalid date.
        errorSchedules.add("1`1`2016-07-03_09:00:00`2016-06-28_10:00:00`SUNDAYS `[]`[]"); //Invalid repeat type. 
        errorSchedules.add("2`1`2016-07-03_16:00:00`2016-07-03`WEEKLY           `[]`[]"); //Date is not well formed.
        errorSchedules.add("3`1`2016-06-30_18:00:00`2016-06-28_19:00:00`WEEKLY`[0]`[1]");  //Not permitted to access this Category.
        errorSchedules.add("4`1`2016-07-03_16:00:00`2016-07-03_15:00:00`WEEKLY`[6]`[0]");  //Not permitted to access this Task.
        errorSchedules.add("4`1`2016-07-03_16:00:00`2016-07-01_15:00:00`WEEKLY`[67]`[]");  //Task DNE.
        errorSchedules.add("4`1`2016-07-03_16:00:00`2016-07-01_15:00:00`WEEKLY`[]`[99]");  //Category DNE.
        for(JSONObject jsonObj: ScheduleApiHelper.toJSONObject(errorSchedules))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, remove schedules from the repository and set
     * pertinent objects to null.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(Category category: toCategories(sampleCategories))
            categoryRepository.delete(category);
        for(Schedule schedule: toSchedules(validSchedules))
            scheduleRepository.delete(schedule);
        for(TaskList taskList: toTaskLists(sampleTaskLists))
            taskListRepository.delete(taskList);
        for(Task task: toTasks(sampleTasks))
            taskRepository.delete(task);
        for(User user: toUsers(sampleUsers))
            userRepository.delete(user);

        verifyRepositoriesAreClean();
        addScheduleInstance=null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to AddSchedule then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addScheduleInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addScheduleInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        //Verify the Schedules have been added correctly:
        for(Schedule schedule: toSchedules(validSchedules))
            LOGGER.info(scheduleRepository.get(schedule).toJson());

        // Verify that the User has been updated.
        for(User user: toUsers(sampleUsers))
            if(user.equals(userRepository.get(user))) {
                LOGGER.error("This user failed to update {}", user);
                fail("This user was not updated!");
            }

        // Verify that the Tasks have been updated.
        for(Task task: toTasks(sampleTasks))
            if(task.equals(taskRepository.get(task))){
                LOGGER.error("This task failed to update {}", task);
                fail("This task was not updated!");
            }

        // Verify that the Categories have been updated.
        for(Category category: toCategories(sampleCategories))
            if(category.equals(categoryRepository.get(category))){
                LOGGER.error("This Category failed to update {}", category.toJson());
                fail("This task was not updated!");
            }
    }
}

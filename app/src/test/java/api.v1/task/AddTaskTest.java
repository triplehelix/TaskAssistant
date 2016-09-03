package api.v1.task;

import api.v1.model.*;
import api.v1.repo.*;
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
 * This class tests the AddTask Class
 * @author kennethlyon
 */
public class AddTaskTest extends TaskApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(AddTaskTest.class);

    private static AddTask addTaskInstance;
    private static TaskRepository taskRepository;
    private static UserRepository userRepository;
    private static CategoryRepository categoryRepository;
    private static ScheduleRepository scheduleRepository;
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> sampleCategories=new ArrayList<String>();
    private static ArrayList<String> sampleSchedules=new ArrayList<String>();
    private static ArrayList<String> sampleTaskLists=new ArrayList<String>();
    private static ArrayList<String> sampleUsers=new ArrayList<String>();
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
        addTaskInstance = new AddTask();
        taskListRepository=addTaskInstance.getTaskListRepository();
        scheduleRepository=addTaskInstance.getScheduleRepository();
        categoryRepository=addTaskInstance.getCategoryRepository();
        taskRepository=addTaskInstance.getTaskRepository();
        userRepository=addTaskInstance.getUserRepository();

        sampleUsers.add("0`mikehedden@gmail.com`a681wo$dKo");
        sampleUsers.add("1`kenlyon@gmail.com`Mouwkl87%qo");
        for(User user: TaskApiHelper.toUsers(sampleUsers))
            userRepository.add(user);

        sampleTaskLists.add("0`0`Mike's TaskList.`This is Mike's  TaskList.");
        sampleTaskLists.add("1`1`Ken's  TaskList.`This is Kenny's TaskList.");
        for(TaskList taskList: TaskApiHelper.toTaskLists(sampleTaskLists))
            taskListRepository.add(taskList);

        sampleSchedules.add("0`0`2016-06-28_18:00:00`2016-06-28_19:00:00`DAILY ");
        sampleSchedules.add("1`0`2016-07-03_09:00:00`2016-06-28_10:00:00`WEEKLY");
        sampleSchedules.add("2`0`2016-06-28_09:00:00`2016-06-28_17:00:00`DAILY ");
        sampleSchedules.add("3`1`2016-06-30_18:00:00`2016-06-28_19:00:00`WEEKLY");
        sampleSchedules.add("4`1`2016-07-03_16:00:00`2016-07-03_15:00:00`WEEKLY");
        sampleSchedules.add("5`1`2016-07-03_16:00:00`2016-07-01_15:00:00`WEEKLY");
        for(Schedule schedule: TaskApiHelper.toSchedules(sampleSchedules))
            scheduleRepository.add(schedule);

        sampleCategories.add("0`0`Mikes work`This is for all of the work Mike does        ");
        sampleCategories.add("1`0`Mikes home`This is for all of the chores Mike never does");
        sampleCategories.add("2`0`Mikes play`This is for Mike's recreational stuff        ");
        sampleCategories.add("3`1`Ken's work`This is for all of the work Ken never does.  ");
        sampleCategories.add("4`1`ken's home`This is for all of the chores Ken does.      ");
        sampleCategories.add("5`1`Ken's play`This is for the recreational stuff Ken does. ");
        for(Category category: TaskApiHelper.toCategories(sampleCategories))
            categoryRepository.add(category);

        validTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[0]`[0,1,2]");
        validTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[1]`[1,2]");
        validTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[2]`[1,2]");
        validTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[0]`[2,0]");
        validTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[3]`[3,4,5]");
        validTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[4]`[]");
        validTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[5]`[]");
        validTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[3]`[]");

        errorTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-02-31_00:00:00`NEW`[]`[]");  //  Invalid Date
        errorTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`YES `2020-05-31_00:00:00`NEW`[]`[]");  //  Invalid boolean value
        errorTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[5]`[]"); //  Cannot access category
        errorTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[]`[5]"); //  Cannot access schedule
        errorTasks.add("4`9`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[]`[]");  //  TaskList DNE
        errorTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[9]`[]"); //  Category DNE 
        errorTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[]`[9]"); //  Schedule DNE
        errorTasks.add("7`1`                   `TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[]`[]");  //  No name.


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

        for(TaskList taskList: toTaskLists(sampleTaskLists))
            taskListRepository.delete(taskList);

        for(Schedule schedule: toSchedules(sampleSchedules))
            scheduleRepository.delete(schedule);

        for(Category category: toCategories(sampleCategories))
            categoryRepository.delete(category);

        for(Task task: toTasks(validTasks))
            taskRepository.delete(task);
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
            addTaskInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addTaskInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        for(Schedule schedule: toSchedules(sampleSchedules))
            if(schedule.equals(scheduleRepository.get(schedule))) {
                LOGGER.error("This schedule failed to update {}", schedule.toJson());
                fail("This schedule was not updated!");
            }
            else
                LOGGER.info("Updated Schedule: {}", scheduleRepository.get(schedule).toJson());


        for(Category category: toCategories(sampleCategories))
            if(category.equals(categoryRepository.get(category))) {
                LOGGER.error("This category failed to update {}", category.toJson());
                fail("This category was not updated!");
            }
            else
                LOGGER.info("Updated Category: {}", categoryRepository.get(category).toJson());

        for(TaskList taskList: toTaskLists(sampleTaskLists))
            if(taskList.equals(taskListRepository.get(taskList))) {
                LOGGER.error("This taskList failed to update {}", taskList.toJson());
                fail("This taskList was not updated!");
            }
            else
                LOGGER.info("Updated TaskList: {}", taskListRepository.get(taskList).toJson());
    }
}

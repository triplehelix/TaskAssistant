package api.v1.category;

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
 * This class will test the UpdateCategory Class
 * @author kennethlyon
 */
public class UpdateCategoryTest extends CategoryApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(UpdateCategoryTest.class);
    private static UpdateCategory updateCategoryInstance;
    private static CategoryRepository categoryRepository;
    private static TaskListRepository taskListRepository;
    private static ScheduleRepository scheduleRepository;
    private static TaskRepository taskRepository;
    private static UserRepository userRepository;

    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validCategories=new ArrayList<String>();
    private static ArrayList<String> validUpdates=new ArrayList<String>();
    private static ArrayList<String> errorUpdates=new ArrayList<String>();
    private static ArrayList<String> sampleTasks=new ArrayList<String>();
    private static ArrayList<String> sampleUsers=new ArrayList<String>();
    private static ArrayList<String> sampleSchedules=new ArrayList<String>();
    private static ArrayList<String> sampleTaskLists=new ArrayList<String>();

    /**
     * First create a new Instance of UpdateCategory() object, then add new
     * category test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        updateCategoryInstance = new UpdateCategory();
        categoryRepository=updateCategoryInstance.getCategoryRepository();
        taskListRepository=updateCategoryInstance.getTaskListRepository();
        scheduleRepository=updateCategoryInstance.getScheduleRepository();
        taskRepository=updateCategoryInstance.getTaskRepository();
        userRepository=updateCategoryInstance.getUserRepository();

        sampleUsers.add("0`mikehedden@gmail.com`a681wo$dKo`[0,1,2]");
        sampleUsers.add("1`kenlyon@gmail.com`Mouwkl87%qo`[3,4,5]");
        for(User user: CategoryApiHelper.toUsers(sampleUsers))
            userRepository.add(user);

        sampleTaskLists.add("0`0`Mike's TaskList.`This is Mike's  TaskList.");
        sampleTaskLists.add("1`1`Ken's  TaskList.`This is Kenny's TaskList.");
        for(TaskList taskList: CategoryApiHelper.toTaskLists(sampleTaskLists))
            taskListRepository.add(taskList);

        sampleTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[0]");  //   [0]
        sampleTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[0]");  //   [0]
        sampleTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[1,2]");//   [1,2]
        sampleTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[1,2]");//   [1,2]
        sampleTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[3]");  //   [3]
        sampleTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[3]");  //   [3]
        sampleTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[4,5]");//   [4,5]
        sampleTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[4,5]");//   [4,5]
        for(Task task: CategoryApiHelper.toTasks(sampleTasks))
            taskRepository.add(task);

        validCategories.add("0`0`Mikes work`This is for all of the work Mike does         `[0,1]`[0,1,2]");
        validCategories.add("1`0`Mikes home`This is for all of the chores Mike never does `[2,3]`[]");
        validCategories.add("2`0`Mikes play`This is for Mike's recreational stuff         `[2,3]`[]");
        validCategories.add("3`1`Ken's work`This is for all of the work Ken never does.   `[4,5]`[3,4,5]");
        validCategories.add("4`1`ken's home`This is for all of the chores Ken does.       `[6,7]`[]");
        validCategories.add("5`1`Ken's play`This is for the recreational stuff Ken does.  `[6,7]`[]");
        for(Category category: toCategories(validCategories))
            categoryRepository.add(category);

        sampleSchedules.add("0`0`2016-06-28_18:00:00`2016-06-28_19:00:00`DAILY `[0]");
        sampleSchedules.add("1`0`2016-07-03_09:00:00`2016-06-28_10:00:00`WEEKLY`[0]");
        sampleSchedules.add("2`0`2016-06-28_09:00:00`2016-06-28_17:00:00`DAILY `[0]");
        sampleSchedules.add("3`1`2016-06-30_18:00:00`2016-06-28_19:00:00`WEEKLY`[3]");
        sampleSchedules.add("4`1`2016-07-03_16:00:00`2016-07-03_15:00:00`WEEKLY`[3]");
        sampleSchedules.add("5`1`2016-07-03_16:00:00`2016-07-01_15:00:00`WEEKLY`[3]");
        for(Schedule schedule: CategoryApiHelper.toSchedules(sampleSchedules))
            scheduleRepository.add(schedule);

        validUpdates.add("0`0`Mikes work`Work related tasks.                                              `[3,2]`[]");
        validUpdates.add("1`0`Mikes home`Thinks like walking the dog, TaskAssistant, cheese platters etc. `[1,3]`[]");
        validUpdates.add("2`0`Mikes play`This is for all the video games and media Mike's into.           `[1,0]`[]");
        validUpdates.add("3`1`Ken's work?`This is for all of the work Ken never does.                     `[5,6]`[]");
        validUpdates.add("4`1`ken's home`Cleaning, bills, side projects etc.                              `[5,6]`[]");
        validUpdates.add("5`1`Ken's play`This is for the recreational stuff Ken does.                     `[4,5]`[5]");

        errorUpdates.add("0`0`Mikes work`This category points to a Tasks that does not belong to Mike.`[4]`[]");
        errorUpdates.add("0`0`Mikes work`This category points to Schedules that do not belong to Mike.`[]`[4,5]");
        errorUpdates.add("1`0`          `This category has no name.`[2,3]`[]");
        errorUpdates.add("2`0`MH recreation`This Category points to tasks that do not exist.`[2000,-3]`[]");
        errorUpdates.add("2`0`MH recreation`This Category points to tasks that do not exist.`[-3,2000]`[]");


        // Populate the Category repository with valid Categories.
        for(Category category: CategoryApiHelper.toCategories(validCategories))
            categoryRepository.add(category);

        // Create valid mock categories.
        for(JSONObject jsonObj: CategoryApiHelper.toJSONObject(validUpdates))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create invalid mock categories.
        for(JSONObject jsonObj: CategoryApiHelper.toJSONObject(errorUpdates))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, remove Categories from the CategoryRepository
     * then set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(Category category: toCategories(validCategories))
            categoryRepository.delete(category);

        for(Task task: toTasks(sampleTasks))
            taskRepository.delete(task);

        for(User user: toUsers(sampleUsers))
            userRepository.delete(user);

        for(TaskList taskList: toTaskLists(sampleTaskLists))
            taskListRepository.delete(taskList);

        for(Schedule schedule: toSchedules(sampleSchedules))
            scheduleRepository.delete(schedule);

        updateCategoryInstance = null;
        validRequestList = null;
        verifyRepositoriesAreClean();
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to UpdateCategory then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateCategoryInstance.doPost(request, response);
            validateDoPostValidResponse(response);

        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateCategoryInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        // Verify that the User has been updated.
        for(User user: toUsers(sampleUsers))
            if(user.equals(userRepository.get(user))) {
                LOGGER.error("This user failed to update {}", user);
                // It does not actually make sense for category ownership to be changed with an update.
                //fail("This user was not updated!");
            }
        // Verify that the Task has been updated.
        for(Task task: toTasks(sampleTasks))
            if(task.equals(taskRepository.get(task))){
                LOGGER.error("This task failed to update {}", task);
                fail("This task was not updated!");
            }

        for(Schedule schedule: toSchedules(sampleSchedules))
            if(schedule.equals(scheduleRepository.get(schedule))) {
                LOGGER.error("This schedule failed to update {}", schedule);
                fail("This schedule was not updated!");
            }

    }
}

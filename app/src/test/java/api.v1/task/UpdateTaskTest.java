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
 * This class tests the output of the UpdateTask api.
 * Created by kennethlyon on 6/15/16.
 */
public class UpdateTaskTest extends TaskApiHelper{
    private Logger LOGGER = LoggerFactory.getLogger(UpdateTaskTest.class);
    private static UpdateTask updateTaskInstance;
    private static TaskRepository taskRepository;
    private static UserRepository userRepository;
    private static CategoryRepository categoryRepository;
    private static ScheduleRepository scheduleRepository;
    private static TaskListRepository taskListRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList<MockHttpServletRequest>();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList<MockHttpServletRequest>();
    private static ArrayList<String> sampleUsers=new ArrayList<String>();
    private static ArrayList<String> sampleTaskLists=new ArrayList<String>();
    private static ArrayList<String> sampleSchedules=new ArrayList<String>();
    private static ArrayList<String> sampleCategories=new ArrayList<String>();
    private static ArrayList<String> validTasks=new ArrayList<String>();
    private static ArrayList<String> validUpdates=new ArrayList<String>();
    private static ArrayList<String> errorUpdates=new ArrayList<String>();

    /**
     * First create a new Instance of AddTask and UpdateTask.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        updateTaskInstance = new UpdateTask();
        taskListRepository=updateTaskInstance.getTaskListRepository();
        scheduleRepository=updateTaskInstance.getScheduleRepository();
        categoryRepository=updateTaskInstance.getCategoryRepository();
        taskRepository=updateTaskInstance.getTaskRepository();
        userRepository=updateTaskInstance.getUserRepository();

       sampleUsers.add("0`mikehedden@gmail.com`a681wo$dKo");
       sampleUsers.add("1`kenlyon@gmail.com`Mouwkl87%qo");
        for(User user: TaskApiHelper.toUsers(sampleUsers))
            userRepository.add(user);

        sampleTaskLists.add("0`0`Mike's TaskList.`This is Mike's  TaskList.`[0,1,2,3]");
        sampleTaskLists.add("1`1`Ken's TaskList 1`This is Kenny's TaskList.`[4,5,6,7]");
        sampleTaskLists.add("2`1`Ken's TaskList 2`This is Kens other TaskList.`[]");
        LOGGER.debug("Starting at the very beginning. These are the TaskLists as they are when they are put in the repository:");
            for(TaskList taskList: TaskApiHelper.toTaskLists(sampleTaskLists))
                taskListRepository.add(taskList);

        sampleSchedules.add("0`0`2016-06-28T18:00:00.123Z`2016-06-28T19:00:00.123Z`DAILY  `[0,3]      ");
        sampleSchedules.add("1`0`2016-07-03T09:00:00.123Z`2016-06-28T10:00:00.123Z`WEEKLY `[0,1,2]    ");
        sampleSchedules.add("2`0`2016-06-28T09:00:00.123Z`2016-06-28T17:00:00.123Z`DAILY  `[0,1,2,3]  ");
        sampleSchedules.add("3`1`2016-06-30T18:00:00.123Z`2016-06-28T19:00:00.123Z`WEEKLY `[4]        ");
        sampleSchedules.add("4`1`2016-07-03T16:00:00.123Z`2016-07-03T15:00:00.123Z`WEEKLY `[4]        ");
        sampleSchedules.add("5`1`2016-07-03T16:00:00.123Z`2016-07-01T15:00:00.123Z`WEEKLY `[4]        ");
        for(Schedule schedule: TaskApiHelper.toSchedules(sampleSchedules))
            scheduleRepository.add(schedule);

        sampleCategories.add("0`0`Mikes work`This is for all of the work Mike does        `[0,3]");
        sampleCategories.add("1`0`Mikes home`This is for all of the chores Mike never does`[1]  ");
        sampleCategories.add("2`0`Mikes play`This is for Mike's recreational stuff        `[2]  ");
        sampleCategories.add("3`1`Ken's work`This is for all of the work Ken never does.  `[7,4]");
        sampleCategories.add("4`1`ken's home`This is for all of the chores Ken does.      `[5]  ");
        sampleCategories.add("5`1`Ken's play`This is for the recreational stuff Ken does. `[6]  ");
        for(Category category: TaskApiHelper.toCategories(sampleCategories))
            categoryRepository.add(category);

        errorUpdates.add(  "8`0`Mike's work task A`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW"); //Task does not exist.
        errorUpdates.add("-10`0`Mike's work task B`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[]`[]");
        errorUpdates.add(  "0`0`Mike's work task C`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[5]`[0,1,2]"); // Lacks permission to access Category.
        errorUpdates.add(  "1`0`Mike's work task D`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[1]`[1,5]");   // Lacks permission to access Schedule.
        //errorUpdates.add(  "2`0`Mike's home task E`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[2]`[1,2]");   // Invalid invested time.
        errorUpdates.add(  "3`0`Mike's home task F`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[0]`[200]");   // Schedule DNE.

                                                                                                                         // Category`Schedule
        validTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[0]`[0,1,2]");
        validTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[1]`[1,2]");
        validTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[2]`[1,2]");
        validTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[0]`[2,0]");
        validTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[3]`[3,4,5]");
        validTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[4]`[]");
        validTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[5]`[]");
        validTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[3]`[]");
        for(Task task: TaskApiHelper.toTasks(validTasks))
            taskRepository.add(task);

        validUpdates.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[0,1,2]`[]");
        validUpdates.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[]`[0,1,2]");
        validUpdates.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[]`[]");
        validUpdates.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[]`[]");
        validUpdates.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[5]`[]");
        validUpdates.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[]`[3,4,5]");
        validUpdates.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60001`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[3,4]`[]");
        validUpdates.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-30T00:00:00.123Z`NEW");

        // Create valid mock tasks.
        for(JSONObject jsonObj: TaskApiHelper.toJSONObjects(validUpdates))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create invalid mock tasks.
        for(JSONObject jsonObj: TaskApiHelper.toJSONObjects(errorUpdates))
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

        for(TaskList taskList: toTaskLists(sampleTaskLists))
            taskListRepository.delete(taskList);

        for(Schedule schedule: toSchedules(sampleSchedules))
            scheduleRepository.delete(schedule);

        for(User user: toUsers(sampleUsers))
            userRepository.delete(user);

        for(Category category: toCategories(sampleCategories))
            categoryRepository.delete(category);

        verifyRepositoriesAreClean();
        updateTaskInstance=null;
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
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateTaskInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateTaskInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        for(Schedule schedule: toSchedules(sampleSchedules))
            if(schedule.equals(scheduleRepository.get(schedule))) {
                LOGGER.error("This schedule failed to update {}", schedule.toJson());
                //fail("This schedule was not updated!");
            }
            else
                LOGGER.info("Updated Schedule: {}", scheduleRepository.get(schedule).toJson());


        for(Category category: toCategories(sampleCategories))
            if(category.equals(categoryRepository.get(category))) {
                LOGGER.error("This category failed to update {}", category.toJson());
                //fail("This category was not updated!");
            }
            else
                LOGGER.info("Updated Category: {}", categoryRepository.get(category).toJson());

        for(TaskList taskList: toTaskLists(sampleTaskLists))
            if(taskList.equals(taskListRepository.get(taskList))) {
                LOGGER.error("This taskList failed to update {}", taskList.toJson());
                //fail("This taskList was not updated!");
            }
            else
                LOGGER.info("Updated TaskList: {}", taskListRepository.get(taskList).toJson());
    }
}

package api.v1.taskList;

import api.v1.error.BusinessException;
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
 * @author kennethlyon on 20160711.
 */
public class DeleteTaskListTest extends TaskListApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(DeleteTaskListTest.class);
    private static DeleteTaskList deleteTaskListInstance;
    private static TaskListRepository taskListRepository;
    private static TaskRepository taskRepository;
    private static UserRepository userRepository;
    private static CategoryRepository categoryRepository;
    private static ScheduleRepository scheduleRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> sampleCategories = new ArrayList<String>();
    private static ArrayList<String> sampleSchedules = new ArrayList<String>();
    private static ArrayList<String> validTaskLists = new ArrayList<String>();
    private static ArrayList<String> errorTaskLists = new ArrayList<String>();
    private static ArrayList<String> sampleTasks = new ArrayList<String>();
    private static ArrayList<String> sampleUsers = new ArrayList<String>();

    /**
     * First create a new Instance of DeleteTaskList() object, then add new
     * taskList test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        //First instantiate DeleteTaskList, then fetch TaskListRepository.
        deleteTaskListInstance = new DeleteTaskList();
        taskListRepository = DeleteTaskList.getTaskListRepository();
        scheduleRepository = DeleteTaskList.getScheduleRepository();
        categoryRepository = DeleteTaskList.getCategoryRepository();
        taskRepository = DeleteTaskList.getTaskRepository();
        userRepository = DeleteTaskList.getUserRepository();

        sampleUsers.add("0`mikehedden@gmail.com`a681wo$dKo`[]`[]`[]`[0]");
        sampleUsers.add("1`kenlyon@gmail.com`Mouwkl87%qo`[]`[]`[]`[6,2,3,4,5,1,7]");
        for (User user : TaskListApiHelper.toUsers(sampleUsers))
            userRepository.add(user);

        sampleTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[0]`[0,1,2]");
        sampleTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[1]`[1,2]");
        sampleTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[2]`[1,2]");
        sampleTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[0]`[2,0]");
        sampleTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[3]`[3,4,5]");
        sampleTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[4]`[]");
        sampleTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[5]`[]");
        sampleTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31_00:00:00`NEW`[3]`[]");
        for (Task task : toTasks(sampleTasks))
            taskRepository.add(task);

        sampleSchedules.add("0`0`2016-06-28_18:00:00`2016-06-28_19:00:00`DAILY  `[0,3]      ");
        sampleSchedules.add("1`0`2016-07-03_09:00:00`2016-06-28_10:00:00`WEEKLY `[0,1,2]    ");
        sampleSchedules.add("2`0`2016-06-28_09:00:00`2016-06-28_17:00:00`DAILY  `[0,1,2,3]  ");
        sampleSchedules.add("3`1`2016-06-30_18:00:00`2016-06-28_19:00:00`WEEKLY `[4]        ");
        sampleSchedules.add("4`1`2016-07-03_16:00:00`2016-07-03_15:00:00`WEEKLY `[4]        ");
        sampleSchedules.add("5`1`2016-07-03_16:00:00`2016-07-01_15:00:00`WEEKLY `[4]        ");
        for (Schedule schedule : TaskListApiHelper.toSchedules(sampleSchedules))
            scheduleRepository.add(schedule);

        sampleCategories.add("0`0`Mikes work`This is for all of the work Mike does        `[0,3]");
        sampleCategories.add("1`0`Mikes home`This is for all of the chores Mike never does`[1]  ");
        sampleCategories.add("2`0`Mikes play`This is for Mike's recreational stuff        `[2]  ");
        sampleCategories.add("3`1`Ken's work`This is for all of the work Ken never does.  `[7,4]");
        sampleCategories.add("4`1`ken's home`This is for all of the chores Ken does.      `[5]  ");
        sampleCategories.add("5`1`Ken's play`This is for the recreational stuff Ken does. `[6]  ");
        for (Category category : TaskListApiHelper.toCategories(sampleCategories))
            categoryRepository.add(category);

        validTaskLists.add("0`0`TaskList 0`This is a valid TaskList.`[0,1,2,3]");
        validTaskLists.add("1`1`TaskList 1`This is a valid TaskList.`[4,5,6,7]");
        // Populate the TaskListRepository with valid TaskLists.
        for (TaskList taskList : TaskListApiHelper.toTaskLists(validTaskLists))
            taskListRepository.add(taskList);

        errorTaskLists.add("4`0`TaskList 4` There is no TaskList 4.`[0,1,2,3]");
        errorTaskLists.add("-1`1`TaskList -1 `-1 is an invalid object id.[]");

        //Finally, create Mock HTTP Servlet Requests.
        for (JSONObject jsonObj : TaskListApiHelper.toJSONObjects(validTaskLists))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        for (JSONObject jsonObj : TaskListApiHelper.toJSONObjects(errorTaskLists))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, set pertinent objects to null.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(User user: toUsers(sampleUsers))
            userRepository.delete(user);
        for(Category category: toCategories(sampleCategories))
            categoryRepository.delete(category);
        for(Schedule schedule: toSchedules(sampleSchedules))
            scheduleRepository.delete(schedule);
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
        for (Task task : toTasks(sampleTasks)) {
            boolean taskDeleted = false;
            try {
                taskRepository.delete(task);

            } catch (BusinessException e) {
                taskDeleted = true;
            }

            if (!taskDeleted) {
                LOGGER.error("The Task Object was never removed from the database! {}", task.toJson());
                fail("DeleteTaskList failed because of cleanup.");
            }
        }

        for (Schedule schedule : toSchedules(sampleSchedules))
            if (schedule.equals(scheduleRepository.get(schedule))) {
                LOGGER.error("This schedule failed to update {}", schedule.toJson());
                fail("This schedule was not updated!");
            } else
                LOGGER.info("Updated Schedule: {}", scheduleRepository.get(schedule).toJson());


        for (Category category : toCategories(sampleCategories))
            if (category.equals(categoryRepository.get(category))) {
                LOGGER.error("This category failed to update {}", category.toJson());
                fail("This category was not updated!");
            } else
                LOGGER.info("Updated Category: {}", categoryRepository.get(category).toJson());
        LOGGER.info("Tasks cleaned successfully.");
    }
}
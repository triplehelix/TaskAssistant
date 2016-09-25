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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.ArrayList;

import static org.springframework.test.util.AssertionErrors.fail;

/**
 * This class tests the DeleteSchedule Class.
 * @author kennethlyon
 */
public class DeleteScheduleTest extends ScheduleApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(AddScheduleTest.class);
    private static DeleteSchedule deleteScheduleInstance;
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
        deleteScheduleInstance=new DeleteSchedule();
        categoryRepository=deleteScheduleInstance.getCategoryRepository();
        scheduleRepository=deleteScheduleInstance.getScheduleRepository();
        taskListRepository=deleteScheduleInstance.getTaskListRepository();
        taskRepository=deleteScheduleInstance.getTaskRepository();
        userRepository=deleteScheduleInstance.getUserRepository();

        sampleUsers.add("0`mikehedden@gmail.com`a681wo$dKo`[0,1,2]");
        sampleUsers.add("1`kenlyon@gmail.com`Mou11wkl87%qo`[3,4,5]");
        for(User user: ScheduleApiHelper.toUsers(sampleUsers))
            userRepository.add(user);

        sampleTaskLists.add("0`0`Mike's TaskList.`This is Mike's  TaskList.");
        sampleTaskLists.add("1`1`Ken's  TaskList.`This is Kenny's TaskList.");
        for(TaskList taskList: ScheduleApiHelper.toTaskLists(sampleTaskLists))
            taskListRepository.add(taskList);

        sampleTasks.add("0`0`Mike's work task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[0]  "); //   [0]  
        sampleTasks.add("1`0`Mike's work task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[0]  "); //   [0]  
        sampleTasks.add("2`0`Mike's home task 01`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[1,2]"); //   [1,2]
        sampleTasks.add("3`0`Mike's home task 02`TRUE`This task belongs to Mike H.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[1,2]"); //   [1,2]
        sampleTasks.add("4`1`Ken's  work task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[3]  "); //   [3]
        sampleTasks.add("5`1`Ken's  work task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[3]  "); //   [3]  
        sampleTasks.add("6`1`Ken's  home task 01`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[4,5]"); //   [4,5] 
        sampleTasks.add("7`1`Ken's  home task 02`TRUE`This task belongs to  Kenny.`60000`100000`TRUE`2020-05-31T00:00:00.123Z`NEW`[4,5]"); //   [4,5]
        for(Task task: ScheduleApiHelper.toTasks(sampleTasks))
            taskRepository.add(task);
                                                                            // Tasks Categories  
        validSchedules.add("0`0`2016-06-28T18:00:00.123Z`2016-06-28T19:00:00.123Z`DAILY `[0,1]`[]");
        validSchedules.add("1`0`2016-07-03T09:00:00.123Z`2016-06-28T10:00:00.123Z`WEEKLY`[2,3]`[0,1,2]");
        validSchedules.add("2`0`2016-06-28T09:00:00.123Z`2016-06-28T17:00:00.123Z`DAILY `[2,3]`[]");
        validSchedules.add("3`1`2016-06-30T18:00:00.123Z`2016-06-28T19:00:00.123Z`WEEKLY`[4,5]`[]");
        validSchedules.add("4`1`2016-07-03T16:00:00.123Z`2016-07-03T15:00:00.123Z`WEEKLY`[6,7]`[3,4,5]");
        validSchedules.add("5`1`2016-07-03T16:00:00.123Z`2016-07-01T15:00:00.123Z`WEEKLY`[6,7]`[]");
        for(Schedule schedule: toSchedules(validSchedules))
            scheduleRepository.add(schedule);

        sampleCategories.add("0`0`Mikes work`This is for all of the work Mike does        `[1]");
        sampleCategories.add("1`0`Mikes home`This is for all of the chores Mike never does`[1]");
        sampleCategories.add("2`0`Mikes play`This is for Mike's recreational stuff        `[1]");
        sampleCategories.add("3`1`Ken's work`This is for all of the work Ken never does.  `[4]");
        sampleCategories.add("4`1`ken's home`This is for all of the chores Ken does.      `[4]");
        sampleCategories.add("5`1`Ken's play`This is for the recreational stuff Ken does. `[4]");
        for(Category category: ScheduleApiHelper.toCategories(sampleCategories))
            categoryRepository.add(category);

        /* Do not challenge the DeleteSchedule API with pointers to Tasks,
         * Users, Categories, that it does not have permission to edit. It
         * will not look at them anyway since it retrieves these references
         * from the repository.
         */
        errorSchedules.add( "-1`0`2016-07-03T09:00:00.123Z`2016-06-28T10:00:00.123Z`WEEKLY");
        errorSchedules.add(  "6`0`2016-06-28T09:00:00.123Z`2016-06-28T17:00:00.123Z`DAILY");
        errorSchedules.add("600`0`2016-06-28T09:00:00.123Z`2016-06-28T17:00:00.123Z`DAILY");

        for(JSONObject jsonObj: ScheduleApiHelper.toJSONObject(errorSchedules))
            errorRequestList.add(createDoPostMockRequest(jsonObj));

        for(JSONObject jsonObj: ScheduleApiHelper.toJSONObject(validSchedules))
            validRequestList.add(createDoPostMockRequest(jsonObj));

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
        for(TaskList taskList: toTaskLists(sampleTaskLists))
            taskListRepository.delete(taskList);
        for(Task task: toTasks(sampleTasks))
            taskRepository.delete(task);
        for(User user: toUsers(sampleUsers))
            userRepository.delete(user);

        verifyRepositoriesAreClean();
        deleteScheduleInstance=null;
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
            deleteScheduleInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            deleteScheduleInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        //Verify the Schedules have been added correctly:

        for(Schedule schedule: toSchedules(validSchedules)) {
            boolean fail=true;
            try {
                scheduleRepository.get(schedule).toJson();
            }catch(Exception e) {
                LOGGER.info("Schedule was removed successfully. {}", schedule.toJson());
                fail=false;
            }
            if(fail)
                fail("This Schedule was never removed from the ");
        }
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

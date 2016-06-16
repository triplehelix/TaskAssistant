package api.v1.task;

import api.v1.model.Task;
import api.v1.repo.TaskRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 6/15/16.
 */
public class UpdateTaskTest{
    private Logger LOGGER = LoggerFactory.getLogger(AddTaskTest.class);
    private static AddTask addTaskInstance;
    private static UpdateTask updateTaskInstance;
    TaskRepository taskRepository;
    private static ArrayList<MockHttpServletRequest> validUpdateTaskRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorUpdateTaskRequestList = new ArrayList();

    /**
     * First create a new Instance of AddTask and UpdateTask.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // 1. Start by crating AddTask and UpdateTask Objects.
        addTaskInstance = new AddTask();
        updateTaskInstance = new UpdateTask();
        taskRepository=updateTaskInstance.getTaskRepository();
        validUpdateTaskRequestList=new ArrayList<MockHttpServletRequest>();
        errorUpdateTaskRequestList=new ArrayList<MockHttpServletRequest>();

        // 2. Next, use AddTask to populate the TaskRepository with Valid Tasks.
        populateTaskRepositoryWithValidTasks();

        // 3. Fetch completed tasks from the repository.

        // 4. Create valid mock requests.

        // 5. Create invalid mock requests.



    }

    /**
     * This method populates the validUpdateTaskRequestList ArrayList
     * with valid MockHttpServletRequest objects.
     */
    private void createValidUpdateRequests() throws Exception{
        ArrayList<Task> repoTasks=fetchRepositoryTasks();

        //"0`Feed dog`TRUE`Dog eats kibble.`60000`0`TRUE`2020-05-28_08:31:01`NEW"
        repoTasks.get(0).setName("Give food to the fluff.");

        //"1`Create AddTask unit test`TRUE`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS"
        repoTasks.get(1).setImportant(false);

        //2`Buy beer`TRUE`Pick up some IPAs on the way home from work. Edit: Bill said he would pick up beers instead.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED"
        repoTasks.get(2).setNote("Bill is getting IPAs for the party.");

        //"3`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`3600000`0`FALSE`2016-06-12_08:00:00`DEFERRED"
        repoTasks.get(3).setEstimatedTime(1800000);

        //"4`Shave`FALSE`GF said I need to shave.`180000`0`TRUE`2016-06-09_19:00:00`DONE");
        repoTasks.get(4).setInvestedTime(90000);

        //"5`Robert')); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`TRUE`2016-06-09_19:00:00`NEW"
        repoTasks.get(5).setUrgent(false);

        //repoTasks.get(6).setDueDate();

        for(Task t: repoTasks)
            validUpdateTaskRequestList.add(createDoPostMockRequest(t));
        repoTasks=null;
    }

    /**
     * This method fetches an ArrayList of Task objects as they appear
     * in the TaskRepository.
     * @return
     * @throws Exception
     */
    private ArrayList<Task> fetchRepositoryTasks() throws Exception{
        ArrayList<Task> repoTaskArrayList=new ArrayList<Task>(9);
        for(int i=0;i<TaskTestHelper.validTasks.size(); i++){
            repoTaskArrayList.add(taskRepository.get(new Task(i)));
        }
        return repoTaskArrayList;
    }


    /**
     * After doPost runs, set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        addTaskInstance=null;
        updateTaskInstance=null;
        taskRepository=null;
        validUpdateTaskRequestList=null;
        errorUpdateTaskRequestList=null;
    }

    public void populateTaskRepositoryWithValidTasks() throws Exception{
        for(String stringTask: TaskTestHelper.validTasks){
            String[] taskElementArray=stringTask.split("`");
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id",                taskElementArray[0]);
            jsonObj.put("name",              taskElementArray[1]);
            jsonObj.put("important",         taskElementArray[2]);
            jsonObj.put("note",              taskElementArray[3]);
            jsonObj.put("estimatedTime",     taskElementArray[4]);
            jsonObj.put("investedTime",      taskElementArray[5]);
            jsonObj.put("urgent",            taskElementArray[6]);
            jsonObj.put("dueDate",           taskElementArray[7]);
            jsonObj.put("status",            taskElementArray[8]);
            LOGGER.info("Created request {}",jsonObj.toJSONString());
            request.addParameter("params", jsonObj.toJSONString());
            addTaskInstance.doPost(request, response);
        }
    }

    /**
     * This method expects that there be 9 existing valid requests
     * in the validUpdateTaskRequestList ArrayList. Each of these
     * tasks ought to have a valid mutatation applied to them.
     * @throws Exception
     */
    public void makeValidMutations()throws Exception{
        MockHttpServletRequest m = validUpdateTaskRequestList.get(0);
    //  m.remove("id");
    }


    /**
     * Create MockHttpServletRequests from an existing Task object.
     * @param task
     * @return
     */
    private MockHttpServletRequest createDoPostMockRequest(Task task) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",                task.getId());
        jsonObj.put("name",              task.getName());
        jsonObj.put("important",         task.getImportant());
        jsonObj.put("note",              task.getNote());
        jsonObj.put("estimatedTime",     task.getEstimatedTime());
        jsonObj.put("investedTime",      task.getInvestedTime());
        jsonObj.put("urgent",            task.getUrgent());
        jsonObj.put("dueDate",           task.getDueDate());
        jsonObj.put("status",            task.getStatus());
        LOGGER.info("Created request {}",jsonObj.toJSONString());
        request.addParameter("params", jsonObj.toJSONString());
        return request;
    }
}

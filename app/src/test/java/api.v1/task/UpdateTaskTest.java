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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static org.junit.Assert.fail;

/**
 * This class tests the output of the UpdateTask api.
 * Created by kennethlyon on 6/15/16.
 */
public class UpdateTaskTest{
    private Logger LOGGER = LoggerFactory.getLogger(AddTaskTest.class);
    private static AddTask addTaskInstance;
    private static UpdateTask updateTaskInstance;
    private TaskRepository taskRepository;
    private static ArrayList<MockHttpServletRequest> validUpdateTaskRequestList = new ArrayList<MockHttpServletRequest>();
    private static ArrayList<MockHttpServletRequest> errorUpdateTaskRequestList = new ArrayList<MockHttpServletRequest>();

    /**
     * First create a new Instance of AddTask and UpdateTask.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        LOGGER.debug("// 1. Start by crating AddTask and UpdateTask Objects.");
        addTaskInstance = new AddTask();
        updateTaskInstance = new UpdateTask();
        taskRepository=updateTaskInstance.getTaskRepository();
        validUpdateTaskRequestList=new ArrayList<MockHttpServletRequest>();
        errorUpdateTaskRequestList=new ArrayList<MockHttpServletRequest>();

        LOGGER.debug("// 2. Next, use AddTask to populate the TaskRepository with Valid Tasks.");
        populateTaskRepositoryWithValidTasks();

        LOGGER.debug("// 3. Fetch completed tasks from the repository and create valid mock requests.");
        createValidUpdateRequests();

        LOGGER.debug("// 5. Create invalid mock requests.");
        createErrorUpdateRequests();
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
        for (MockHttpServletRequest request : validUpdateTaskRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateTaskInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }
        for (MockHttpServletRequest request : errorUpdateTaskRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateTaskInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
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

    /**
     * Check to verify that valid UpdateTask doPost responses are indeed
     * valid and log results.
     *
     * @param response
     */
    private void validateDoPostValidResponse(MockHttpServletResponse response) {
        // Valid cases are: success or error if error then error
        String responseString;
        try{
            responseString = response.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error recorded while reading response", e);
            return;
        }
        LOGGER.info("response={}", responseString);
        JSONObject responseObj;
        try {
            responseObj = (JSONObject) new JSONParser().parse(responseString);
        } catch (ParseException e) {
            LOGGER.error("Parse Exception while parsing the response string", e);
            return;
        }
        if (null != responseObj){
            JSONObject error;
            if (null != (error=(JSONObject) responseObj.get("error"))){
                LOGGER.info("Response contained the error: code={}, msg={}", error.get("code"), error.get("msg"));
                fail("Received an error response on a valid input");
            }else{
                boolean success = (Boolean) responseObj.get("success");
                if (success){
                    LOGGER.info("Success value returned to the caller as: true ");
                }else{
                    fail("success value false in response and error value was not found");
                }
            }
        }else{
            fail("Response Object is empty");
        }
    }

    /**
     * Check to verify that invalid UpdateTask requests are caught
     * by UpdateTask().doPost and are received as error messages.
     * @param response
     */
    private void validateDoPostErrorResponse(MockHttpServletResponse response) {
        // Valid cases are: success or error if error then error
        String responseString;
        try{
            responseString = response.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error recorded while reading response", e);
            return;
        }
        LOGGER.info("response={}", responseString);
        JSONObject responseObj;
        try {
            responseObj = (JSONObject) new JSONParser().parse(responseString);
        } catch (ParseException e) {
            LOGGER.error("Parse Exception while parsing the response string", e);
            return;
        }
        if (null != responseObj){
            JSONObject error;
            if (null != (error=(JSONObject) responseObj.get("error"))){
                LOGGER.info("Response contained the error: code={}, msg={}", error.get("code"), error.get("msg"));
            }else{
                boolean success = (Boolean) responseObj.get("success");
                if (success){
                    LOGGER.info("Success value returned to the caller as: true ");
                    fail("Success value should not be present in case of invalid inputs.");
                }else{
                    fail("success value false in response and error value was not found");
                }
            }
        }else{
            fail("Response Object is empty");
        }
    }


    /**
     * This method populates the validUpdateTaskRequestList ArrayList with
     * valid MockHttpServletRequest objects. This method expects that there
     * be 9 existing valid requests in the validUpdateTaskRequestList
     * ArrayList. Each of these tasks ought to have a valid mutation applied
     * to them.
     * @throws Exception
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

        // "6`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW"
        //repoTasks.get(6).setDueDate();

        // "7`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED"
        //repoTasks.get(7).setStatus("DONE");

        // "8`Finish TaskAssistant`TRUE`APIs, Unit tests, services …`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS"
        //repoTasks.get(8).setStatus();

        for(Task t: repoTasks)
            validUpdateTaskRequestList.add(createDoPostMockRequest(t));
    }

    /**
     * This method populates the errorUpdateTaskRequestList ArrayList with
     * valid MockHttpServletRequest objects. This method uses the strings
     * from TaskTestHelper.errorTasks ArrayList. Each of these tasks ought
     * represent an invalid mutation to valid Task.
     *
     * @throws Exception
     */
    private void createErrorUpdateRequests() throws Exception {
        for(String stringTask: TaskTestHelper.errorTasks)
            errorUpdateTaskRequestList.add(createDoPostMockRequest(stringTask));
    }

    /**
     * This method fetches an ArrayList of Task objects as they appear
     * in the TaskRepository.
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
     * This method populates the task repository with valid tasks.
     * @throws Exception
     */
    public void populateTaskRepositoryWithValidTasks() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        for(String stringTask: TaskTestHelper.validTasks){
            request=createDoPostMockRequest(stringTask);
            addTaskInstance.doPost(request, response);
        }
    }

    /**
     * This class creates a mock http servlet request from a string
     * task object such as those found in TaskTestHelper.errorTasks.
     * @param stringTask
     * @return
     */
    private MockHttpServletRequest createDoPostMockRequest(String stringTask) {
        String[] taskElementArray=stringTask.split("`");
        MockHttpServletRequest request = new MockHttpServletRequest();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",              taskElementArray[0]);
        jsonObj.put("name",            taskElementArray[1]);
        jsonObj.put("important",       taskElementArray[2]);
        jsonObj.put("note",            taskElementArray[3]);
        jsonObj.put("estimatedTime",   taskElementArray[4]);
        jsonObj.put("investedTime",    taskElementArray[5]);
        jsonObj.put("urgent",          taskElementArray[6]);
        jsonObj.put("dueDate",         taskElementArray[7]);
        jsonObj.put("status",          taskElementArray[8]);
        LOGGER.info("Created request {}",jsonObj.toJSONString());
        request.addParameter("params", jsonObj.toJSONString());
        return request;
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


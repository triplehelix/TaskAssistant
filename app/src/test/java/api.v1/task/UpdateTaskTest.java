package api.v1.task;

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
    private Logger LOGGER = LoggerFactory.getLogger(UpdateTaskTest.class);
    private static AddTask addTaskInstance;
    private static UpdateTask updateTaskInstance;
    private static ArrayList<MockHttpServletRequest> validUpdateTaskRequestList = new ArrayList<MockHttpServletRequest>();
    private static ArrayList<MockHttpServletRequest> errorUpdateTaskRequestList = new ArrayList<MockHttpServletRequest>();

    /**
     * First create a new Instance of AddTask and UpdateTask.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        LOGGER.debug("");
        LOGGER.debug("*********** Starting @Before ***********");
        LOGGER.debug("// 1. Start by creating AddTask and UpdateTask Objects.");
        addTaskInstance = new AddTask();
        updateTaskInstance = new UpdateTask();
        validUpdateTaskRequestList=new ArrayList<MockHttpServletRequest>();
        errorUpdateTaskRequestList=new ArrayList<MockHttpServletRequest>();

        LOGGER.debug("// 2. Next, use AddTask to populate the TaskRepository with Valid Tasks.");
        populateTaskRepositoryWithValidTasks();



        /* Use the TaskTestHelper.validUpdates ArrayList to populate the
        * validUpdateTaskRequestList.
        */
        LOGGER.debug("// 3. Fetch completed tasks from the repository and create valid mock requests.");
        for(String stringTask: TaskTestHelper.validUpdates)
            validUpdateTaskRequestList.add(createDoPostMockRequest(stringTask));


        /* Use the TaskTestHelper.errorTasks ArrayList to populate the
        * errorUpdateTaskRequestList.
        */
        LOGGER.debug("// 4. Create invalid mock requests.");
        for(String stringTask: TaskTestHelper.errorUpdates)
            errorUpdateTaskRequestList.add(createDoPostMockRequest(stringTask));



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
        LOGGER.debug("// 5. Post valid mock requests...");
        for (MockHttpServletRequest request : validUpdateTaskRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateTaskInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }
        LOGGER.debug("// 6. Post error mock requests...");
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
        LOGGER.debug("@After: " + validUpdateTaskRequestList.size() + " " + errorUpdateTaskRequestList.size() + " ");
        addTaskInstance=null;
        updateTaskInstance=null;
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
     * This method populates the task repository with valid tasks.
     * @throws Exception
     */
    private void populateTaskRepositoryWithValidTasks() throws Exception{
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
        request.addParameter("params",   jsonObj.toJSONString());
        return request;
    }
}

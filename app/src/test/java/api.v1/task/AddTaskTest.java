package api.v1.task;

import api.v1.model.Task;
import api.v1.task.AddTask;
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
 * Created by mikeh on 4/17/2016.
 * This class will test the AddTask Class
 */
public class AddTaskTest {
    private Logger LOGGER = LoggerFactory.getLogger(AddTaskTest.class);
    private static AddTask addTaskInstance;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

    /**
     * First create a new Instance of AddTask() object, then add new
     * task test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        addTaskInstance = new AddTask();
        // Create valid mock tasks.
        for(String s: TaskTestHelper.validTasks)
            validRequestList.add(createDoPostMockRequest(s));

        // Create invalid mock tasks.
        for(String s: TaskTestHelper.errorTasks)
            errorRequestList.add(createDoPostMockRequest(s));
    }

    /**
     * After doPost runs, set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        addTaskInstance = null;
        validRequestList = null;
        errorRequestList = null;
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
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addTaskInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addTaskInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
    }

    /**
     * Check to verify that valid AddTask doPost responses are indeed
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
     * Check to verify that invalid AddTask requests are caught
     * by AddTask().doPost and are received as error messages.
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
     * Pass this method a String that can be interpreted as a well
     * formed Task. The string parameter should be delimitted by
     * backticks and
     * @param stringTask
     * @return
     */
    private MockHttpServletRequest createDoPostMockRequest(String stringTask){
        String[] taskElementArray=stringTask.split("`");
        MockHttpServletRequest request = new MockHttpServletRequest();
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
        return request;
    }
}

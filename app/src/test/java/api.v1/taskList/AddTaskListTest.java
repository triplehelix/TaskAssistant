package api.v1.taskList;

import api.v1.ApiTest;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.ArrayList;


/**
 * @author kennethlyon on 20160711.
 */
public class AddTaskListTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(AddTaskListTest.class);
    private static AddTaskList addTaskListInstance;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

    /**
     * First create a new Instance of AddTaskList() object, then add new
     * taskList test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * After doPost runs, set pertinent objects to null.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        addTaskListInstance = null;
        validRequestList = null;
        errorRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to AddTaskList then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     *
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
    }

    /**
     * Pass this method a json object to return a MockHttpServletRequest.
     *
     * @param jsonObj
     * @return
     */
    private MockHttpServletRequest createDoPostMockRequest(JSONObject jsonObj) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        LOGGER.info("Created request {}", jsonObj.toJSONString());
        request.addParameter("params", jsonObj.toJSONString());
        return request;

    }
}


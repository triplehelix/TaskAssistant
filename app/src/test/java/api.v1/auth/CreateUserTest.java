package api.v1.auth;

import api.v1.ApiTest;
import api.v1.model.User;
import api.v1.model.UserTest;
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
  * This class will test the CreateUser Class
  */
public class CreateUserTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(CreateUserTest.class);
    private static CreateUser createUserInstance;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

    /**
     * First create a new Instance of CreateUser() object, then add new
     * user test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        createUserInstance = new CreateUser();
        // Creating Valid requests
        validRequestList.add(createDoPostMockRequest("mikehedden@gmail.com", "a681wo$dKo"));
        validRequestList.add(createDoPostMockRequest("kenlyon@gmail.com","Mouwkl87%qo"));
        validRequestList.add(createDoPostMockRequest("kenlyon@test.com","e-W^2VmQ"));
        validRequestList.add(createDoPostMockRequest("fatsteaks@gmail.com","+%D5|x%b"));
        validRequestList.add(createDoPostMockRequest("yannisgreek@gmail.com","sy@UCL0_"));
        validRequestList.add(createDoPostMockRequest("rustypuppy@gmail.com","3Z^V)xkE"));
        validRequestList.add(createDoPostMockRequest("yo.momma.so.fat@gmail.com","6PnCK/?8"));
        validRequestList.add(createDoPostMockRequest("under_scores_rule@gmail.com","6~Zas2R*"));
        validRequestList.add(createDoPostMockRequest("test@mikehedden.gmail.com","i2@<uMtJ"));
        // Creating invalid requests
        errorRequestList.add(createDoPostMockRequest("mike", "password1"));
        errorRequestList.add(createDoPostMockRequest("mike@test.com", ""));
        errorRequestList.add(createDoPostMockRequest("mike@test@test.com", "aHouw8789"));
        errorRequestList.add(createDoPostMockRequest("houston@wehaveaproblem.com", "11111111111111111111"));
        errorRequestList.add(createDoPostMockRequest("toosimple@password.com", "ab1"));
    }

    /**
     * After doPost runs, remove Users from the repository, then set
     * pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(User user: UserTest.getValidTestUsersAsUsers())
            createUserInstance.getUserRepository().delete(user);
        createUserInstance = null;
        validRequestList = null;
        errorRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to CreateUser then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            createUserInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }
        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            createUserInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
    }

    /**
     * Create and return a new MockHttpServletRequest.
     * @param email
     * @param password
     * @return
     */
    private MockHttpServletRequest createDoPostMockRequest(String email, String password) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        JSONObject requestObj = new JSONObject();
        requestObj.put("email", email);
        requestObj.put("password", password);
        LOGGER.info("Created request {}", requestObj.toJSONString());
        request.addParameter("params", requestObj.toJSONString());
        return request;
    }
}
package api.v1.auth;

import api.v1.UnitTestHelper;
import api.v1.model.User;
import api.v1.repo.UserRepository;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.ArrayList;

/**
 * This class tests the AddUser Class.
 * @author kennethlyon
 */
public class CreateUserTest extends AuthApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(CreateUserTest.class);
    private static CreateUser createUserInstance;
    private static UserRepository userRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validUsers;
    private static ArrayList<String> errorUsers;

    /**
     * First create a new Instance of AddUser() object, then add new
     * user test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        createUserInstance = new CreateUser();
        userRepository=createUserInstance.getUserRepository();
        validUsers=new ArrayList<String>();
        validUsers.add("0`mikehedden@gmail.com`a681wo$dKo");
        validUsers.add("1`kenlyon@gmail.com`Mouwkl87%qo");
        validUsers.add("2`kenlyon@test.com`e-W^2VmQ");
        validUsers.add("3`fatsteaks@gmail.com`+%D5|x%b");
        validUsers.add("4`yannisgreek@gmail.com`sy@UCL0_");
        validUsers.add("5`rustypuppy@gmail.com`3Z^Vxk45ffr6bE");
        validUsers.add("6`yo.momma.so.fat@gmail.com`6PnCK/?8");
        validUsers.add("7`under_scores_rule@gmail.com`6~Zas2R*");
        validUsers.add("8`test@mikehedden.gmail.com`i2@<uMtJ");

        errorUsers=new ArrayList<String>();
        errorUsers.add("0`mike`password1");
        errorUsers.add("1`kenlyon@gmail.com`user-exists-19@38!5");
        errorUsers.add("2`mike@test@test.com`aHouw8789");
        errorUsers.add("3`houston@wehaveaproblem.com`11111111111111111111");
        errorUsers.add("4`toosimple@password.com`ab1");
        errorUsers.add("5`@com`ab1");


        // Create valid mock users.
        for(JSONObject jsonObj: AuthApiHelper.toJSONObject(validUsers))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create invalid mock users.
        for(JSONObject jsonObj: AuthApiHelper.toJSONObject(errorUsers))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, remove users from the repository and set
     * pertinent objects to null.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(User user: AuthApiHelper.toUsers(validUsers))
            userRepository.delete(user);
        createUserInstance = null;
        validRequestList = null;
        errorRequestList = null;
        verifyRepositoriesAreClean();
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to AddUser then forward responses to
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

        LOGGER.info("Verifying users were placed in the repository...");
        for(User user: UnitTestHelper.toUsers(validUsers)){
            LOGGER.info(userRepository.get(user).toJson());
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            createUserInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
    }
}

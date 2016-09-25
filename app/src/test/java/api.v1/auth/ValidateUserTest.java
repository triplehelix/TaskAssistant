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
 * This class tests the ValidateUser Class. To operate successfully,
 * it must use a User email and password to verify that a User exists.
 * Then it must return a user with arraylists of TaskIds.
 *
 * @author kennethlyon
 */
public class ValidateUserTest extends AuthApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(ValidateUserTest.class);
    private static ValidateUser validateUserInstance;
    private static UserRepository userRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validUsers;
    private static ArrayList<String> errorUsers;

    /**
     * First validate a new Instance of AddUser() object, then add new
     * user test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        validateUserInstance = new ValidateUser();
        userRepository=validateUserInstance.getUserRepository();

        validUsers=new ArrayList<String>();
        validUsers.add(     "0`mikehedden@gmail.com`a681wo$dKo` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(       "1`kenlyon@gmail.com`Mouwkl87%qo` [2,1,3,4,7] ` [20,30,40,50,60]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(           "2`kenlyon@test.com`e-W^2VmQ` [0,1,2,3,5] ` [30,40,50,60,70]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(        "3`fatsteaks@gmail.com`+%D5|x%b` [0,2,1,3,4] ` [40,50,60,70,80]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(      "4`yannisgreek@gmail.com`sy@UCL0_` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(       "5`rustypuppy@gmail.com`3Z^V$xkE` [2,1,3,4,7] ` [20,30,40,50,60]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(  "6`yo.momma.so.fat@gmail.com`6PnCK/?8` [0,1,2,3,5] ` [30,40,50,60,70]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add("7`under_scores_rule@gmail.com`6~Zas2R*` [0,2,1,3,4] ` [40,50,60,70,80]` [11,22,33,44,55]` [0,1,2]");
        validUsers.add(  "8`test@mikehedden.gmail.com`i2@<uMtJ` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]");

        // Add valid Users to the user repository.
	    for(User user: toUsers(validUsers))
	        userRepository.add(user);

        validUsers=null;
        validUsers=new ArrayList<String>();
        validUsers.add(     "0`mikehedden@gmail.com`a681wo$dKo");
        validUsers.add(       "1`kenlyon@gmail.com`Mouwkl87%qo");
        validUsers.add(           "2`kenlyon@test.com`e-W^2VmQ");
        validUsers.add(        "3`fatsteaks@gmail.com`+%D5|x%b");
        validUsers.add(      "4`yannisgreek@gmail.com`sy@UCL0_");
        validUsers.add(       "5`rustypuppy@gmail.com`3Z^V$xkE");
        validUsers.add(  "6`yo.momma.so.fat@gmail.com`6PnCK/?8");
        validUsers.add("7`under_scores_rule@gmail.com`6~Zas2R*");
        validUsers.add(  "8`test@mikehedden.gmail.com`i2@<uMtJ");


        errorUsers=new ArrayList<String>();
        errorUsers.add("0`mikehedden@gmail.com`Mouwkl87%qo"); // wrong password
        errorUsers.add(   "1`kenlyon@gmail.com`a681wo$dKo");  // wrong password
        errorUsers.add( "2`kenlyon@test.com`wrong_passwrd-VmQ");
        errorUsers.add(                     "3`mike`password1");
        errorUsers.add(                    "4`mike@test.com`a");
        errorUsers.add(       "5`mike@test@test.com`aHouw8789");


        // Validate valid mock users.
        for(JSONObject jsonObj: AuthApiHelper.toJSONObject(validUsers))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Validate invalid mock users.
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
        validateUserInstance = null;
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
            validateUserInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            validateUserInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
    }
}

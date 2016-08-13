package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class serves a a container for test case proto-Users.
 * Created by kennethlyon on 6/9/16.
 */
public class UserTest {
    private static Logger LOGGER = LoggerFactory.getLogger(UserTest.class);
    private static ArrayList<String> validUsers;
    private static ArrayList<String> errorUsers;
    private static ArrayList<String> validUpdates;
    private static ArrayList<String> errorUpdates;

    static {
        /* Add valid Users. Users fields are arranged in the order:
         * id, email, password
         */
        validUsers = new ArrayList<String>();
        validUpdates = new ArrayList<String>();
        errorUsers = new ArrayList<String>();
        errorUpdates = new ArrayList<String>();
        validUsers.add("0`mikehedden@gmail.com`a681wo$dKo");
        validUsers.add("1`kenlyon@gmail.com`Mouwkl87%qo");
        validUsers.add("2`kenlyon@test.com`e-W^2VmQ");
        validUsers.add("3`fatsteaks@gmail.com`+%D5|x%b");
        validUsers.add("4`yannisgreek@gmail.com`sy@UCL0_");
        validUsers.add("5`rustypuppy@gmail.com`3Z^V)xkE");
        validUsers.add("6`yo.momma.so.fat@gmail.com`6PnCK/?8");
        validUsers.add("7`under_scores_rule@gmail.com`6~Zas2R*");
        validUsers.add("8`test@mikehedden.gmail.com`i2@<uMtJ");

        errorUsers.add("10`mike`password1");
        errorUsers.add("11`mike@test.com` ");
        errorUsers.add("12`mike@test@test.com`aHouw8789");
        errorUsers.add("13`houston@wehaveaproblem.com`11111111111111111111");
        errorUsers.add("14`toosimple@password.com`ab1");

        errorUpdates.add("-10`mike`password1");
        errorUpdates.add("-1`mike@test.com` ");
        errorUpdates.add("102`mike@test@test.com`aHouw8789");
        errorUpdates.add("4294967297`houston@wehaveaproblem.com`11111111111111111111");
        errorUpdates.add("-204`toosimple@password.com`ab1");

        validUpdates.add("0`mikeHeddenIsAGirl@gmail.com`a681wo$dKo");
        validUpdates.add("1`ken.lyon@ymail.com`Mouwkl87%qo");
        validUpdates.add("2`ken_lyon@test.com`e-W^2VmQ");
        validUpdates.add("3`fatstakes@gmail.com`+%D5|x%b");
        validUpdates.add("4`yannisgreekfood@goodfood.com`sy@UCL0_");
        validUpdates.add("5`rusty_puppy@gmail.com`3Z^V)xkE");
        validUpdates.add("6`yo.momma.so.tall@gmail.com`6PnCK/?8");
        validUpdates.add("7`under_scores_rule@gmail.com`6~Zas2R*");
        validUpdates.add("8`test@mikehedden.gmail.com`i2@<uMtJ");
    }

    private static User toUser(String s) throws Exception{
        String[] UserElementArray = s.split("`");
        User User = new User();
        User.setId(Integer.parseInt(UserElementArray[0]));
        User.setEmail(UserElementArray[1]);
        User.setPassword(UserElementArray[2]);
        return User;
    }

    private static JSONObject toJson(String stringUser) {
        String[] UserElementArray = stringUser.split("`");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",       UserElementArray[0]);
        jsonObj.put("email",    UserElementArray[1]);
        jsonObj.put("password", UserElementArray[2]);
        LOGGER.info("Created request {}", jsonObj.toJSONString());
        return jsonObj;
    }

    /**
     * @throws Exception
     */
    @Test
    public void doTest() throws Exception {
        LOGGER.info("TESTING_VALID_USERS");
        for(String s: validUsers){
            UserTest.toUser(s);
            LOGGER.info("Valid User {}", toJson(s));
        }

        LOGGER.info("TESTING_VALID_UPDATES");
        for(String s: validUpdates){
            UserTest.toUser(s);
            LOGGER.info("Valid User {}", toJson(s));
        }

        LOGGER.info("TESTING_ERROR_USERS");
        for(String s: errorUsers){
            validateErrorUser(s);
            LOGGER.info("Error User {}", toJson(s));
        }

        LOGGER.info("TESTING_ERROR_UPDATES");
        for(String s: errorUpdates){
            validateErrorUser(s);
            LOGGER.info("Error User {}", toJson(s));
        }//*/
    }

    public void validateErrorUser(String s){
        boolean error=false;
        try{
            UserTest.toUser(s);
        }catch(Exception e){
            error=true;
            LOGGER.info("Invalid User returned error. " + e.getMessage(), e);
        }
        if(!error){
            fail("Success returned for invalid User: " + s);
        }
    }
}
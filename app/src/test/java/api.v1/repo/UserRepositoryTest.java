package api.v1.repo;
import api.v1.BaseRequestHandler;
import api.v1.error.BusinessException;
import api.v1.model.User;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.fail;

/**
 * Test the UserRepository. Here we ensure for User u that:
 *    1. UserRepository.get(u) is equal to u when u has just been added.
 *    2. UserRepository.get(u) is equal to u when u has just been Updated.
 *    3. delete(u) throws an error if u DNE.
 *    4. update(u) throws an error if u DNE.
 *    5. get(u) throws an error if u DNE.
 * Created by kennethlyon on 6/18/16.
 *
 */
public class UserRepositoryTest {

    private Logger LOGGER = LoggerFactory.getLogger(UserRepositoryTest.class);
    private UserRepository userRepository = new UserRepository();
    private static ArrayList<User> validAddUsers=new ArrayList<User>();
    private static ArrayList<User> errorAddUsers=new ArrayList<User>();
    private static ArrayList<User> validGetUsers=new ArrayList<User>();
    private static ArrayList<User> errorGetUsers=new ArrayList<User>();
    private static ArrayList<User> validUpdates=new ArrayList<User>();
    private static ArrayList<User> errorUpdates=new ArrayList<User>();

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepository();
        validAddUsers.add(toUser(     "0`mikehedden@gmail.com`a681wo$dKo` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]"));
        validAddUsers.add(toUser(       "1`kenlyon@gmail.com`Mouwkl87%qo` [2,1,3,4,7] ` [20,30,40,50,60]` [11,22,33,44,55]` [0,1,2]"));
        validAddUsers.add(toUser(           "2`kenlyon@test.com`e-W^2VmQ` [0,1,2,3,5] ` [30,40,50,60,70]` [11,22,33,44,55]` [0,1,2]"));
        validAddUsers.add(toUser(        "3`fatsteaks@gmail.com`+%D5|x%b` [0,2,1,3,4] ` [40,50,60,70,80]` [11,22,33,44,55]` [0,1,2]"));
        validAddUsers.add(toUser(      "4`yannisgreek@gmail.com`sy@UCL0_` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]"));
        validAddUsers.add(toUser(       "5`rustypuppy@gmail.com`3Z^V$xkE` [2,1,3,4,7] ` [20,30,40,50,60]` [11,22,33,44,55]` [0,1,2]"));
        validAddUsers.add(toUser(  "6`yo.momma.so.fat@gmail.com`6PnCK/?8` [0,1,2,3,5] ` [30,40,50,60,70]` [11,22,33,44,55]` [0,1,2]"));
        validAddUsers.add(toUser("7`under_scores_rule@gmail.com`6~Zas2R*` [0,2,1,3,4] ` [40,50,60,70,80]` [11,22,33,44,55]` [0,1,2]"));
        validAddUsers.add(toUser(  "8`test@mikehedden.gmail.com`i2@<uMtJ` [1,2,3,5,8] ` [10,20,30,40,50]` [11,22,33,44,55]` [0,1,2]"));



        errorAddUsers.add(toUser("0`mikehedden@gmail.com`a681wo$dKo")); // email already in use
        errorAddUsers.add(toUser("1`kenlyon@gmail.com`Mouwkl87%qo"));   // email already in use

        validGetUsers.add(toUser("0` `a681wo$dKo")); // Search by id
        validGetUsers.add(toUser("-1`kenlyon@gmail.com`Mouwkl87%qo"));  // Search by email
        validGetUsers.add(toUser("2``e-W^2VmQ"));       // Search by id
        validGetUsers.add(toUser("-1`fatsteaks@gmail.com`+%D5|x%b"));   // Search by email
        errorGetUsers.add(toUser("0`mikehedden@gmail.com`a681wo$dKo")); // wrong password
        errorGetUsers.add(toUser("1`kenlyon@gmail.com`Mouwkl87%qo"));   // wrong password

        validUpdates.add(toUser("0`mikehedden@gmail.com`Mouwkl87%qo"));
        validUpdates.add(toUser("1`ken.lyon@gmail.com`a681wo$dKo"));
        validUpdates.add(toUser("2`kenlyon@test.com`+%D5|x%b"));
        validUpdates.add(toUser("3`fatsteaks@gmail.com`02e-W^2VmQ19"));

        errorUpdates.add(toUser("-10`mikehedden@gmail.com`Mouwkl87%qo"));
        errorUpdates.add(toUser("5`ken.lyon@gmail.com`a681wo$dKo"));
        }

    private User toUser(String s) throws Exception{
        User user= new User();
        String[] elements = s.split("`");
        user.setId(Integer.parseInt(elements[0]));
        user.setEmail(elements[1]);
        user.setPassword(elements[2]);
        return user;
    }


    @Test
    public void test() throws Exception {
        testAdd();
        validateAddedUsers();
        testGet();
        testUpdate();
        validateUpdatedUsers();
        testDelete();
    }

    @After
    public void tearDown() throws Exception {
        HashMap<String, User> emailMap=userRepository.getEmailMap();
        HashMap<Integer, User> userMap=userRepository.getUserMap();

        Gson gson=new Gson();
        LOGGER.debug("Leviathan {}", gson.toJson(emailMap));
        LOGGER.debug("Leviathan {}", gson.toJson(userMap));

        userRepository = null;
        validUpdates=null;
        validAddUsers = null;
        validGetUsers = null;
        errorGetUsers = null;
    }

    /**
     * Compare the object just added to the repository to an
     * object from the repository with the same id.
     *
     * @throws Exception
     */
    private void validateAddedUsers() throws Exception {
        LOGGER.info("Validating added users... ");
        for (User uIn : validAddUsers) {
            if (!(userRepository.get(uIn).toJson()).equals(uIn.toJson()))
                LOGGER.error("These users are not identical!\n"+
                        userRepository.get(uIn).toJson() + "\n" +
                        uIn.toJson()
                );
            else
                LOGGER.info("These users are identical.\n"+
                        userRepository.get(uIn).toJson() + "\n" +
                        uIn.toJson()
                );
        }
        LOGGER.info("Added users are validated.\n\n");
    }

    /**
     * Compare the object just added to the repository to an
     * object from the repository with the same id.
     *
     * @throws Exception
     */
    private void validateUpdatedUsers() throws Exception {
        LOGGER.info("Validating updated users... ");
        for (User tIn : validUpdates) {
            if (!(userRepository.get(tIn).toJson()).equals(tIn.toJson()))
                LOGGER.error("These users are not identical!\n" +
                        userRepository.get(tIn).toJson() + "\n" +
                        tIn.toJson()
                );
            else
                LOGGER.info("These users are identical.\n"+
                        userRepository.get(tIn).toJson() + "\n" +
                        tIn.toJson()
                );
        }
        LOGGER.info("Updated users are validated.\n\n");
    }

    /**
     * Delete all of the users in the repository. Then, Attempt to
     * delete them again.
     */
    private void testDelete() {
        LOGGER.debug("Testing UserRepository.delete(User):");
        User u = null;
        try {
            for (int i=0;i<validAddUsers.size();i++) {
                u=validAddUsers.get(i);
                LOGGER.info("Deleting ", u.toJson());
                userRepository.delete(u);
            }
        } catch (Exception e) {
            LOGGER.error("delete user error. User not deleted {}", u.toJson(),e);
            fail("The user could not be deleted.");
        }

        boolean error=false;
        for (int i=0;i<validUpdates.size();i++){
            try {
                u=validUpdates.get(i);
                //    LOGGER.debug("Re: UserRepositoryTest.testDelete: Attempting to delete " + t.toJson());
                userRepository.delete(u);
            } catch (Exception e) {
                LOGGER.info("Delete User error {}", u.toJson());
                LOGGER.info(e.getMessage(), e);
                error = true;
            }
            if(!error)
                fail("Success returned for an invalid delete.");
        }
        LOGGER.info("\"testDelete\" finished. \n\n");
    }

    /**
     * Test the functionality of UserRepository.update(User) using
     * errorUpdates and validUpdates.
     */
    private void testUpdate() throws Exception {
        LOGGER.debug("Testing UserRepository.update(User):");
        for(User u: validUpdates) {
            userRepository.update(u);
            LOGGER.info("\t After update {}" + userRepository.get(u));
        }

        for(User u: errorUpdates){
            boolean error=false;
            try {
                userRepository.update(u);
            } catch (BusinessException e) {
                LOGGER.info("Update User error {}", u.toJson());
                LOGGER.info(e.getMessage(), e);
                error = true;
            }
            if(!error)
                fail("Success returned for an invalid update.");
        }
        LOGGER.info("\"testUpdate\" finished.\n\n");
    }

    private void testAdd() throws Exception {
        LOGGER.debug("Testing UserRepository.add(User):");
        for(User u: validAddUsers)
            userRepository.add(u);

        LOGGER.info("Test add for error users: ");
        for(User u: errorAddUsers) {
            boolean error = false;
            try {
                userRepository.add(u);
            } catch (BusinessException e) {
                LOGGER.info("Add User error {}", u.toJson());
                LOGGER.info(e.getMessage(), e);
                error = true;
            }
            if (!error)
                fail("Success returned for an invalid update.");
        }
        LOGGER.info("\"testAdd\" finished. \n\n");
    }

    private void testGet() throws Exception {
        LOGGER.debug("Testing UserRepository.get(User):");
        for(User u: validGetUsers){
            LOGGER.info("Received User: {} from {}", userRepository.get(u).toJson(), u.toJson());
        }
        LOGGER.info("\"testGet\" finished.\n\n");
    }
}

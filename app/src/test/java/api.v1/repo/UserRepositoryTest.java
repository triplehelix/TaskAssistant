package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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
        validAddUsers.add(toUser("0`mikehedden@gmail.com`a681wo$dKo"));
        validAddUsers.add(toUser("1`kenlyon@gmail.com`Mouwkl87%qo"));
        validAddUsers.add(toUser("2`kenlyon@test.com`e-W^2VmQ"));
        validAddUsers.add(toUser("3`fatsteaks@gmail.com`+%D5|x%b"));

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
        //First delete them all.
        User u = null;
        try {
            for (int i=0;i<validUpdates.size();i++) {
                u=validUpdates.get(i);
                userRepository.delete(u);
                LOGGER.info("Deleting ");
            }
        } catch (Exception e) {
            LOGGER.error("delete user error. User not deleted {}", u.toJson());
            fail("The user could not be deleted.");
        }
        //LOGGER.debug("Re: UserRepositoryTest.testDelete: ");
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
        LOGGER.info("Test update: ");
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
        LOGGER.info("Test add for valid users: ");
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
        LOGGER.info("Test get: ");
        for(User u: validGetUsers){
            LOGGER.info("Received User: {} from {}", userRepository.get(u).toJson(), u.toJson());
        }
        LOGGER.info("\"testGet\" finished.\n\n");
    }

}

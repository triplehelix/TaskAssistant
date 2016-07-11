package api.v1.repo;
import api.v1.model.User;
import api.v1.model.UserTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.fail;

/**
 * Test the UserRepository. Here we ensure that for User u:
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
    private static ArrayList<User> validUsers = new ArrayList<User>();
    private static ArrayList<User> validUpdates = new ArrayList<User>();

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepository();
        validUsers = UserTest.getValidTestUsersAsUsers();
        validUpdates = UserTest.getValidTestUsersUpdatesAsUsers();
    }

    @Test
    public void test() throws Exception {
        for (User u : validUsers) {
            LOGGER.info("Add valid user {}", u.toJson());
            userRepository.add(u);
        }
        validateAddedUsers();

        for (User u : validUpdates) {
            LOGGER.info("Add valid user {}", u.toJson());
            userRepository.update(u);
        }
        validateUpdatedUsers();
        testDelete();
        testUpdate();
    }

    @After
    public void tearDown() throws Exception {
        userRepository = null;
        validUpdates=null;
        validUsers = null;
    }

    /**
     * Compare the object just added to the repository to an
     * object from the repository with the same id.
     *
     * @throws Exception
     */
    private void validateAddedUsers() throws Exception {
        for (User uIn : validUsers) {
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
    }

    /**
     * Compare the object just added to the repository to an
     * object from the repository with the same id.
     *
     * @throws Exception
     */
    private void validateUpdatedUsers() throws Exception {
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
                LOGGER.error("Delete User error \n\t{}", u.toJson());
                LOGGER.error(e.getMessage(), e);
                error = true;
            }
            if(!error)
                fail("Success returned for an invalid delete.");
        }
    }

    private void testUpdate(){
        LOGGER.debug("Re: UserRepositoryTest.testUpdate: ");
        boolean error=false;
        User u=null;
        for (int i=0;i<validUpdates.size();i++){
            try {
                u=validUpdates.get(i);
                LOGGER.debug("Re: UserRepositoryTest.testDelete: Attempting to delete " + u.toJson());
                userRepository.update(u);
            } catch (Exception e) {
                LOGGER.error("Update User error \n\t{}", u.toJson());
                LOGGER.error(e.getMessage(), e);
                error = true;
            }
            if(!error)
                fail("Success returned for an invalid update.");
        }
    }
}

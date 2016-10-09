package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import api.v1.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * This is the ProtoUserRepository. This class does not yet
 * interact with a database.
 */
public class UserRepository implements Repository<User> {
    private static Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);
    private HashMap<Integer, User> userMap;
    private HashMap<String, User> emailMap;

    /**
     * Create a new instance of a repository.
     */
    public UserRepository() {
        userMap = new HashMap<Integer, User>();
        emailMap = new HashMap<String, User>();
    }

    /**
     * First discover a user id that has not been used. Then copy the incoming
     * user fields into the new user.
     *
     * @param u
     * @throws BusinessException
     * @throws SystemException
     */
    public User add(User u) throws BusinessException, SystemException {
        // First, we make sure that the user DNE. Else throw BusinessException
        if (emailMap.containsKey(u.getEmail()))
            throw new BusinessException("Error. This User email already exits! {" + u.getEmail() + "}", Error.valueOf("CREATE_USER_ERROR_USER_EXISTS"));
        int userId = 0;
        while (userMap.containsKey(userId))
            userId++;
        u.setId(userId);
        userMap.put(userId, u);
        emailMap.put(u.getEmail(), u);
        return new User(u);
    }

    /**
     * Use a User object to fetch a corresponding complete User. First try
     * to fetch the user by the use email. If that fails, try to
     *
     * @param u
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public User get(User u) throws BusinessException, SystemException {
        try {
            return new User(getUserByEmail(u));
        } catch (BusinessException e) {
            return new User(getUserById(u));
        }
    }

    /**
     * Use the id field to fetch the specified User.
     *
     * @param u
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private User getUserById(User u) throws BusinessException, SystemException {
        if (userMap.containsKey(u.getId()))
            return userMap.get(u.getId());
        else
            throw new BusinessException(" User id not found. Id=" + u.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     * Use a user email to fetch the specified User.
     *
     * @param u
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private User getUserByEmail(User u) throws BusinessException, SystemException {
        if (emailMap.containsKey(u.getEmail()))
            return emailMap.get(u.getEmail());
        else
            throw new BusinessException(" User email not found {" + u.getEmail() + "}", Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     * @param u
     * @throws BusinessException
     * @throws SystemException
     */
    public void update(User u) throws BusinessException, SystemException {
        String oldEmail = "";
        if (userMap.containsKey(u.getId()))
            oldEmail = userMap.get(u.getId()).getEmail();
        else
            throw new BusinessException(" User not found. ID=" + u.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));

        if (!oldEmail.equals(u.getEmail()) && emailMap.containsKey(u.getEmail()))
            throw new BusinessException(" Updated email already taken. ID=" + u.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
        userMap.remove(u.getId());
        emailMap.remove(oldEmail);
        userMap.put(u.getId(), u);
        emailMap.put(u.getEmail(), u);
    }

    /**
     * Deletes the provided user. Note that the user passed as a parameter
     * must have both the proper userId and Email to successfully remove a
     * User.
     *
     * @param u
     * @throws BusinessException
     * @throws SystemException
     */
    public void delete(User u) throws BusinessException, SystemException {
        // First, throw an exception if the User cannot be found in
        // either mapping:
        if (u.getEmail()==null)
            throw new BusinessException("A valid user email must be provided.", Error.valueOf("NO_SUCH_OBJECT_ERROR"));

        if (!userMap.containsKey(u.getId()))
            throw new BusinessException(" User not found. ID=" + u.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));

        if (!emailMap.containsKey(u.getEmail()))
            throw new BusinessException(" User not found. email=" + u.getEmail(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));

        if(!emailMap.get(u.getEmail()).equals(userMap.get(u.getId())))
            throw new BusinessException("The user id and email do not point to the same user! email=" + u.getEmail() + "  ID=" + u.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
        //*/
        //Then attempt to remove the mapping.
        removeFromEmailMap(u);
        removeFromUserMap(u);
    }

    /**
     * User Iterator to remove an email-User HashMap entry.
     * @param u
     */
    private void removeFromEmailMap(User u) {
        Iterator <Map.Entry<String, User>> iterator=emailMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, User> entry = iterator.next();
            if(entry.getKey().equals(u.getEmail())){
                iterator.remove();
            }
        }
    }

    /**
     * * User Iterator to remove an id-User HashMap entry.
     * @param u
     */
    private void removeFromUserMap(User u) {
        Iterator <Map.Entry<Integer, User>> iterator=userMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer, User> entry = iterator.next();
            if(entry.getKey().equals(u.getId())){
                iterator.remove();
            }
        }
    }
}

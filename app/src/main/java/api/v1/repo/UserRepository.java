package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * This is the ProtoUserRepository. This class does not yet
 * interact with a database.
 */
public class UserRepository implements Repository<User>{
    private static Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);
    private HashMap<Integer, User> userMap;
    private HashMap<String, User> emailMap;

    /**
     * Create a new instance of a repository.
     */
    public UserRepository(){
        userMap=new HashMap<Integer, User>();
    }

    /**
     * First discover a user id that has not been used. Then copy the incoming
     * user fields into the new user.
     * @param u
     * @throws BusinessException
     * @throws SystemException
     */
    public void add(User u) throws BusinessException, SystemException{
        LOGGER.debug("ADDING: " + u.toJson());
	    // First, we make sure that the user DNE. Else throw BusinessException
        int userId=0;
        while(userMap.containsKey(userId))
            userId++;
        u.setId(userId);
        userMap.put(userId, u);
        emailMap.put(u.getEmail(), u);
    }

    /**
     * Use a User object to fetch a corresponding complete User.
     * @param u
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public User get(User u)throws BusinessException, SystemException{
        if(u.getId()==-1)
            return getUserByEmail(u);
        else
            return getUserById(u);
    }


    /**
     * Use the id field to fetch the specified User.
     * @param u
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private User getUserById(User u)throws BusinessException, SystemException{
        if(userMap.containsKey(u.getId()))
            return userMap.get(u.getId());
        else
            throw new BusinessException(" User id not found. Id=" + u.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }
    /**
     * * Use a user email to fetch the specified User.
     * @param u
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private User getUserByEmail(User u)throws BusinessException, SystemException{
        if(userMap.containsKey(u.getId()))
            return userMap.get(u.getId());
        else
            throw new BusinessException(" User email not found {}" + u.getEmail(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     *
     * @param u
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(User u) throws BusinessException, SystemException{
        LOGGER.debug(u.toJson());
        // First, delete the user:
        this.delete(u);
        // Then add the new user:
        this.add(u);
	}

    /**
     * Deletes the provided user.
     *
     * @param u
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(User u) throws BusinessException, SystemException{
        if(userMap.containsKey(u.getId())){
            userMap.remove(u.getId());
        }
        else
            throw new BusinessException(" User not found. ID=" + u.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }
}

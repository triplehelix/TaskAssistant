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


    /**
     * Create a new instance of a repository.
     */
    public UserRepository(){
        userMap=new HashMap<Integer, User>();
    }

    /**
     * First discover a user id that has not been used. Then copy the incoming
     * user fields into the new user.
     * @param foobar
     * @throws BusinessException
     * @throws SystemException
     */
    public void add(User foobar) throws BusinessException, SystemException{
	// First, we make sure that the user DNE. Else throw BusinessException
        int userId=0;
        while(userMap.containsKey(userId))
            userId++;
        userMap.put(userId, foobar);
    }

    /**
     * @param foobar
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public User get(User foobar)throws BusinessException, SystemException{
        if(userMap.containsKey(foobar.getId()))
            return userMap.get(foobar.getId());
        else
            throw new BusinessException(" User not found. ", Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     *
     * @param foobar
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(User foobar) throws BusinessException, SystemException{
        // First, delete the user:
        this.delete(foobar);
        // Then add the new user:
        this.add(foobar);
	}

    /**
     * Deletes the provided user.
     *
     * @param foobar
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(User foobar) throws BusinessException, SystemException{
        if(userMap.containsKey(foobar.getId())){
            userMap.remove(foobar.getId());
        }
        else
            throw new BusinessException(" User not found. ", Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }
}

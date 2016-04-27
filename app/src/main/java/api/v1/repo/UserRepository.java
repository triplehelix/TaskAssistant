package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import java.sql.SQLException;
import java.util.HashMap;

import api.v1.model.User;

/**
 * This is the "ProtoUserRepository". Don't let the actual class
 * name fool you, it is definitely incomplete. Namely, the feature
 * it lacks is that it does not interact with a database, and as a
 * consequence, it does not really have a solid method of ensuring
 * that User IDs are not colliding. At present, new Users are given
 * a user id that is equal to the length of the userMap.
 */
public class UserRepository implements Repository<User>{

	private HashMap<Integer, User> userMap;
	/**
	 *  Add a user to the user-map/database. If the user already exists,
	 *  throw an exception.
	 *
	 * @param u
	 * @throws SystemException
     */
	public void add(User u) throws SystemException, BusinessException{
		// First, we make sure that the user DNE. Else throw BusinessException
        if(userDNE(u))
            userMap.put(userMap.size(), u);

        // Then, if the User DE add user. Now only SystemExceptions can be
        // Thrown.

    }

	/**
	 * Returns a User with the same User ID
	 * @param u
	 * @return
	 * @throws SystemException
     */
	public User get(User u)throws SystemException, BusinessException{
        if(userMap.containsKey(u))
            return userMap.get(u);
        else
            throw new BusinessException(" User not found. ", Error.valueOf(""));
    }

	public void update(User u) throws SystemException{
		// TODO Auto-generated method stub
	}

	public void delete(User u) throws SystemException{
		// TODO Auto-generated method stub
	}


    /**
     * The UserRepository Constructor is created by a static reference from
     * the BaseAuthRequestHandler. All it has to do is create a new HashMap.
     */
    public UserRepository(){
        userMap=new HashMap<Integer, User>();
    }

    /**
     * Verify that the user Does not exist. Later
     * @param u
     * @return
     */
    private boolean userDNE(User u){
        if(userMap.containsKey(u))
            return false;
        else
            return true;
    }
}

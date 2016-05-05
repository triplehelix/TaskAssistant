package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
//import java.sql.SQLException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.User;

/**
 *
 * This is the ProtoUserRepository. This class does not yet
 * interact with a database.
 */
public class UserRepository implements Repository<User>{

	private HashMap<Integer, User> userMap;

	/**
	 *  Add a user to the user-map/database. If the user already exists,
	 *  throw an exception.
	 *
     * @param u
     * @throws BusinessException
     * @throws SystemException
     */
    public void add(User u) throws BusinessException, SystemException{
	// First, we make sure that the user DNE. Else throw BusinessException
        if(userDNE(u))
            userMap.put(userMap.size(), u);

        // Then, if the User DE add user. Now only BusinessExceptions can be
        // Thrown.

    }

	/**
	 * Returns a User with the same User ID.
	 *
     * @param u
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public User get(User u)throws BusinessException, SystemException{
        if(userMap.containsKey(u))
            return userMap.get(u);
        else
            throw new BusinessException(" User not found. ", Error.valueOf("")); //TODO specifty error.
    }


    /**
     *
     * @param u
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(User u) throws BusinessException, SystemException{
        // First, delete the user:
        this.delete(u);
        // Then add the new u:
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
	    userMap.remove(userMap.get(u.getId()));
	}


    /**
     * The UserRepository Constructor is created by a static reference from
     * the BaseAuthRequestHandler. This constructor creates the userMap.
     */
    public UserRepository(){
        userMap=new HashMap<Integer, User>();
    }

    /**
     * Verify that the user Does not exist. 
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

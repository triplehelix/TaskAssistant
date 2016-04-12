package api.v1.repo;

import java.sql.SQLException;
import java.util.HashMap;

import api.v1.model.User;

public class UserRepository implements Repository<User>{

	private HashMap<Integer, User> userMap;

	/**
	 *  Add a user to the user-map/database. If the user already exists,
	 *  throw an exception.
	 *
	 * @param u
	 * @throws SQLException
     */
	public void add(User u) throws SQLException{
		String errMsg="User already exits.";
		if(!userMap.containsKey(u))
			userMap.put(u.getId(),u);
		else
			throw new SQLException();
	}

	/**
	 * Returns a User with the same User ID
	 * @param u
	 * @return
	 * @throws SQLException
     */
	public User get(User u)throws SQLException{
		if(userMap.containsKey(u))
			return userMap.get(u);
		else
			return deepSearch(u);
	}

	/**
	 * Find a User from the database with the same User ID.
	 * @param u
	 * @return
     */
	private User deepSearch(User u) throws SQLException{
		//TODO actually check the database for such an item.
		if(true)
			throw new SQLException("User does not exist");
		return null;
	}

	public void update(User u) {
		// TODO Auto-generated method stub	
	}

	public void delete(User u) {
		// TODO Auto-generated method stub
	}
}

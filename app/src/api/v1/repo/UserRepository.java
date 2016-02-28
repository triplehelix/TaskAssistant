package api.v1.repo;

import api.v1.model.User;

public class UserRepository implements Repository<User>{

	/**
	 *  Add a user to the database. If the user already exists.
	 *  Throw an exception.
	 */
	public void add(User u) throws Exception{
		// TODO Auto-generated method stub
	}

	/**
	 * This method must return an instance of a User, that is identical
	 * to user that it was given. Since the user that it returns comes 
	 * from the database, it cannot have a null id, email, or password.
	 * 
	 * Incoming methods are expected to either have a known id, OR a 
	 * known user name and password.
	 * 
	 */
	public User get(User u)throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	public void update(User u) {
		// TODO Auto-generated method stub	
	}

	public void delete(User u) {
		// TODO Auto-generated method stub
	}
}

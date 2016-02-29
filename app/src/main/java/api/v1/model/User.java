package api.v1.model;

public class User {
	private int id;
	private String email;
	private String password;
	
	/**
	 * Create a User object with a preexisting id. Such User objects
	 * must come from the database.
	 * @param id
	 */
	public User(int id){
		this.id = id;
	}
	
	/**
	 * Create an empty User object.
	 */
	public User(){
	}
		
	public int getId() {
		return id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}

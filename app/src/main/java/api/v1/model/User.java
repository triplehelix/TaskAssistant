package api.v1.model;

public class User {
	private int id;
	private String email;
	private String password;
	
	/**
	 * Create a new User w/o an user id. Users created without an id
     * are assigned an id of -9.
	 */
	public User(){
        this.id=-9;
	}

	public int getId() {return id;}
	public void setId(int id){
		this.id=id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
	 this.password=password;
	}
    public String getPassword(){
        return this.password;
    }
}

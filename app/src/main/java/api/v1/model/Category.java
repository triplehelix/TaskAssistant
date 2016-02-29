package api.v1.model;

public class Category {
	private int id;
	private String name;
	private String description;
	
	
	/**
	 * Create a new category class with an id that is already defined. 
	 * @param id
	 */
	public Category(int id){
		
	}
	
	/**
	 * Create an new empty Category object. 
	 */
	public Category(){		
	}


	public int getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	/**
	 * String name must be < 128 chars.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Description must be < 1024 chars.
	 * @return
	 */
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
}

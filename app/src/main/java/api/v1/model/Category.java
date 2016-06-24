package api.v1.model;

public class Category {
	private int id;
	private String name;
	private String description;
	
	
	/**
	 * Create an new Category w/o a category id. Categories without an
     * id are assigned an id of -9.
	 */
	public Category(){
		this.id=-9;
	}


	public int getId() {
		return id;
	}

	public void setId(int id){
		this.id=id;
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

package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import com.google.appengine.repackaged.com.google.gson.Gson;

public class Category {
	private int id;
	private String name;
	private String description;

	/**
	 * Create an new Category w/o a category id. Categories without an
     * id are assigned an id of -1.
	 */
	public Category(){
		this.id=-1;
        this.description=null;
        this.name=null;
    }

	public int getId() {
        return id;
	}

	public void setId(int id) {
		this.id=id;
	}

	public String getName() {
		return name;
	}

    public void setName(String name)throws BusinessException {
        if(name==null || name.equals(""))
            throw new BusinessException("The Category name cannot be empty.", Error.valueOf("INVALID_NAME_ERROR"));
        this.name=name;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    /**
     * Create a serialized JSON String of this instance
     * using GSON.
     * @return
     */
    public String toJson(){
        Gson gson=new Gson();
        return gson.toJson(this);
    }
}

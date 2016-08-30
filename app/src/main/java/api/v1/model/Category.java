package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import api.v1.helper.ModelHelper;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.util.ArrayList;

public class Category {
	private int id;
    private int userId;
	private String name;
	private String description;
    private ArrayList<Integer> taskIds;
    private ArrayList<Integer> scheduleIds;

	/**
	 * Create an new Category w/o a category id. Categories without an
     * id are assigned an id of -1.
	 */
	public Category(){
		this.id=-1;
        this.userId=-1;
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
        this.name=name.trim();
    }
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description.trim();
	}
    public ArrayList<Integer> getTaskIds() {
        return taskIds;
    }
    public void setTaskIds(ArrayList<Integer> taskIds) {
        this.taskIds = taskIds;
    }
    public ArrayList<Integer> getScheduleIds() {
        return scheduleIds;
    }
    public void setScheduleIds(ArrayList<Integer> scheduleIds) {
        this.scheduleIds = scheduleIds;
    }
    public void addSchedule(Schedule schedule){
        if(this.scheduleIds==null)
            scheduleIds=new ArrayList<Integer>();
        scheduleIds.add(schedule.getId());
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
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
    /**
     * Creates and returns a deep copy of this object.
     */
    public Category(Category originalCategory){
        this.id=originalCategory.getId();
        this.userId=originalCategory.getUserId();
        this.name= new String(originalCategory.getName());
        this.description=new String(originalCategory.getDescription());
        this.taskIds= ModelHelper.copyIntegerArrayList(originalCategory.getTaskIds());
        this.scheduleIds= ModelHelper.copyIntegerArrayList(originalCategory.getScheduleIds());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != category.id) return false;
        if (userId != category.userId) return false;
        if (!name.equals(category.name)) return false;
        if (description != null ? !description.equals(category.description) : category.description != null)
            return false;
        if (taskIds != null ? !taskIds.equals(category.taskIds) : category.taskIds != null) return false;
        return scheduleIds != null ? scheduleIds.equals(category.scheduleIds) : category.scheduleIds == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (taskIds != null ? taskIds.hashCode() : 0);
        result = 31 * result + (scheduleIds != null ? scheduleIds.hashCode() : 0);
        return result;
    }
}

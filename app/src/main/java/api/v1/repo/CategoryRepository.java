package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * This is the ProtoCategoryRepository. This class does not yet
 * interact with a database.
 */
public class CategoryRepository implements Repository<Category>{
    private static Logger LOGGER = LoggerFactory.getLogger(CategoryRepository.class);
    private HashMap<Integer, Category> categoryMap;


    /**
     * Create a new instance of a repository.
     */
    public CategoryRepository(){
        categoryMap=new HashMap<Integer, Category>();
    }

    /**
     * First discover a category id that has not been used and assign it to
     * the given task. Then, add the new task to the repository.
     *
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */
    public void add(Category c) throws BusinessException, SystemException{
	// First, we make sure that the category DNE. Else throw BusinessException
        int categoryId=0;
        while(categoryMap.containsKey(categoryId))
            categoryId++;
        c.setId(categoryId);
        categoryMap.put(categoryId, c);
    }

    /**
     * Fetch a category object from the repository with the given category id.
     * @param c
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Category get(Category c)throws BusinessException, SystemException{
        if(categoryMap.containsKey(c.getId()))
            return categoryMap.get(c.getId());
        else
            throw new BusinessException(" Category not found. ID=" + c.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     * Replace an instance of Category with the instance provided.
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Category c) throws BusinessException, SystemException{
        // First, delete the category:
        this.delete(c);
        // Then add the new category:
        this.add(c);
	}

    /**
     * Deletes the provided category.
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Category c) throws BusinessException, SystemException{
        if(categoryMap.containsKey(c.getId())){
            categoryMap.remove(c.getId());
        }
        else
            throw new BusinessException(" Category not found. ID=" + c.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }
}

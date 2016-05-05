package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
//import java.sql.SQLException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Category;

/**
 *
 * This is the ProtoCategoryRepository. This class does not yet
 * interact with a database.
 */
public class CategoryRepository implements Repository<Category>{

     private HashMap<Integer, Category> categoryMap;

    /**
     *
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */

    public void add(Category c) throws BusinessException, SystemException{
	// First, we make sure that the Category DNE. Else throw BusinessException
        if(categoryDNE(c))
            categoryMap.put(categoryMap.size(), c);
    }

/**
     * @param c
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Category get(Category c)throws BusinessException, SystemException{
        if(categoryMap.containsKey(c))
            return categoryMap.get(c);
        else
            throw new BusinessException(" Category not found. ", Error.valueOf("")); //TODO specifty error.
    }


    /**
     *
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Category c) throws BusinessException, SystemException{
        // First, delete the Category:
        this.delete(c);
        // Then add the new u:
        this.add(c);
	}

    /**
     * Deletes the provided Category.
     *
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Category c) throws BusinessException, SystemException{
	    categoryMap.remove(categoryMap.get(c.getId()));
    }


    public CategoryRepository(){
        categoryMap=new HashMap<Integer, Category>();
    }

    private boolean categoryDNE(Category c){
        if(categoryMap.containsKey(c))
            return false;
        else
            return true;
    }
}

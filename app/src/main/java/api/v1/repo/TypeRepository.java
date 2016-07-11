package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * This is the ProtoTypeRepository. This class does not yet
 * interact with a database.
 */
public class TypeRepository implements Repository<Type>{
    private static Logger LOGGER = LoggerFactory.getLogger(TypeRepository.class);
    private HashMap<Integer, Type> typeMap;


    /**
     * Create a new instance of a repository.
     */
    public TypeRepository(){
        typeMap=new HashMap<Integer, Type>();
    }

    /**
     * First discover a type id that has not been used. Then copy the incoming
     * type fields into the new type.
     * @param foobar
     * @throws BusinessException
     * @throws SystemException
     */
    public void add(Type foobar) throws BusinessException, SystemException{
	// First, we make sure that the type DNE. Else throw BusinessException
        int typeId=0;
        while(typeMap.containsKey(typeId))
            typeId++;
        typeMap.put(typeId, foobar);
    }

    /**
     * @param foobar
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Type get(Type foobar)throws BusinessException, SystemException{
        if(typeMap.containsKey(foobar.getId()))
            return typeMap.get(foobar.getId());
        else
            throw new BusinessException(" Type not found. ", Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }


    /**
     *
     * @param foobar
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Type foobar) throws BusinessException, SystemException{
        // First, delete the type:
        this.delete(foobar);
        // Then add the new type:
        this.add(foobar);
	}
    /**
     * Deletes the provided type.
     *
     * @param foobar
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Type foobar) throws BusinessException, SystemException{
        if(typeMap.containsKey(foobar.getId())){
            typeMap.remove(foobar.getId());
        }
        else
            throw new BusinessException(" Type not found. ", Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }
}

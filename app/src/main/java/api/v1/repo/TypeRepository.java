package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
//import java.sql.SQLException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Type;

/**
 *
 * This is the ProtoTypeRepository. This class does not yet
 * interact with a database.
 */
public class TypeRepository implements Repository<Type>{

     private HashMap<Integer, Type> typeMap;

    /**
     *
     * @param t
     * @throws BusinessException
     * @throws SystemException
     */

    public void add(Type t) throws BusinessException, SystemException{
	// First, we make sure that the type DNE. Else throw BusinessException
        if(typeDNE(t))
            typeMap.put(typeMap.size(), t);
    }

/**
     * @param t
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Type get(Type t)throws BusinessException, SystemException{
        if(typeMap.containsKey(t))
            return typeMap.get(t);
        else
            throw new BusinessException(" Type not found. ", Error.valueOf("")); //TODO specifty error.
    }


    /**
     *
     * @param t
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Type t) throws BusinessException, SystemException{
        // First, delete the type:
        this.delete(t);
        // Then add the new u:
        this.add(t);
	}

    /**
     * Deletes the provided type.
     *
     * @param t
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Type t) throws BusinessException, SystemException{
	    typeMap.remove(typeMap.get(t.getId()));
    }


    public TypeRepository(){
        typeMap=new HashMap<Integer, Type>();
    }

    private boolean typeDNE(Type t){
        if(typeMap.containsKey(t))
            return false;
        else
            return true;
    }
}

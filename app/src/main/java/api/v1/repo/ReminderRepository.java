package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
//import java.sql.SQLException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Reminder;

/**
 *
 * This is the ProtoReminderRepository. This class does not yet
 * interact with a database.
 */
public class ReminderRepository implements Repository<Reminder>{

     private HashMap<Integer, Reminder> reminderMap;

    /**
     *
     * @param r
     * @throws BusinessException
     * @throws SystemException
     */

    public void add(Reminder r) throws BusinessException, SystemException{
	// First, we make sure that the reminder DNE. Else throw BusinessException
        if(reminderDNE(r))
            reminderMap.put(reminderMap.size(), r);
    }

/**
     * @param r
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Reminder get(Reminder r)throws BusinessException, SystemException{
        if(reminderMap.containsKey(r))
            return reminderMap.get(r);
        else
            throw new BusinessException(" Reminder not found. ", Error.valueOf("")); //TODO specifty error.
    }


    /**
     *
     * @param r
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Reminder r) throws BusinessException, SystemException{
        // First, delete the reminder:
        this.delete(r);
        // Then add the new u:
        this.add(r);
	}

    /**
     * Deletes the provided reminder.
     *
     * @param r
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Reminder r) throws BusinessException, SystemException{
	    reminderMap.remove(reminderMap.get(r.getId()));
    }


    public ReminderRepository(){
        reminderMap=new HashMap<Integer, Reminder>();
    }

    private boolean reminderDNE(Reminder r){
        if(reminderMap.containsKey(r))
            return false;
        else
            return true;
    }
}

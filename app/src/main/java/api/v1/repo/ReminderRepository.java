package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * This is the ProtoReminderRepository. This class does not yet
 * interact with a database.
 */
public class ReminderRepository implements Repository<Reminder>{
    private static Logger LOGGER = LoggerFactory.getLogger(ReminderRepository.class);
    private HashMap<Integer, Reminder> reminderMap;


    /**
     * Create a new instance of a repository.
     */
    public ReminderRepository(){
        reminderMap=new HashMap<Integer, Reminder>();
    }

    /**
     * First discover a reminder id that has not been used and assign it to
     * the given task. Then, add the new task to the repository.
     *
     * @param r
     * @throws BusinessException
     * @throws SystemException
     */
    public void add(Reminder r) throws BusinessException, SystemException{
	// First, we make sure that the reminder DNE. Else throw BusinessException
        int reminderId=0;
        while(reminderMap.containsKey(reminderId))
            reminderId++;
        reminderMap.put(reminderId, r);
    }

    /**
     * Fetch a reminder object from the repository with the given reminder id.
     * @param r
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Reminder get(Reminder r)throws BusinessException, SystemException{
        if(reminderMap.containsKey(r.getId()))
            return reminderMap.get(r.getId());
        else
            throw new BusinessException(" Reminder not found. ID=" + r.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     * Replace an instance of Reminder with the instance provided.
     * @param r
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Reminder r) throws BusinessException, SystemException{
        // First, delete the reminder:
        this.delete(r);
        // Then add the new reminder:
        this.add(r);
	}

    /**
     * Deletes the provided reminder.
     * @param r
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Reminder r) throws BusinessException, SystemException{
        if(reminderMap.containsKey(r.getId())){
            reminderMap.remove(r.getId());
        }
        else
            throw new BusinessException(" Reminder not found. ID=" + r.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }
}

package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
//import java.sql.SQLException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Schedule;

/**
 *
 * This is the ProtoScheduleRepository. This class does not yet
 * interact with a database.
 */
public class ScheduleRepository implements Repository<Schedule>{

     private HashMap<Integer, Schedule> scheduleMap;

    /**
     *
     * @param s
     * @throws BusinessException
     * @throws SystemException
     */

    public void add(Schedule s) throws BusinessException, SystemException{
	// First, we make sure that the Schedule DNE. Else throw BusinessException
        if(scheduleDNE(s))
            scheduleMap.put(scheduleMap.size(), s);
    }

/**
     * @param s
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Schedule get(Schedule s)throws BusinessException, SystemException{
        if(scheduleMap.containsKey(s))
            return scheduleMap.get(s);
        else
            throw new BusinessException(" Schedule not found. ", Error.valueOf("")); //TODO specifty error.
    }


    /**
     *
     * @param s
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Schedule s) throws BusinessException, SystemException{
        // First, delete the Schedule:
        this.delete(s);
        // Then add the new u:
        this.add(s);
	}

    /**
     * Deletes the provided Schedule.
     *
     * @param s
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Schedule s) throws BusinessException, SystemException{
	    scheduleMap.remove(scheduleMap.get(s.getId()));
    }


    public ScheduleRepository(){
        scheduleMap=new HashMap<Integer, Schedule>();
    }

    private boolean scheduleDNE(Schedule s){
        if(scheduleMap.containsKey(s))
            return false;
        else
            return true;
    }
}

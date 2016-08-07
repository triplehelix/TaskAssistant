package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * This is the ProtoScheduleRepository. This class does not yet
 * interact with a database.
 */
public class ScheduleRepository implements Repository<Schedule>{
    private static Logger LOGGER = LoggerFactory.getLogger(ScheduleRepository.class);
    private HashMap<Integer, Schedule> scheduleMap;


    /**
     * Create a new instance of a repository.
     */
    public ScheduleRepository(){
        scheduleMap=new HashMap<Integer, Schedule>();
    }

    /**
     * First discover a schedule id that has not been used and assign it to
     * the given task. Then, add the new task to the repository.
     *
     * @param s
     * @throws BusinessException
     * @throws SystemException
     */
    public void add(Schedule s) throws BusinessException, SystemException{
	// First, we make sure that the schedule DNE. Else throw BusinessException
        int scheduleId=0;
        while(scheduleMap.containsKey(scheduleId))
            scheduleId++;
        s.setId(scheduleId);
        scheduleMap.put(scheduleId, s);
    }

    /**
     * Fetch a schedule object from the repository with the given schedule id.
     * @param s
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Schedule get(Schedule s)throws BusinessException, SystemException{
        if(scheduleMap.containsKey(s.getId()))
            return scheduleMap.get(s.getId());
        else
            throw new BusinessException(" Schedule not found. ID=" + s.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     * Replace an instance of Schedule with the instance provided.
     * @param s
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Schedule s) throws BusinessException, SystemException{
        // First, delete the schedule:
        this.delete(s);
        // Then add the new schedule:
        this.add(s);
	}

    /**
     * Deletes the provided schedule.
     * @param s
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Schedule s) throws BusinessException, SystemException{
        if(scheduleMap.containsKey(s.getId())){
            scheduleMap.remove(s.getId());
        }
        else
            throw new BusinessException(" Schedule not found. ID=" + s.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }
}

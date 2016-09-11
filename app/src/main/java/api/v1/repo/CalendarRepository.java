package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * This is the ProtoCalendarRepository. This class does not yet
 * interact with a database.
 */
public class CalendarRepository implements Repository<Calendar>{
    private static Logger LOGGER = LoggerFactory.getLogger(CalendarRepository.class);
    private HashMap<Integer, Calendar> calendarMap;


    /**
     * Create a new instance of a repository.
     */
    public CalendarRepository(){
        calendarMap=new HashMap<Integer, Calendar>();
    }

    /**
     * First discover a calendar id that has not been used and assign it to
     * the given task. Then, add the new task to the repository.
     *
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */
    public Calendar add(Calendar c) throws BusinessException, SystemException{
        int calendarId=0;
        while(calendarMap.containsKey(calendarId))
            calendarId++;
        c.setId(calendarId);
        calendarMap.put(calendarId, c);
        return c;
    }

    /**
     * Fetch a calendar object from the repository with the given calendar id.
     * @param c
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Calendar get(Calendar c)throws BusinessException, SystemException{
        if(calendarMap.containsKey(c.getId()))
            return calendarMap.get(c.getId());
        else
            throw new BusinessException(" Calendar not found. ID=" + c.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }

    /**
     * Replace an instance of Calendar with the instance provided.
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Calendar c) throws BusinessException, SystemException{
        if (calendarMap.containsKey(c.getId())) {
            calendarMap.remove(c.getId());
            calendarMap.put(c.getId(), c);
        } else
            throw new BusinessException(" Calendar not found. ID=" + c.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
	}

    /**
     * Deletes the provided calendar.
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Calendar c) throws BusinessException, SystemException{
        if(calendarMap.containsKey(c.getId())){
            calendarMap.remove(c.getId());
        }
        else
            throw new BusinessException(" Calendar not found. ID=" + c.getId(), Error.valueOf("NO_SUCH_OBJECT_ERROR"));
    }
}

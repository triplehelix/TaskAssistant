package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
//import java.sql.SQLException;
import api.v1.error.Error;
import java.util.HashMap;
import api.v1.model.Calendar;

/**
 *
 * This is the ProtoCalendarRepository. This class does not yet
 * interact with a database.
 */
public class CalendarRepository implements Repository<Calendar>{

     private HashMap<Integer, Calendar> calendarMap;

    /**
     *
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */

    public void add(Calendar c) throws BusinessException, SystemException{
	// First, we make sure that the calendar DNE. Else throw BusinessException
        if(calendarDNE(c))
            calendarMap.put(calendarMap.size(), c);
    }

/**
     * @param c
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
	public Calendar get(Calendar c)throws BusinessException, SystemException{
        if(calendarMap.containsKey(c))
            return calendarMap.get(c);
        else
            throw new BusinessException(" Calendar not found. ", Error.valueOf("")); //TODO specifty error.
    }


    /**
     *
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */
	public void update(Calendar c) throws BusinessException, SystemException{
        // First, delete the calendar:
        this.delete(c);
        // Then add the new u:
        this.add(c);
	}

    /**
     * Deletes the provided calendar.
     *
     * @param c
     * @throws BusinessException
     * @throws SystemException
     */
	public void delete(Calendar c) throws BusinessException, SystemException{
	    calendarMap.remove(calendarMap.get(c.getId()));
    }


    public CalendarRepository(){
        calendarMap=new HashMap<Integer, Calendar>();
    }

    private boolean calendarDNE(Calendar c){
        if(calendarMap.containsKey(c))
            return false;
        else
            return true;
    }
}

package api.v1;
import api.v1.repo.CalendarRepository;

/**
 * CalendarRequestHandler contains, fields and methods that are common to
 * calendar APIs. All calendar APIs inherit CalendarRequestHandler. 
 */
public class CalendarRequestHandler extends BaseRequestHandler {

    protected static CalendarRepository calendarRepository;

    static {
        calendarRepository = new CalendarRepository();
    }
}

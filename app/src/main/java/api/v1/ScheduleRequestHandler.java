package api.v1;
import api.v1.repo.ScheduleRepository;

/**
 * 
 * ScheduleRequestHandler contains, fields and methods that are common to
 * schedule APIs. All schedule APIs inherit ScheduleRequestHandler. 
 */
public class ScheduleRequestHandler extends TaskRequestHandler {

    protected static ScheduleRepository scheduleRepository;

    static {
        scheduleRepository = new ScheduleRepository();
    }
}

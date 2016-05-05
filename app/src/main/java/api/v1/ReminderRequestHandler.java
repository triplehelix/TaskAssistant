package api.v1;
import api.v1.repo.ReminderRepository;

/**
 * ReminderRequestHandler contains, fields and methods that are common to
 * reminder APIs. All reminder APIs inherit ReminderRequestHandler. 
 */
public class ReminderRequestHandler extends BaseRequestHandler {

    protected static ReminderRepository reminderRepository;

    static {
        reminderRepository = new ReminderRepository();
    }
}

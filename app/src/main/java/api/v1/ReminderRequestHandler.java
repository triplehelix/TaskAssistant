package api.v1;

import api.v1.repo.ReminderRepository;
/**
 * The ReminderHadlerClass provides tools to parse the input expected
 * for a new Reminder.
 * 
 */
public class ReminderRequestHandler extends BaseRequestHandler{

    protected static ReminderRepository reminderRepository;
    static {
        reminderRepository= new ReminderRepository();
    }
}

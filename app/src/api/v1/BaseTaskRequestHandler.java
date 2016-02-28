package api.v1;

public class BaseTaskRequestHandler extends BaseRequestHandler{

	/**
	 * Return an integer representation of a string id.
	 * @param stringId
	 * @return
	 */
    protected int ParseJsonAsTasklistId(String stringId){
    	return parseJsonIntAsInt(stringId);
    }
    protected int ParseJsonAsCategoryId(String stringId){
    	return parseJsonIntAsInt(stringId);
    }
    
    protected String ParseJsonAsName(){
    	
    }
    protected long ParseJsonAsEstimatedTime(){}
    protected long ParseJsonAsInvestedTime(){}
    protected Date ParseJsonAsScheduledTime(){}
    protected String ParseJsonAsImportance(){}
    protected String ParseJsonAsUrgency(){}
    protected Date ParseJsonAsDeadline(){}
    protected int ParseJsonAsTaskId(){}
    protected long ParseJsonAsReminderTime(){}
}

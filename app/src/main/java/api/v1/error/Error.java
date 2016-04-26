package api.v1.error;

/**
 * This class is a container for a grip of different kinds of errors. Mostly we are 
 * enumerating Business errors, which ought to encompass all of our checked exceptions.
 * 
 */
public enum Error{

    //1000-1999 business errors.
    //2000-2999 System error.
    //3000-3999 WTF error.
    // I got high hopes for you niggas, we gonna see.
    JSON_PARSE_EXCEPTION(1000, "The JSON string could not be parsed. "),
    JSON_DATE_EXCEPTION(1001, "The string provided could not be parsed as a date. ");
ls
    
    int code;
    String  message;

    /**
     *
     * @param code
     * @param message
     */
    Error(int code, String message){
        this.code=code;
        this.message=message;
    }

    /**
     * return the default message for this kind of exception.
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Return the error code ass/ w/ this exception.
     * @return
     */
    public int getCode() {
        return code;
    }
}

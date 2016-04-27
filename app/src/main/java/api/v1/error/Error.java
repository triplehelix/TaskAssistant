package api.v1.error;

/**
 * This class is a container for a grip of different kinds of errors. Mostly we are 
 * enumerating Business errors, which ought to encompass all of our checked exceptions.
 *
 * In this API we expect to handle errors of three major types: business errors, system
 * errors and WTF errors. Note that All WTF errors are unchecked. As such, you're sol
 * when that happens.
 * //1000-1999 business errors.
 * //2000-2999 System error.
 * //0000-0999 WTF error.
 *
 * I got high hopes for you niggas, we gonna see.
 */
public enum Error{


    PARSE_DATE_ERROR(1001, "Error Parsing String as Date. "),
    PARSE_INTEGER_ERROR(1002, "The String provided could not be parsed as an Integer. "),
    BAD_EMAIL_ERROR(1003, "Invalid email error. "),
    BAD_PASSWORD_ERROR(1004, "The password provided is not strong enough."),

    PARSE_JSON_ERROR(1999, "Error parsing the JSON String. ");


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

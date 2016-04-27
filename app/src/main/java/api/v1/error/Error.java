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

    //BusinessErrors.
    PARSE_JSON_ERROR(1999),
    PARSE_DATE_ERROR(1001),
    PARSE_INTEGER_ERROR(1002),
    BAD_EMAIL_ERROR(1003),
    BAD_PASSWORD_ERROR(1004),
    CREATE_USER_ERROR_USER_EXISTS(1005),
    CREATE_USER_ERROR_USER_DNE(1015),
    CREATE_USER_SQL_ERROR(2005)
    ;


    int code;

    /**
     *
     * @param code
     */
    Error(int code){
        this.code=code;
    }

    /**
     * Return the error code ass/ w/ this exception.
     * @return
     */
    public int getCode() {
        return code;
    }
}

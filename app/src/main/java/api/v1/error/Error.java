package api.v1.error;

/**
 * This class is a container for a grip of different kinds of errors. Mostly we are 
 * enumerating Business errors, which ought to encompass all of our checked exceptions.
 *
 * In this API we expect to handle errors of three major types: business errors, system
 * errors and WTF errors. Note that All WTF errors are unchecked. As such, they are not
 * included in this class.
 *
 * //1000-1999 business errors.
 * //2000-2999 System error.
 * //0000-0999 WTF error.
 *
 */
public enum Error{

    /*** Business Errors ***/
    PARSE_DATE_ERROR(1001),
    PARSE_INTEGER_ERROR(1002),
    BAD_EMAIL_ERROR(1003),
    BAD_PASSWORD_ERROR(1004),
    PARSE_LONG_INTEGER_ERROR(1005),
    CREATE_USER_ERROR_USER_EXISTS(1006),
    PARSE_BOOLEAN_ERROR(1007),
    INVALID_ENUM_TYPE(1008),

    /** model errors */
    CREATE_USER_ERROR_USER_DNE(1150),
    INVALID_TASK_NAME_ERROR(1160),
    INVALID_TASK_STATUS_ERROR(1161),

    /** Repo errors */
    NO_SUCH_TASK_ERROR(1200),

    UNIT_TEST_LOGIC_ERROR(1901),
    /*** System Errors ***/
    PARSE_JSON_ERROR(1999),
    CREATE_USER_SQL_ERROR(2001);
    private int code;

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

package api.v1.error;

@SuppressWarnings("serial")
public class SystemException extends Exception{
    private Error error;

    /**
     * Create a new SystemException. SystemException ought to have an
     * associated ErrorCode from 2000-2999.
     * @param message
     * @param error
     */
    public SystemException(String message, Error error)
    {
        super(message);
        this.error=error;
    }

    /**
     * Returns the instance of this Error.
     * @return
     */
    public Error getError(){ return this.error; }
}

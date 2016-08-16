package api.v1.error;


@SuppressWarnings("serial")
public class CriticalException extends Exception{
    private Error error;

    /**
     * Create a new CriticalException. CriticalException ought to have an
     * associated ErrorCode from 3000-3999.
     * @param message
     * @param error
     */
    public CriticalException(String message, Error error) {
        super(message);
        this.error = error;
    }

    /**
     * Returns the instance of this Error.
     * @return
     */
    public Error getError(){
        return this.error;
    }
}

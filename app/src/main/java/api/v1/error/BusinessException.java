package api.v1.error;


@SuppressWarnings("serial")
public class BusinessException extends Exception{
    private Error error;

    /**
     * Create a new BusinessException. BusinessException ought to have an
     * associated ErrorCode from 1000-1999.
     * @param message
     * @param error
     */
    public BusinessException(String message, Error error) {
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

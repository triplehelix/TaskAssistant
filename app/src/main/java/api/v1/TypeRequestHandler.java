package api.v1;
import api.v1.model.Reminder;
import api.v1.repo.ReminderRepository;
import api.v1.repo.TypeRepository;

import java.util.Date;
import javax.servlet.ServletException;

/**
 * The BaseTypeHadlerClass provides tools to parse the input expected
 * for a new type. These methods are to be visible to the api class
 * "AddType".
 */
public class TypeRequestHandler extends BaseRequestHandler{

	protected static TypeRepository typeRepository;
    static {
        typeRepository=new TypeRepository();
    }

	/**
	 * Return an integer representation of a string id.
	 * @param stringId
	 * @return
	 */

}


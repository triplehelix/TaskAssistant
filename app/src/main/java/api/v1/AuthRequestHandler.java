package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.model.*;
import api.v1.repo.UserRepository;
import api.v1.error.Error;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;


/**
 * BaseAuthRequestHandler is a subclass of BaseRequestHandler and provides functionality 
 * for APIs that require Authentication.
 * @author kennethlyon
 *
 */
public class AuthRequestHandler extends BaseRequestHandler{


	/**
	 * Validates that an email is well formed. Throws Exception 
	 * if it is not well formed.
	 * @param email
	 * @return
	 */
	protected String parseJsonAsEmail(String email) throws BusinessException{
		email=email.trim();
		if(!isValidEmail(email)){
            log.error("Supplied email address: {} is not valid.", email);
            throw new BusinessException("Email address: " + email + " is invalid.", Error.valueOf("INVALID_EMAIL_ERROR"));
		}
		return email;
	}

	/**
	 * Validates that the email is well formed. Returns T/F.
	 * @param email
	 * @return
     */
	private boolean isValidEmail(String email){
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
	}

	/**
	 * Validates the password as being well formed. Throws Exception.
	 * @param password
	 * @return
	 * @throws Exception
	 */
	protected String parseJsonAsPassword(String password) throws BusinessException{
        if(!password.matches("(?=^.{8,16}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+}{\":;'?/>.<,])(?!.*\\s).*$"))
            throw new BusinessException("Try another password. ", Error.valueOf("INVALID_PASSWORD_ERROR"));
		return password;
	}

    /**
     * Use to validate the supplied password from a GetUser request.
     * @param fromClient
     * @param fromRepository
     * @return
     */
    protected void validatePassword(User fromClient, User fromRepository) throws BusinessException, SystemException {
        if(fromClient.getPassword().equals(fromRepository.getPassword()))
            return;
        else{
            log.error(fromClient.toJson());
            log.error(fromRepository.toJson());
            throw new BusinessException("Incorrect password.", Error.valueOf("INCORRECT_PASSWORD_ERROR"));
        }
    }

	/**
	 * Verify that each taskId supplied belongs to a TaskList that belongs to the
	 * supplied User id.
	 * @param userId
	 * @param taskIds
	 */
	protected void verifyTaskPrivileges(int userId, ArrayList<Integer> taskIds)
			throws BusinessException, SystemException {
		if(taskIds==null)
			return;
		for(Integer i: taskIds){
			// First fetch the Task specified in the taskIds list.
			Task task=new Task();
			task.setId(i);
			task=taskRepository.get(task);

			//Next fetch the TaskList that owns this Task.
			TaskList taskList=new TaskList();
			taskList.setId(task.getTaskListId());
			taskList=taskListRepository.get(taskList);

			log.debug("TaskList owner id: " + taskList.getUserId());
			log.debug("User id: " + userId);
			//Finally, verify that ownership of the TaskList.
			if(taskList.getUserId()==userId)
				return;
			else{
				String message= "This task cannot be accessed by the specified user. ";
				throwObjectOwnershipError(userId, message);
			}
		}
	}

	protected void throwObjectOwnershipError(int userId, String message) throws BusinessException, SystemException{
		User user = new User();
		user.setId(userId);
		user=userRepository.get(user);
		message+=" {id: " + userId +", email: " + user.getEmail() + "}";
		throw new BusinessException(message, Error.valueOf("OBJECT_OWNERSHIP_ERROR"));
	}

	/**
	 * Verify that the User with the specified ID has permission to access these
	 * schedules.
	 * @param userId
	 * @param scheduleIds
	 */
	protected void verifySchedulePrivileges(int userId, ArrayList<Integer> scheduleIds) throws BusinessException, SystemException{
		if(scheduleIds==null)
			return;
		Schedule schedule=new Schedule();
		for(int i: scheduleIds)
			schedule.setId(i);
		schedule=scheduleRepository.get(schedule);
		if (schedule.getUserId()==userId)
			return;
		else{
			String message= "This schedule cannot be accessed by the specified user. ";
			throwObjectOwnershipError(userId, message);
		}
	}

	/**
	 * Verify that the User with the specified ID has permission to access these
	 * schedules.
	 * @param userId
	 * @param categoryIds
	 */
	protected void verifyCategoryPrivileges(int userId, ArrayList<Integer> categoryIds) throws BusinessException, SystemException{
		if(categoryIds==null)
			return;
		Category category=new Category();
		for(int i: categoryIds)
			category.setId(i);
		category=categoryRepository.get(category);
		if (category.getUserId()==userId)
			return;
		else{
			String message= "This category cannot be accessed by the specified user. ";
			throwObjectOwnershipError(userId, message);
		}
	}
}

package api.v1;
import api.v1.error.BusinessException;
import api.v1.error.CriticalException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import api.v1.model.Category;
import api.v1.model.Task;
import api.v1.model.User;

/**
 * 
 * CategoryRequestHandler contains, fields and methods that are common to
 * category APIs. All category APIs inherit CategoryRequestHandler. 
 */
public class CategoryRequestHandler extends TaskRequestHandler {

    /**
     * Remove references to this Category from the User, and Tasks associated with it.
     * Here we guarantee that no alterations to internal objects are made unless it can
     * be done without error.
     * @param category
     * @throws BusinessException
     * @throws SystemException
     */
    protected void removeReferences(Category category) throws BusinessException, SystemException, CriticalException {
        User user=new User();
        user.setId(category.getUserId());
        user=userRepository.get(user);
        for(int i: user.getCategoryIds())
            log.debug("Concerning the user "+user.getEmail()+ ", here is one category id: " + i);
        //log.debug("Category as JSON: " + category.toJson());
        //log.debug("User as JSON: " + user.toJson());
        if(user.getCategoryIds().contains(category.getId())) {
            //  log.debug("Here is the category id: " + category.getId());
            //  log.debug("Here are the category ids that belong to the user: " + new Gson().toJson(user.getCategoryIds()));
            user.getCategoryIds().remove((Object)category.getId());
        }
        else
            throw new CriticalException("Critical error! Cannot clean this Category. User {email="
                    + user.getEmail() + ", id=" + user.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));

        Task task = new Task();
        // Verify that all tasks expected to reference this Category actually do reference this category.
        for(int taskId: category.getTaskIds()) {
            task.setId(taskId);
            task = taskRepository.get(task);
            if (!task.getCategoryIds().contains(category.getId()))
                throw new CriticalException("Critical error! Cannot clean this Category. Task {name="
                        + task.getName() + ", id=" + task.getId()
                        + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
        }
        // Now we actually remove references to this object.
        for(int taskId: category.getTaskIds()) {
            task.setId(taskId);
            task = taskRepository.get(task);
            if (task.getCategoryIds().contains(category.getId()))
                task.getCategoryIds().remove((Object)category.getId());
        }
    }
}

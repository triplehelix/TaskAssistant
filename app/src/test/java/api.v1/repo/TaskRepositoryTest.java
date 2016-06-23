package api.v1.repo;
import api.v1.model.Task;
import api.v1.model.TaskTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.fail;

/**
 * Test the TaskRepository. Here we ensure that for Task t:
 *    1. TaskRepository.get(t) is equal to t when t has just been added.
 *    2. TaskRepository.get(t) is equal to t when t has just been Updated.
 *    3. delete(t) throws an error if t DNE.
 *    4. update(t) throws an error if t DNE.
 *    5. get(t) throws an error if t DNE.
 * Created by kennethlyon on 6/18/16.
 *
 */
public class TaskRepositoryTest {


    private Logger LOGGER = LoggerFactory.getLogger(TaskRepositoryTest.class);
    private TaskRepository taskRepository = new TaskRepository();
    private static ArrayList<Task> validTasks = new ArrayList();
    private static ArrayList<Task> validUpdates = new ArrayList();

    @Before
    public void setUp() throws Exception {
        taskRepository = new TaskRepository();
        validTasks = TaskTest.getValidTestTasksAsTasks();
        validUpdates = TaskTest.getValidTestTasksUpdatesAsTasks();
    }

    @Test
    public void test() throws Exception {
        for (Task t : validTasks) {
            LOGGER.info("Add valid task {}", t.toJson());
            taskRepository.add(t);
        }
        validateAddedTasks();


        for (Task t : validUpdates) {
            LOGGER.info("Add valid task {}", t.toJson());
            taskRepository.update(t);
        }
        validateUpdatedTasks();
        testDelete();
    }

    @After
    public void tearDown() throws Exception {
        taskRepository = null;
        validUpdates=null;
        validTasks = null;
    }

    /**
     * Compare the object just added to the repository to an
     * object from the repository with the same id.
     *
     * @throws Exception
     */
    private void validateAddedTasks() throws Exception {
        for (Task tIn : validTasks) {
            if (!(taskRepository.get(tIn).toJson()).equals(tIn.toJson()))
                LOGGER.error("These tasks are not identical!\n"+
                        taskRepository.get(tIn).toJson() + "\n" +
                        tIn.toJson()
                );
            else
                LOGGER.info("These tasks are identical.\n"+
                        taskRepository.get(tIn).toJson() + "\n" +
                        tIn.toJson()
                );
        }
    }

    /**
     * Compare the object just added to the repository to an
     * object from the repository with the same id.
     *
     * @throws Exception
     */
    private void validateUpdatedTasks() throws Exception {
        for (Task tIn : validUpdates) {
            if (!(taskRepository.get(tIn).toJson()).equals(tIn.toJson()))
                LOGGER.error("These tasks are not identical!\n" +
                        taskRepository.get(tIn).toJson() + "\n" +
                        tIn.toJson()
                );
            else
                LOGGER.info("These tasks are identical.\n"+
                        taskRepository.get(tIn).toJson() + "\n" +
                        tIn.toJson()
                );
        }
    }

    private void testDelete() {
        //First delete them all.
        Task t = null;
        try {
            for (int i=0;i<validUpdates.size();i++) {
                t=validUpdates.get(i);
                taskRepository.delete(t);
            }
        } catch (Exception e) {
            LOGGER.error("delete task error. Task not deleted {}", t.toJson());
            fail("The task could not be deleted.");
        }

        boolean error=false;
        for (int i=0;i<validUpdates.size();i++){
            try {
                t=validUpdates.get(i);
                taskRepository.delete(t);
            } catch (Exception e) {
                LOGGER.error("delete task error {}", t.toJson());
                error = true;
            }
            if(!error)
                fail("Success returned for an invalid delete.");
        }
    }

}

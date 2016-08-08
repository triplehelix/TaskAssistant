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
    private static ArrayList<Task> validTasks = new ArrayList<Task>();
    private static ArrayList<Task> validUpdates = new ArrayList<Task>();

    @Before
    public void setUp() throws Exception {
        taskRepository = new TaskRepository();

        /* Add valid tasks. Tasks fields are arranged in the order:
         * validTasks.add("int id` String name` boolean important` String note` long estimatedTime` long investedTime` boolean urgent` Date dueDate` State status");
         */
        ArrayList<String> protoValidTasks = new ArrayList<String>();
        protoValidTasks.add("0`0`Feed dog`TRUE`Dog eats kibble.`60000`0`TRUE`2020-05-28_08:31:01`NEW");
        protoValidTasks.add("1`0`Create AddTask unit test`TRUE`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS");
        protoValidTasks.add("2`0`Buy beer`TRUE`Pick up some IPAs on the way home from work. Edit: Bill said he would pick up beers instead.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");
        protoValidTasks.add("3`0`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`3600000`0`FALSE`2016-06-12_08:00:00`DEFERRED");
        protoValidTasks.add("4`0`Shave`FALSE`GF said I need to shave.`180000`0`TRUE`2016-06-09_19:00:00`DONE");
        protoValidTasks.add("5`0`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`TRUE`2016-06-09_19:00:00`NEW");
        protoValidTasks.add("6`0`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW");
        protoValidTasks.add("7`0`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED");
        protoValidTasks.add("8`0`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS");

        // Add valid mutations to valid tasks.
        ArrayList<String> protoValidUpdates = new ArrayList<String>();
        protoValidUpdates.add("0`1`Feed dog`TRUE`Give food to the fluff.`60000`0`TRUE`2020-05-28_08:31:01`NEW");
        protoValidUpdates.add("1`1`Create AddTask unit test`false`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS");
        protoValidUpdates.add("2`1`Buy beer`TRUE`Bill is getting IPAs for the party.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");
        protoValidUpdates.add("3`1`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`1800000`0`FALSE`2016-06-12_08:00:00`DEFERRED");
        protoValidUpdates.add("4`1`Shave`FALSE`GF said I need to shave.`180000`90000`TRUE`2016-06-09_19:00:00`DONE");
        protoValidUpdates.add("5`1`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`false`2016-06-09_19:00:00`NEW");
        protoValidUpdates.add("6`1`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW");
        protoValidUpdates.add("7`1`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED");
        protoValidUpdates.add("8`1`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`DONE");

        validTasks = RepositoryHelper.toTasks(protoValidTasks);
        validUpdates = RepositoryHelper.toTasks(protoValidUpdates);
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
        testUpdate();
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

    /**
     * Delete all of the tasks in the repository. Then, Attempt to
     * delete them again.
     */
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
        //LOGGER.debug("Re: TaskRepositoryTest.testDelete: ");
        boolean error=false;
        for (int i=0;i<validUpdates.size();i++){
            try {
                t=validUpdates.get(i);
          //    LOGGER.debug("Re: TaskRepositoryTest.testDelete: Attempting to delete " + t.toJson());
                taskRepository.delete(t);
            } catch (Exception e) {
                LOGGER.error("Delete Task error \n\t{}", t.toJson());
                LOGGER.error(e.getMessage());
                error = true;
            }
            if(!error)
                fail("Success returned for an invalid delete.");
        }
    }

    private void testUpdate(){
        LOGGER.debug("Re: TaskRepositoryTest.testUpdate: ");
        boolean error=false;
        Task t=null;
        for (int i=0;i<validUpdates.size();i++){
            try {
                t=validUpdates.get(i);
                LOGGER.debug("Re: TaskRepositoryTest.testDelete: Attempting to delete " + t.toJson());
                taskRepository.update(t);
            } catch (Exception e) {
                LOGGER.error("Update Task error \n\t{}", t.toJson());
                LOGGER.error(e.getMessage());
                error = true;
            }
            if(!error)
                fail("Success returned for an invalid update.");
        }
    }
}

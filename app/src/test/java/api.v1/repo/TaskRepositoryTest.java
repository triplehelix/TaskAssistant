package api.v1.repo;
import api.v1.model.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;

/**
 * Created by kennethlyon on 6/18/16.
 *
 */
public class TaskRepositoryTest {


    private Logger LOGGER = LoggerFactory.getLogger(TaskRepository.class);
    TaskRepository taskRepository=new TaskRepository();

    @Before
    public void setUp() throws Exception{
	taskRepository=new TaskRepository();

    }

    @Test
    public void test() throws Exception{
    /*
        for(String s:validTasks)
	        taskRepository.add(createTask(s))

        for(String s:errorTasks)
	        taskRepository.add(createTask(s));*/
    }

    @After
    public void tearDown() throws Exception{

    }
}

package api.v1.task;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 6/9/16.
 */
public class TaskTestHelper {
    protected static ArrayList<String> validTasks;
    protected static ArrayList<String> errorTasks;
    static{

        /* Add valid tasks. Tasks fields are arranged in the order:
         * validTasks.add("int id` String name` boolean important` String note` long estimatedTime` long investedTime` boolean urgent` Date dueDate` State status");
         */
        validTasks=new ArrayList<String>();
        validTasks.add("0`Feed dog`TRUE`Dog eats kibble.`60000`0`TRUE`2020-05-28_08:31:01`NEW");
        validTasks.add("1`Create AddTask unit test`TRUE`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS");
        validTasks.add("2`Buy beer`TRUE`Pick up some IPAs on the way home from work. Edit: Bill said he would pick up beers instead.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");
        validTasks.add("3`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`3600000`0`FALSE`2016-06-12_08:00:00`DEFERRED");
        validTasks.add("4`Shave`FALSE`GF said I need to shave.`180000`0`TRUE`2016-06-09_19:00:00`DONE");
        validTasks.add("5`Robert')); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`TRUE`2016-06-09_19:00:00`NEW");

        // Add error causing tasks.
        errorTasks=new ArrayList<String>();
        errorTasks.add("6`Call Attorney J.J. Coleostomy`TRUE`Bring photographic proof!`3600000`0`ONE`2016-06-14_15:15:00`NEW");
        errorTasks.add("7`Fix mom's computer.`TRUE`Again!?!`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorTasks.add("8`Prepare for zombie apocalypse .`TRUE`Need cat nip and shotguns.`4200000`0`TRUE`yyyy-MM-dd_HH:mm:ss`NEW");
        errorTasks.add("9`Vaccinate cat against zombie cat syndrome.`TRUE`Don't forget that Mr Bigglesworth doesn't like shots.`1 hour`0`TRUE`2020-08-31_00:00:00`NEW");
        errorTasks.add("10`Change motor oil`untrue`Use quicky-lube coupon.`1600000`0`FALSE`2020-05-31_22:00:00`NEW");
        errorTasks.add("11`Merge git conflickts`TRUE``180000`0`TRUE`2020-05-31_03:00:00`incomplete");
        errorTasks.add("12`Refinish porch`FALSE``210000`0`TRUE`2020-02-31_00:00:00`NEW");
    }
}

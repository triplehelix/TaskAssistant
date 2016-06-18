package api.v1.task;

import java.util.ArrayList;

/**
 * This class serves a a container for test case proto-tasks.
 * Created by kennethlyon on 6/9/16.
 */
public class TaskTestHelper {
    protected static ArrayList<String> validTasks;
    protected static ArrayList<String> errorTasks;
    protected static ArrayList<String> validUpdates;
    protected static ArrayList<String> errorUpdates;
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
        validTasks.add("5`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`TRUE`2016-06-09_19:00:00`NEW");
        validTasks.add("6`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW");
        validTasks.add("7`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED");
        validTasks.add("8`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS");
    

        // Add error causing tasks.
        errorTasks=new ArrayList<String>();
        errorTasks.add("0`Call Attorney J.P. Coleostomy`TRUE`Bring photographic proof!`3600000`0`YES`2016-06-14_15:15:00`NEW");
        errorTasks.add("1`Fix mom's computer.`TRUE`Again!?!`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorTasks.add("2`Prepare for apocalyptic zombie-cat-hoard.`TRUE`Need cat nip and shotguns.`4200000`0`TRUE`yyyy-MM-dd_HH:mm:ss`NEW");
        errorTasks.add("3`Vaccinate cat against zombie cat syndrome.`TRUE`Don't forget that Mr Bigglesworth doesn't like shots.`1 hour`0`TRUE`2020-08-31_00:00:00`NEW");
        errorTasks.add("4`Change motor oil`BLUE`Use quicky-lube coupon.`1600000`0`FALSE`2020-05-31_22:00:00`NEW");
        errorTasks.add("5`merge git conflicts`TRUE`I really need to learn how to use git.`180000`0`TRUE`2020-05-31_03:00:00`incomplete");
        errorTasks.add("6`Refinish porch`FALSE``210000`0`TRUE`2020-09-31_00:00:00`NEW");
        errorTasks.add("7``TRUE`THIS TASK HAS NO NAME`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorTasks.add(" ```` ``` ``` ``` ``` ``` ``` ``` ```");


        // Add valid mutations to valid tasks.         
        validUpdates=new ArrayList<String>();
        //repoTasks.get(0).setName("Give food to the fluff.");
        //               "0`Feed dog`TRUE`Dog eats kibble.`60000`0`TRUE`2020-05-28_08:31:01`NEW"
        validUpdates.add("0`Feed dog`TRUE`Dog eats kibble.`60000`0`TRUE`2020-05-28_08:31:01`NEW");

        //repoTasks.get(1).setImportant(false);
        //               "1`Create AddTask unit test`TRUE`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS"
        validUpdates.add("1`Create AddTask unit test`false`A unit test for the AddTask api needs to be created.`3600000`60000`FALSE`2020-05-31_00:00:00`IN_PROGRESS");

        //repoTasks.get(2).setNote("Bill is getting IPAs for the party.");
        //               "2`Buy beer`TRUE`Pick up some IPAs on the way home from work. Edit: Bill said he would pick up beers instead.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");
        validUpdates.add("2`Buy beer`TRUE`Bill is getting IPAs for the party.`900000`0`TRUE`2016-06-09_18:30:00`DELEGATED");

        // repoTasks.get(3).setEstimatedTime(1800000);
        //               "3`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`3600000`0`FALSE`2016-06-12_08:00:00`DEFERRED"
        validUpdates.add("3`Play basketball with Tom and Eric.`FALSE`Sunday morning at 08:00 at Sunset Park.`1800000`0`FALSE`2016-06-12_08:00:00`DEFERRED");

        //repoTasks.get(4).setInvestedTime(90000);
        //               "4`Shave`FALSE`GF said I need to shave.`180000`0`TRUE`2016-06-09_19:00:00`DONE");
        validUpdates.add("4`Shave`FALSE`GF said I need to shave.`180000`90000`TRUE`2016-06-09_19:00:00`DONE");

        // repoTasks.get(5).setUrgent(false);
        //               "5`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`TRUE`2016-06-09_19:00:00`NEW"
        validUpdates.add("5`Robert'); DROP TABLE`TRUE`We call him little Bobby Tables.`300000`0`false`2016-06-09_19:00:00`NEW");

        //repoTasks.get(6).setDueDate();
        //               "6`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW"
        validUpdates.add("6`Collect underpants`TRUE`In phase 1 we collect underpants.`94620000000`31540000000`FALSE`2020-05-31_00:00:00`NEW");

        //repoTasks.get(7).setStatus("DONE");
        //               "7`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED"
        validUpdates.add("7`Do taxes`TRUE`Yay!! Taxes!!!`3600000`60000`TRUE`2016-04-15_00:00:01`DEFERRED");

        //repoTasks.get(8).setStatus(Status.valueOf("DONE"));
        //               "8`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS"
        validUpdates.add("8`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`DONE");
    


        // Add invalid mutations to valid tasks.
        errorUpdates=new ArrayList<String>();
        errorUpdates.add("0`Call Attorney J.P. Coleostomy`TRUE`Bring photographic proof!`3600000`0`YES`2016-06-14_15:15:00`NEW");
        errorUpdates.add("1`Fix mom's computer.`TRUE`Again!?!`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorUpdates.add("2`Prepare for apocalyptic zombie-cat-hoard.`TRUE`Need cat nip and shotguns.`4200000`0`TRUE`yyyy-MM-dd_HH:mm:ss`NEW");
        errorUpdates.add("3`Vaccinate cat against zombie cat syndrome.`TRUE`Don't forget that Mr Bigglesworth doesn't like shots.`1 hour`0`TRUE`2020-08-31_00:00:00`NEW");
        errorUpdates.add("4`Change motor oil`BLUE`Use quicky-lube coupon.`1600000`0`FALSE`2020-05-31_22:00:00`NEW");
        errorUpdates.add("5`merge git conflicts`TRUE`I really need to learn how to use git.`180000`0`TRUE`2020-05-31_03:00:00`incomplete");
        errorUpdates.add("6`Refinish porch`FALSE``210000`0`TRUE`2020-09-31_00:00:00`NEW");
        errorUpdates.add("7``TRUE`THIS TASK HAS NO NAME`3600000`not started`TRUE`2016-06-12_08:00:00`NEW");
        errorUpdates.add("100`Finish TaskAssistant`TRUE`APIs, Unit tests, services...`1080000000`360000000`FALSE`2016-06-01_00:00:01`IN_PROGRESS");
    }
}

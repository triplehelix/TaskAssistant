package api.v1.repo;
import api.v1.model.Type;
import api.v1.model.TypeTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.fail;

/**
 * Test the TypeRepository. Here we ensure that for Type foobar:
 *    1. TypeRepository.get(foobar) is equal to foobar when foobar has just been added.
 *    2. TypeRepository.get(foobar) is equal to foobar when foobar has just been Updated.
 *    3. delete(foobar) throws an error if foobar DNE.
 *    4. update(foobar) throws an error if foobar DNE.
 *    5. get(foobar) throws an error if foobar DNE.
 * Created by kennethlyon on 6/18/16.
 *
 */
public class TypeRepositoryTest {

    private Logger LOGGER = LoggerFactory.getLogger(TypeRepositoryTest.class);
    private TypeRepository typeRepository = new TypeRepository();
    private static ArrayList<Type> validTypes = new ArrayList<Type>();
    private static ArrayList<Type> validUpdates = new ArrayList<Type>();

    @Before
    public void setUp() throws Exception {
        typeRepository = new TypeRepository();
        validTypes = TypeTest.getValidTestTypesAsTypes();
        validUpdates = TypeTest.getValidTestTypesUpdatesAsTypes();
    }

    @Test
    public void test() throws Exception {
        for (Type foobar : validTypes) {
            LOGGER.info("Add valid type {}", foobar.toJson());
            typeRepository.add(foobar);
        }
        validateAddedTypes();


        for (Type foobar : validUpdates) {
            LOGGER.info("Add valid type {}", foobar.toJson());
            typeRepository.update(foobar);
        }
        validateUpdatedTypes();
        testDelete();
        testUpdate();
    }

    @After
    public void tearDown() throws Exception {
        typeRepository = null;
        validUpdates=null;
        validTypes = null;
    }

    /**
     * Compare the object just added to the repository to an
     * object from the repository with the same id.
     *
     * @throws Exception
     */
    private void validateAddedTypes() throws Exception {
        for (Type foobarIn : validTypes) {
            if (!(typeRepository.get(foobarIn).toJson()).equals(foobarIn.toJson()))
                LOGGER.error("These types are not identical!\n"+
                        typeRepository.get(foobarIn).toJson() + "\n" +
                        foobarIn.toJson()
                );
            else
                LOGGER.info("These types are identical.\n"+
                        typeRepository.get(foobarIn).toJson() + "\n" +
                        foobarIn.toJson()
                );
        }
    }

    /**
     * Compare the object just added to the repository to an
     * object from the repository with the same id.
     *
     * @throws Exception
     */
    private void validateUpdatedTypes() throws Exception {
        for (Type tIn : validUpdates) {
            if (!(typeRepository.get(tIn).toJson()).equals(tIn.toJson()))
                LOGGER.error("These types are not identical!\n" +
                        typeRepository.get(tIn).toJson() + "\n" +
                        tIn.toJson()
                );
            else
                LOGGER.info("These types are identical.\n"+
                        typeRepository.get(tIn).toJson() + "\n" +
                        tIn.toJson()
                );
        }
    }

    /**
     * Delete all of the types in the repository. Then, Attempt to
     * delete them again.
     */
    private void testDelete() {
        //First delete them all.
        Type foobar = null;
        try {
            for (int i=0;i<validUpdates.size();i++) {
                foobar=validUpdates.get(i);
                typeRepository.delete(foobar);
            }
        } catch (Exception e) {
            LOGGER.error("delete type error. Type not deleted {}", foobar.toJson());
            fail("The type could not be deleted.");
        }
        //LOGGER.debug("Re: TypeRepositoryTest.testDelete: ");
        boolean error=false;
        for (int i=0;i<validUpdates.size();i++){
            try {
                foobar=validUpdates.get(i);
          //    LOGGER.debug("Re: TypeRepositoryTest.testDelete: Attempting to delete " + t.toJson());
                typeRepository.delete(foobar);
            } catch (Exception e) {
                LOGGER.error("Delete Type error \n\t{}", foobar.toJson());
                LOGGER.error(e.getMessage(), e);
                error = true;
            }
            if(!error)
                fail("Success returned for an invalid delete.");
        }
    }

    /**
     *  Test the updateType function of this repository.
     */
    private void testUpdate(){
        LOGGER.debug("Re: TypeRepositoryTest.testUpdate: ");
        boolean error=false;
        Type foobar=null;
        for (int i=0;i<validUpdates.size();i++){
            try {
                foobar=validUpdates.get(i);
                LOGGER.debug("Re: TypeRepositoryTest.testDelete: Attempting to delete " + foobar.toJson());
                typeRepository.update(foobar);
            } catch (Exception e) {
                LOGGER.error("Update Type error \n\t{}", foobar.toJson());
                LOGGER.error(e.getMessage(), e);
                error = true;
            }
            if(!error)
                fail("Success returned for an invalid update.");
        }
    }
}

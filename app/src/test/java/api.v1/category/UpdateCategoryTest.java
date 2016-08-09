package api.v1.category;

import api.v1.model.Category;
import api.v1.repo.CategoryRepository;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.ArrayList;

/**
 * This class will test the UpdateCategory Class
 * @author kennethlyon
 */
public class UpdateCategoryTest extends CategoryApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(UpdateCategoryTest.class);
    private static UpdateCategory updateCategoryInstance;
    private static CategoryRepository categoryRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validCategories;
    private static ArrayList<String> validUpdates;
    private static ArrayList<String> errorUpdates;
    /**
     * First create a new Instance of UpdateCategory() object, then add new
     * category test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        updateCategoryInstance = new UpdateCategory();
        categoryRepository=updateCategoryInstance.getCategoryRepository();

        validCategories=new ArrayList<String>();
        validCategories.add("0`Physics`Homework, study groups, lab reports, etc, for physics II");
        validCategories.add("1`chores`Any kind of household chores.");
        validCategories.add("2`work`work related stuff only!");
        validCategories.add("3`money`Anything related to money. Taxes, budgeting, student loans, etc.");
        validCategories.add("4`Journal club`Tasks related to journal club");
        validCategories.add("5`Organic Chemistry`Homework, study groups, lab reports, etc, for organic chemistry.");

        validUpdates=new ArrayList<String>();
        validUpdates.add("0`Physics 181`Homework, study groups, lab reports, etc, for physics II");
        validUpdates.add("1`chores`Any kind of household chores.");
        validUpdates.add("2`work`work work work work work work");
        validUpdates.add("3`money`Anything related to money. Taxes, budgeting, student loans, etc.");
        validUpdates.add("4`Journal club`Bioinfromatics journal articles that I need to read.");
        validUpdates.add("5`O-Chem`Homework, study groups, lab reports, etc, for organic chemistry.");

        errorUpdates=new ArrayList<String>();
        errorUpdates.add("99``Homework, study groups, lab reports, etc, for physics II");
        errorUpdates.add("-1``Any kind of household chores.");
        errorUpdates.add("-2``work related stuff only!");
        errorUpdates.add("300``Anything related to money. Taxes, budgeting, student loans, etc.");
        errorUpdates.add("10`Journal Club`Tasks related to journal club");
        errorUpdates.add("-5`O-chem`Study groups, lab reports, etc, for organic chemistry.");


        // Populate the Category repository with valid Categories.
        for(Category category: CategoryApiHelper.toCategories(validCategories))
            categoryRepository.add(category);

        // Create valid mock categories.
        for(JSONObject jsonObj: CategoryApiHelper.toJSONObject(validUpdates))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create invalid mock categories.
        for(JSONObject jsonObj: CategoryApiHelper.toJSONObject(errorUpdates))
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, remove Categories from the CategoryRepository
     * then set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(Category category: CategoryApiHelper.toCategories(validCategories))
            categoryRepository.delete(category);
        updateCategoryInstance = null;
        validRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to UpdateCategory then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateCategoryInstance.doPost(request, response);
            validateDoPostValidResponse(response);

        }

        Category category=new Category();
        LOGGER.info("Verifying categories were updated in the repository...");
        for(int i=0;i<validRequestList.size();i++) {
            category.setId(i);
            LOGGER.info(categoryRepository.get(category).toJson());
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            updateCategoryInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
    }

    /**
     * Pass this method a json object to return a MockHttpServletRequest.
     * @param jsonObj
     * @return
     */
    private MockHttpServletRequest createDoPostMockRequest(JSONObject jsonObj){
        MockHttpServletRequest request = new MockHttpServletRequest();
        LOGGER.info("Created request {}",jsonObj.toJSONString());
        request.addParameter("params", jsonObj.toJSONString());
        return request;
    }
}

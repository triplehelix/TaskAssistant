package api.v1.category;

import api.v1.ApiTest;
import api.v1.model.Category;
import api.v1.model.CategoryTest;
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
 * Created by mikeh on 4/17/2016.
 * This class will test the UpdateCategory Class
 */
public class UpdateCategoryTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(UpdateCategoryTest.class);
    private static UpdateCategory updateCategoryInstance;
    private static CategoryRepository categoryRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

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

        // Populate the Category repository with valid Categories.
        for(Category category: CategoryTest.getValidTestCategoriesAsCategories())
            categoryRepository.add(category);

        // Create valid mock categories.
        for(JSONObject jsonObj: CategoryTest.getValidTestCategoryUpdatesAsJson())
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create error mock categories.
        for(JSONObject jsonObj: CategoryTest.getErrorTestCategoryUpdatesAsJson())
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
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

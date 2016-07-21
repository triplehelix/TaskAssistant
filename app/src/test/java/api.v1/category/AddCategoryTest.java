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
 * This class tests the AddCategory Class.
 * @author kennethlyon
 */
public class AddCategoryTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(AddCategoryTest.class);
    private static AddCategory addCategoryInstance;
    private static CategoryRepository categoryRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();

    /**
     * First create a new Instance of AddCategory() object, then add new
     * category test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        addCategoryInstance = new AddCategory();
        categoryRepository=addCategoryInstance.getCategoryRepository();
        // Create valid mock categories.
        for(JSONObject jsonObj: CategoryTest.getValidTestCategoriesAsJson())
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create invalid mock categories.
        for(JSONObject jsonObj: CategoryTest.getErrorTestCategoriesAsJson())
            errorRequestList.add(createDoPostMockRequest(jsonObj));
    }

    /**
     * After doPost runs, set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        addCategoryInstance = null;
        validRequestList = null;
        errorRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to AddCategory then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addCategoryInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }
        Category category=new Category();
        LOGGER.info("Verifying categories were placed in the repository...");
        for(int i=0;i<validRequestList.size();i++) {
            category.setId(i);
            LOGGER.info(categoryRepository.get(category).toJson());
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            addCategoryInstance.doPost(request, response);
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

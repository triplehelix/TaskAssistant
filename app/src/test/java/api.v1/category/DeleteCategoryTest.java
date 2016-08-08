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
 * This class tests the DeleteCategory Class
 * @author kennethlyon
 */
public class DeleteCategoryTest extends ApiTest {
    private Logger LOGGER = LoggerFactory.getLogger(DeleteCategoryTest.class);
    private static DeleteCategory deleteCategoryInstance;
    private static CategoryRepository categoryRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validCategories;
    private static ArrayList<String> errorCategories;

    /**
     * First create a new Instance of DeleteCategory() object, then add new
     * category test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        deleteCategoryInstance = new DeleteCategory();
        categoryRepository=deleteCategoryInstance.getCategoryRepository();

        validCategories=new ArrayList<String>();
        validCategories.add("0`Physics`Homework, study groups, lab reports, etc, for physics II");
        validCategories.add("1`chores`Any kind of household chores.");
        validCategories.add("2`work`work related stuff only!");
        validCategories.add("3`money`Anything related to money. Taxes, budgeting, student loans, etc.");
        validCategories.add("4`Journal club`Tasks related to journal club");
        validCategories.add("5`Organic Chemistry`Homework, study groups, lab reports, etc, for organic chemistry.");

        errorCategories=new ArrayList<String>();
        errorCategories.add("99``Homework, study groups, lab reports, etc, for physics II");
        errorCategories.add("-1``Any kind of household chores.");
        errorCategories.add("-2``work related stuff only!");
        errorCategories.add("300``Anything related to money. Taxes, budgeting, student loans, etc.");
        errorCategories.add("10`Journal Club`Tasks related to journal club");
        errorCategories.add("-5`O-chem`Study groups, lab reports, etc, for organic chemistry.");


        // Populate the Category repository with valid Categories.
        for(Category category: CategoryApiHelper.toCategories(validCategories))
            categoryRepository.add(category);

        // Create valid mock categories.
        for(JSONObject jsonObj: CategoryApiHelper.toJSONObject(validCategories))
            validRequestList.add(createDoPostMockRequest(jsonObj));

        // Create invalid mock categories.
        for(JSONObject jsonObj: CategoryApiHelper.toJSONObject(errorCategories))
            errorRequestList.add(createDoPostMockRequest(jsonObj));

    }

    /**
     * After doPost runs, set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        deleteCategoryInstance = null;
        validRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to DeleteCategory then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            deleteCategoryInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            deleteCategoryInstance.doPost(request, response);
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

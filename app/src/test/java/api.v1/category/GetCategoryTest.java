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
 * This class tests the GetCategory Class
 * @author kennethlyon
 */
public class GetCategoryTest extends CategoryApiHelper {
    private Logger LOGGER = LoggerFactory.getLogger(GetCategoryTest.class);
    private static GetCategory getCategoryInstance;
    private static CategoryRepository categoryRepository;
    private static ArrayList<MockHttpServletRequest> validRequestList = new ArrayList();
    private static ArrayList<MockHttpServletRequest> errorRequestList = new ArrayList();
    private static ArrayList<String> validCategories;
    private static ArrayList<String> errorCategories;
    /**
     * First create a new Instance of GetCategory() object, then add new
     * category test cases to validRequestList and errorRequestList.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        getCategoryInstance = new GetCategory();
        categoryRepository=getCategoryInstance.getCategoryRepository();


        validCategories=new ArrayList<String>();
        validCategories.add("0`0`Physics`Homework, study groups, lab reports, etc, for physics II`[]");
        validCategories.add("1`0`chores`Any kind of household chores.`[]");
        validCategories.add("2`0`work`work related stuff only!`[]");
        validCategories.add("3`0`money`Anything related to money. Taxes, budgeting, student loans, etc.`[]");
        validCategories.add("4`0`Journal club`Tasks related to journal club`[]");
        validCategories.add("5`0`Organic Chemistry`Homework, study groups, lab reports, etc, for organic chemistry.`[]");

        errorCategories=new ArrayList<String>();
        errorCategories.add("06`0`Mikes work`This is for all of the work Mike does         `[0,1]");
        errorCategories.add("-1`0`Mikes home`This is for all of the chores Mike never does `[2,3]");

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
     * After doPost runs, empty the repository and set pertinent objects to null.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        for(Category category: CategoryApiHelper.toCategories(validCategories))
            categoryRepository.delete(category);
        getCategoryInstance = null;
        validRequestList = null;
    }

    /**
     * Loop though validRequestList and errorRequestList sending each
     * MockHttpServletRequest to GetCategory then forward responses to
     * validateDoPostValidResponse and validateDoPostErrorResponse
     * respectfully.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {

        for (MockHttpServletRequest request : validRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getCategoryInstance.doPost(request, response);
            validateDoPostValidResponse(response);
        }

        for (MockHttpServletRequest request : errorRequestList) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            getCategoryInstance.doPost(request, response);
            validateDoPostErrorResponse(response);
        }
    }
}

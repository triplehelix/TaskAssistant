package api.v1;
import api.v1.repo.CategoryRepository;

/**
 * CategoryRequestHandler contains, fields and methods that are common to
 * category APIs. All category APIs inherit CategoryRequestHandler.
 */
public class CategoryRequestHandler extends BaseRequestHandler {

    protected static CategoryRepository categoryRepository;

    static {
        categoryRepository = new CategoryRepository();
    }
}

package api.v1;

import api.v1.repo.CategoryRepository;
/**
 * The CategoryHadler Class provides tools to parse the input expected
 * for category manipulation.
 * 
 */
public class CategoryRequestHandler extends BaseRequestHandler{

    protected static CategoryRepository categoryRepository;
    static {
        categoryRepository= new CategoryRepository();
    }
}

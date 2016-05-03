package api.v1;
import api.v1.repo.TypeRepository;

/**
 * TypeRequestHandler contains, fields and methods that are common to
 * type APIs. All type APIs inherit TypeRequestHandler. 
 */
public class TypeRequestHandler extends BaseRequestHandler {

    protected static TypeRepository typeRepository;

    static {
        typeRepository = new TypeRepository();
    }
}

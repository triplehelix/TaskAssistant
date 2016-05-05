package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;


public interface Repository<T> {


	public void add(final T t) throws BusinessException, SystemException;
	public T get(final T t) throws BusinessException, SystemException;
	public void update(final T t) throws BusinessException, SystemException;
	public void delete(final T t) throws BusinessException, SystemException;

}

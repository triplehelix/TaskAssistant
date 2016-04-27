package api.v1.repo;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;

public interface Repository<T> {
	public void add(final T t) throws SystemException, BusinessException;
	public T get(final T t) throws SystemException, BusinessException;
	public void update(final T t) throws SystemException;
	public void delete(final T t) throws SystemException;
}

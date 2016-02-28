package api.v1.repo;

public interface Repository<T> {
	public void add(final T t) throws Exception;
	public T get(final T t) throws Exception;
	public void update(final T t) throws Exception;
	public void delete(final T t) throws Exception;
}

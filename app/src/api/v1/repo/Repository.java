package api.v1.repo;

public interface Repository<T> {
	
	public void add(final T t);
	public T get(final T t);
	public void update(final T t);
	public void delete(final T t);
}

package api.v1.repo;
import java.sql.SQLException;
public interface Repository<T> {
	public void add(final T t) throws SQLException;
	public T get(final T t) throws SQLException;
	public void update(final T t) throws SQLException;
	public void delete(final T t) throws SQLException;
}

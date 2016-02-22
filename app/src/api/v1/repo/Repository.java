package api.v1.repo;

import java.util.Map;

public interface Repository<T> {
	//public <E> get(<E> e);
	//public void create(Object o);
	//public Map<T> myMap;
	public void add(Object o);
	public Object get(Object o);
	public void update(Object o);
	
}

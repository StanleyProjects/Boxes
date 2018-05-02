package stan.boxes;

import java.util.Map;

public interface ORM<T>
{
	Map<String, Object> write(T data);
	T read(Map<String, Object> map);
}
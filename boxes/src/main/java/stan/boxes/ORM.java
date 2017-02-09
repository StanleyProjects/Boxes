package stan.boxes;

import java.util.Map;

public interface ORM<DATA>
{
	Map write(DATA data);
	DATA read(Map map);
}
package stan.boxes;

public interface Query<DATA>
{
	boolean query(DATA data);
}
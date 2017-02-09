package stan.boxes;

public class Range
{
	private int start;
	private int count;

	public Range(int s, int c)
	{
		start = s;
		count = c;
	}

	public int getStart()
	{
		return start;
	}
	public int getCount()
	{
		return count;
	}
}
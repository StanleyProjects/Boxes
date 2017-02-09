package stan.boxes;

public class Transaction
{
    private int count;
    private long date;
    private int id;

    public Transaction(int i, int c, long d)
    {
        id = i;
        count = c;
        date = d;
    }

    public int getId()
    {
        return id;
    }
    public long getDate()
    {
        return date;
    }
    public int getCount()
    {
        return count;
    }
}
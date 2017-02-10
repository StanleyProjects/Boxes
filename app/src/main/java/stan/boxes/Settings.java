package stan.boxes;

public class Settings
{
    private long time;
    private String name;
    private int color;

    public Settings(long t, String n, int c)
    {
        time = t;
        name = n;
        color = c;
    }

    public long getTime()
    {
        return time;
    }
    public String getName()
    {
        return name;
    }
    public int getColor()
    {
        return color;
    }
}
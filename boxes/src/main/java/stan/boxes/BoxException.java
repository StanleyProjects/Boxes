package stan.boxes;

public class BoxException
    extends RuntimeException
{
    BoxException(Exception e)
    {
        super(e);
    }
    BoxException(String s)
    {
        super(s);
    }
}
package stan.boxes;

public class BoxException
    extends RuntimeException
{
    BoxException(Exception e)
    {
        super(e);
    }
}
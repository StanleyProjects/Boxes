package stan.boxes.utils;

import java.nio.charset.Charset;
import java.util.Random;

public abstract class MainTest
{
    static private final Random random = new Random();

    protected boolean nextBoolean()
    {
        return random.nextBoolean();
    }
    protected final int nextInt()
    {
        return random.nextInt();
    }
    protected final int nextInt(int range)
    {
        return random.nextInt(range);
    }
    protected final long nextLong()
    {
        return random.nextInt();
    }
    protected final double nextDouble()
    {
        return random.nextDouble() + nextInt();
    }
    protected final String nextString()
    {
        byte[] array = new byte[random.nextInt(50)+10];
        random.nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}
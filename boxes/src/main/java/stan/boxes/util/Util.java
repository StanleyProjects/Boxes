package stan.boxes.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public final class Util
{
    static public void write(Writer writer, String data) throws IOException
    {
        if(writer == null) throw new IllegalArgumentException();
        if(data == null) throw new IllegalArgumentException();
        try
        {
            writer.write(data);
        }
        finally
        {
            writer.close();
        }
    }
    static public String read(Reader reader) throws IOException
    {
        if(reader == null) throw new IllegalArgumentException();
        try
        {
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while(line != null)
            {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        }
        finally
        {
            reader.close();
        }
    }

    private Util()
    {
        throw new IllegalStateException();
    }
}

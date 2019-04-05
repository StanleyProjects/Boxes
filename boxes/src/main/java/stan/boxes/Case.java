package stan.boxes;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import stan.boxes.json.JSONParser;
import stan.boxes.json.JSONWriter;
import stan.boxes.json.ParseException;
import stan.boxes.util.Util;

public class Case<T>
{
    private final T def;
    private final ORM<T> orm;
    private final String fullPath;

    public Case(T d, ORM<T> o, String path)
    {
        if(o == null) throw new BoxException("Property \"orm\" must be exist!");
        if(path == null) throw new BoxException("Property \"path\" must be exist!");
        def = d;
        orm = o;
        fullPath = path;
        createNewFile();
    }

    public T get()
    {
        T data;
        try
        {
            String json = read(fullPath);
            Map<String, Object> map = (Map<String, Object>)JSONParser.read(json);
            Map<String, Object> convert = (Map<String, Object>)map.get("data");
            data = orm.read(convert);
        }
        catch(ParseException e)
        {
            save(def);
            data = def;
        }
        catch(Exception e)
        {
            throw new BoxException(e);
        }
        return data;
    }
    public void save(T data)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", orm.write(data));
        map.put("date", System.currentTimeMillis());
        write(fullPath, JSONWriter.write(map));
    }
    public void clear()
    {
        save(def);
    }

    private void createNewFile()
    {
        File file = new File(fullPath);
        if(file.exists()) return;
        File parent = file.getParentFile();
        if(parent != null && !parent.exists()) parent.mkdirs();
        try
        {
            file.createNewFile();
        }
        catch(IOException e)
        {
            throw new BoxException(e);
        }
        save(def);
    }

    private void write(String filePath, String data)
    {
        try
        {
            Util.write(new FileWriter(filePath), data);
        }
        catch(IOException e)
        {
            throw new BoxException(e);
        }
    }
    private String read(String filePath)
    {
        try
        {
            return Util.read(new FileReader(filePath));
        }
        catch(IOException e)
        {
            throw new BoxException(e);
        }
    }
}
package stan.boxes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import stan.boxes.json.JSONParser;
import stan.boxes.json.JSONWriter;
import stan.boxes.json.ParseException;

public class Case<DATA>
{
    private final DATA def;
    private final ORM<DATA> orm;
    private final String fullPath;

    public Case(DATA d, ORM<DATA> o, String fp)
    {
        def = d;
        orm = o;
        fullPath = fp;
        createNewFile();
    }
    private void createNewFile()
    {
        File caseFile = new File(fullPath);
        if(!caseFile.exists())
        {
            try
            {
                caseFile.createNewFile();
                save(def);
            }
            catch(IOException e)
            {
            }
        } 
    }

    public DATA get()
    {
        DATA data = null;
        try
        {
            String json = read(fullPath);
            Map map = (Map)JSONParser.read(json);
            Map convert = (Map)map.get("data");
            data = orm.read(convert);
        }
        catch(ParseException e)
        {
            save(def);
        }
        catch(IOException e)
        {
        }
        return data;
    }
    public void save(DATA data)
    {
        Map map = new HashMap();
        map.put("data", orm.write(data));
        map.put("date", System.currentTimeMillis());
        try
        {
            write(fullPath, JSONWriter.write(map));
        }
        catch(IOException ex)
        {
        }
    }
    public void clear()
    {
        save(def);
    }

    private void write(String fp, String data) throws IOException
    {
        FileWriter fw = null;
        try
        { 
            fw = new FileWriter(fp);
            fw.write(data);
        }
        finally
        {
            fw.close();
        }
    }
    private String read(String fp) throws IOException
    {
        FileReader fr = null;
        try
        {
            fr = new FileReader(fp);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line != null)
            {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        }
        finally
        {
            fr.close();
        }
    }
}
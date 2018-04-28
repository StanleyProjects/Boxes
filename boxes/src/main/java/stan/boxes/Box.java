package stan.boxes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stan.boxes.json.JSONParser;
import stan.boxes.json.JSONWriter;
import stan.boxes.json.ParseException;

public class Box<T>
{
    private final ORM<T> orm;
    private final String fullPath;

    public Box(ORM<T> o, String fp)
    {
        orm = o;
        fullPath = fp;
        createNewFile();
    }
    private void createNewFile()
    {
        File boxFile = new File(fullPath);
        if(!boxFile.exists())
        {
            try
            {
                boxFile.createNewFile();
            }
            catch(IOException e)
            {
                throw new BoxException(e);
            }
            writeEmpty();
        } 
    }

    public List getRaw()
    {
        List convert;
        try
        {
            convert = (List)((Map)JSONParser.read(read(fullPath))).get("list");
        }
        catch(IOException e)
        {
            throw new BoxException(e);
        }
        catch(ParseException e)
        {
            writeEmpty();
            return Collections.emptyList();
        }
        return convert;
    }
    public List<T> getAll()
    {
        List convert = getRaw();
        if(convert.isEmpty())
        {
            return new ArrayList<T>();
        }
        List<T> list = new ArrayList<T>(convert.size());
        for(Object item: convert)
        {
            list.add(orm.read((Map)item));
        }
        return list;
    }
    public List<T> get(Query<T> query)
    {
        List convert = getRaw();
        if(convert.isEmpty())
        {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<T>(convert.size());
        for(Object item: convert)
        {
            T tmp = orm.read((Map)item);
            if(query.query(tmp))
            {
                list.add(tmp);
            }
        }
        return list;
    }
    public List<T> get(Comparator<T> comparator)
    {
        List<T> list = getAll();
        Collections.sort(list, comparator);
        return list;
    }
    public List<T> get(Query<T> query, Comparator<T> comparator)
    {
        List<T> list = get(query);
        Collections.sort(list, comparator);
        return list;
    }
    public List<T> get(Query<T> query, Range range)
    {
        List<T> list = get(query);
        return list.subList(range.start, range.start + range.count);
    }
    public List<T> get(Query<T> query, Comparator<T> comparator, Range range)
    {
        List<T> list = get(query, comparator);
        return list.subList(range.start, range.start + range.count);
    }
    public void add(T data, T... datas)
    {
        List<T> list = getAll();
        list.add(data);
        if(datas != null && datas.length > 0)
        {
            Collections.addAll(list, datas);
        }
        save(list);
    }
    public void addAll(Collection<T> datas)
    {
        if(datas == null || datas.isEmpty())
        {
            return;
        }
        List<T> list = getAll();
        list.addAll(datas);
        save(list);
    }
    public void update(Query<T> query, T data)
    {
        List<T> list = getAll();
        for(int i=0; i<list.size(); i++)
        {
            if(query.query(list.get(i)))
            {
                list.set(i, data);
                break;
            }
        }
        save(list);
    }
    public void updateAll(Query<T> query, T data)
    {
        List<T> list = getAll();
        for(int i=0; i<list.size(); i++)
        {
            if(query.query(list.get(i)))
            {
                list.set(i, data);
            }
        }
        save(list);
    }
    public void removeFirst(Query<T> query)
    {
        List<T> list = getAll();
        for(int i=0; i<list.size(); i++)
        {
            if(query.query(list.get(i)))
            {
                list.remove(i);
                break;
            }
        }
        save(list);
    }
    public void removeAll(Query<T> query)
    {
        List<T> list = getAll();
        int i=0;
        while(i<list.size())
        {
            if(query.query(list.get(i)))
            {
                list.remove(i);
            }
            else
            {
                i++;
            }
        }
        save(list);
    }
    public void clear()
    {
        writeEmpty();
    }
    private void writeData(Object object)
    {
        Map map = new HashMap();
        map.put("list", object);
        map.put("date", System.currentTimeMillis());
        try
        {
            write(fullPath, JSONWriter.write(map));
        }
        catch(IOException e)
        {
            throw new BoxException(e);
        }
    }
    private void writeEmpty()
    {
        writeData(new Object[]{});
    }
    private void save(List<T> list)
    {
        List convert = new ArrayList(list.size());
        for(T item : list)
        {
            convert.add(orm.write(item));
        }
        writeData(convert);
    }

    synchronized private void write(String path, String data) throws IOException
    {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter(path);
            fileWriter.write(data);
        }
        finally
        {
            if(fileWriter != null)
            {
                fileWriter.close();
            }
        }
    }
    synchronized private String read(String path)
        throws IOException
    {
        FileReader fileReader = null;
        try
        {
            fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
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
            if(fileReader != null)
            {
                fileReader.close();
            }
        }
    }
}
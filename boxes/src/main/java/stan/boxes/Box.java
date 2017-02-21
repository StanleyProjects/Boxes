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

public class Box<DATA>
{
    private final ORM<DATA> orm;
    private final String fullPath;

    public Box(ORM<DATA> o, String fp)
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
                Map map = new HashMap();
                map.put("list", new Object[]{});
                map.put("date", System.currentTimeMillis());
                write(fullPath, JSONWriter.write(map));
            }
            catch(IOException e)
            {
            }
        } 
    }

    public List<DATA> getAll()
    {
        try
        {
            String data = read(fullPath);
            Map map = (Map)JSONParser.read(data);
            List convert = (List)map.get("list");
            List<DATA> list = new ArrayList<DATA>(convert.size());
            for(int i=0; i<convert.size(); i++)
            {
                list.add(orm.read((Map)convert.get(i)));
            }
            return list;
        }
        catch(ParseException e)
        {
            try
            {
                Map map = new HashMap();
                map.put("list", new Object[]{});
                map.put("date", System.currentTimeMillis());
                write(fullPath, JSONWriter.write(map));
            }
            catch(IOException ex)
            {
            }
        }
        catch(Exception e)
        {
        }
        return new ArrayList<DATA>();
    }

    public List<DATA> get(Query<DATA> query)
    {
        try
        {
            String data = read(fullPath);
            Map map = (Map)JSONParser.read(data);
            List convert = (List)map.get("list");
            List<DATA> list = new ArrayList<DATA>(convert.size());
            for(int i=0; i<convert.size(); i++)
            {
                DATA tmp = orm.read((Map)convert.get(i));
                if(query.query(tmp))
                {
                    list.add(tmp); 
                }
            }
            return list;
        }
        catch(ParseException e)
        {
            try
            {
                Map map = new HashMap();
                map.put("list", new Object[]{});
                map.put("date", System.currentTimeMillis());
                write(fullPath, JSONWriter.write(map));
            }
            catch(IOException ex)
            {
            }
        }
        catch(Exception e)
        {
        }
        return new ArrayList<DATA>();
    }
    public List<DATA> get(Comparator<DATA> comparator)
    {
        List<DATA> list = getAll();
        Collections.sort(list, comparator);
        return list;
    }
    public List<DATA> get(Query<DATA> query, Comparator<DATA> comparator)
    {
        List<DATA> list = get(query);
        Collections.sort(list, comparator);
        return list;
    }
    public List<DATA> get(Query<DATA> query, Range range)
    {
        List<DATA> list = get(query);
        return list.subList(range.getStart(), range.getStart() + range.getCount());
    }
    public List<DATA> get(Query<DATA> query, Comparator<DATA> comparator, Range range)
    {
        List<DATA> list = get(query, comparator);
        return list.subList(range.getStart(), range.getStart() + range.getCount());
    }
    public void add(DATA... datas)
    {
        if(datas == null || datas.length == 0)
        {
            return;
        }
        List<DATA> list = getAll();
        Collections.addAll(list, datas);
        save(list);
    }
    public void add(Collection<DATA> datas)
    {
        if(datas == null || datas.size() == 0)
        {
            return;
        }
        List<DATA> list = getAll();
        list.addAll(datas);
        save(list);
    }
    public void replace(Query<DATA> query, DATA data)
    {
        List<DATA> list = getAll();
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
    public void removeFirst(Query<DATA> query)
    {
        List<DATA> list = getAll();
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
    public void removeAll(Query<DATA> query)
    {
        List<DATA> list = getAll();
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
        Map map = new HashMap();
        map.put("list", new Object[]{});
        map.put("date", System.currentTimeMillis());
        try
        {
            write(fullPath, JSONWriter.write(map));
        }
        catch(IOException ex)
        {
        }
    }
    private void save(List<DATA> list)
    {
        List convert = new ArrayList(list.size());
        for(int i=0; i<list.size(); i++)
        {
            convert.add(orm.write(list.get(i)));
        }
        Map map = new HashMap();
        map.put("list", convert);
        map.put("date", System.currentTimeMillis());
        try
        {
            write(fullPath, JSONWriter.write(map));
        }
        catch(IOException ex)
        {
        }
    }

    private void write(String fp, String data) throws IOException
    {
        synchronized(this)
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
    }
    private String read(String fp) throws IOException
    {
        synchronized(this)
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
}
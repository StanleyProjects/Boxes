package stan.boxes.reactive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

import stan.boxes.ORM;
import stan.boxes.Query;
import stan.boxes.Range;
import stan.boxes.json.JSONParser;
import stan.boxes.json.JSONWriter;
import stan.boxes.json.ParseException;

public class ReactiveBox<DATA>
{
    private final List<DATA> allData;
    private final ORM<DATA> orm;
    private final String fullPath;
    private final JSONParser parser = new JSONParser();
    private final Queue<Runnable> runnableQueue = new PriorityQueue<Runnable>();
    private final Runnable saveWork = new Runnable()
    {
        public void run()
        {
            save();
        }
    };

    public ReactiveBox(ORM<DATA> o, String fp)
    {
        orm = o;
        fullPath = fp;
        createNewFile();
        allData = getAllData();
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
    private List<DATA> getAllData()
    {
        try
        {
            String data = read(fullPath);
            Map map = (Map)parser.parse(data);
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
        catch(IOException e)
        {
        }
        return new ArrayList<DATA>();
    }

    public ListModel<DATA> getAll()
    {
        return new ArrayListModel<DATA>(allData);
    }

    public ListModel<DATA> get(Query<DATA> query)
    {
        return new ArrayListModel<DATA>(query(query));
    }
    public ListModel<DATA> get(Query<DATA> query, Comparator<DATA> comparator)
    {
        List<DATA> list = query(query);
        Collections.sort(list, comparator);
        return new ArrayListModel<DATA>(list);
    }
    public ListModel<DATA> get(Query<DATA> query, Range range)
    {
        List<DATA> list = query(query);
        return new ArrayListModel<DATA>(list.subList(range.getStart(), range.getStart() + range.getCount()));
    }
    public ListModel<DATA> get(Query<DATA> query, Comparator<DATA> comparator, Range range)
    {
        List<DATA> list = query(query);
        Collections.sort(list, comparator);
        return new ArrayListModel<DATA>(list.subList(range.getStart(), range.getStart() + range.getCount()));
    }
    private List<DATA> query(Query<DATA> query)
    {
        List<DATA> list = new ArrayList<DATA>();
        for(int i=0; i<allData.size(); i++)
        {
            if(query.query(allData.get(i)))
            {
                list.add(allData.get(i)); 
            }
        }
        return list;
    }

    public void add(DATA... datas)
    {
        if(datas == null || datas.length == 0)
        {
            return;
        }
        synchronized(allData)
        {
            for(DATA d : datas)
            {
                allData.add(d);
            }
        }
        addWork(saveWork);
    }
    public void replace(Query<DATA> query, DATA data)
    {
        boolean replace = false;
        for(int i=0; i<allData.size(); i++)
        {
            if(query.query(allData.get(i)))
            {
                allData.set(i, data);
                replace = true;
                break;
            }
        }
        if(replace)
        {
            addWork(saveWork);
        }
    }
    public void removeFirst(Query<DATA> query)
    {
        boolean remove = false;
        for(int i=0; i<allData.size(); i++)
        {
            if(query.query(allData.get(i)))
            {
                allData.remove(i);
                remove = true;
                break;
            }
        }
        if(remove)
        {
            addWork(saveWork);
        }
    }
    public void removeAll(Query<DATA> query)
    {
        boolean remove = false;
        int i=0;
        while(i<allData.size())
        {
            if(query.query(allData.get(i)))
            {
                allData.remove(i);
                remove = true;
            }
            else
            {
                i++;
            }
        }
        if(remove)
        {
            addWork(saveWork);
        }
    }
    public void clear()
    {
        synchronized(allData)
        {
            allData.clear();
        }
        addWork(saveWork);
    }
    private void save()
    {
        List convert = new ArrayList(allData.size());
        synchronized(allData)
        {
            for(int i=0; i<allData.size(); i++)
            {
                convert.add(orm.write(allData.get(i)));
            }
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

    private void addWork(Runnable runnable)
    {
        if(runnableQueue.isEmpty())
        {
            runnableQueue.offer(runnable);
            try
            {
                make(runnableQueue.remove());
            }
            catch(NoSuchElementException e)
            {

            }
        }
        else
        {
            runnableQueue.offer(runnable);
        }
    }
    private void make(Runnable runnable)
    {
        new Thread(runnable)
        {
            public void run()
            {
                super.run();
                try
                {
                    make(runnableQueue.remove());
                }
                catch(NoSuchElementException e)
                {

                }
            }
        }.start();
    }
}
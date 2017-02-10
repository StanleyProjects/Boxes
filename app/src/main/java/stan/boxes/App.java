package stan.boxes;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class App
{
    static private Box<Transaction> transactionsBox;
    static private Case<Settings> settingsCase;
    static public void main(String[] args)
    {
        log("\n\tbegin");
    	String path = System.getProperty("user.home")+"/stan/boxes";
    	new File(path).mkdirs();
    	/*
        transactionsBox = new Box<Transaction>(new ORM<Transaction>()
			{
				public Map write(Transaction data)
				{
        			Map map = new HashMap();
        			map.put("id", data.getId());
        			map.put("count", data.getCount());
        			map.put("date", data.getDate());
					return map;
				}
				public Transaction read(Map map)
				{
					return new Transaction(
							Long.valueOf((Long)map.get("id")).intValue()
							,Long.valueOf((Long)map.get("count")).intValue()
							,(Long)map.get("date"));
				}
			}, path+"/transactionsbox");
        log("size: " + transactionsBox.getAll().size());
        List<Transaction> transactions = transactionsBox.getAll();
        for(int i=0; i<transactions.size(); i++)
        {
        	log("\ttransaction:"
        		+"\n\t\tid " + transactions.get(i).getId()
        		+"\n\t\tcount " + transactions.get(i).getCount()
        		+"\n\t\tdate " + transactions.get(i).getDate()
        		);
        }
        */
        settingsCase = new Case<Settings>(new Settings(0, null, 0), new ORM<Settings>()
			{
				public Map write(Settings data)
				{
        			Map map = new HashMap();
        			map.put("time", data.getTime());
        			map.put("name", data.getName());
        			map.put("color", data.getColor());
					return map;
				}
				public Settings read(Map map)
				{
					return new Settings((Long)map.get("time")
						,(String)map.get("name")
						,Long.valueOf((Long)map.get("color")).intValue());
				}
			}, path+"/settingscase");
        Settings settings = settingsCase.get();
        	log("\tsettings:"
        		+"\n\t\ttime " + settings.getTime()
        		+"\n\t\tname " + settings.getName()
        		+"\n\t\tcolor " + settings.getColor()
        		);
        clearSettings();
        //save();
        //clearBox();
        //add();
        //query();
        //queryOrder();
        //queryRange();
        //queryOrderRange();
        //replace();
        //removeFirst();
        //removeAll();
        log("\n\tend");
    }
    static private void save()
    {
        settingsCase.save(new Settings(System.currentTimeMillis(), nextString(), nextInt()));
        Settings settings = settingsCase.get();
        	log("\tsettings after save:"
        		+"\n\t\ttime " + settings.getTime()
        		+"\n\t\tname " + settings.getName()
        		+"\n\t\tcolor " + settings.getColor()
        		);
    }
    static private void clearSettings()
    {
        settingsCase.clear();
        Settings settings = settingsCase.get();
        	log("\tsettings after clear:"
        		+"\n\t\ttime " + settings.getTime()
        		+"\n\t\tname " + settings.getName()
        		+"\n\t\tcolor " + settings.getColor()
        		);
    }
    static private void add()
    {
        transactionsBox.add(
        	new Transaction(nextInt(),nextInt(100),System.currentTimeMillis())
        	,new Transaction(nextInt(),nextInt(100),System.currentTimeMillis())
        	,new Transaction(nextInt(),nextInt(100),System.currentTimeMillis())
        	,new Transaction(nextInt(),nextInt(100),System.currentTimeMillis())
        	,new Transaction(nextInt(),nextInt(100),System.currentTimeMillis())
        	,new Transaction(nextInt(),nextInt(100),System.currentTimeMillis())
        	);
        log("size after add: " + transactionsBox.getAll().size());
    }
    static private void query()
    {
        List<Transaction> transactions = transactionsBox.get(new Query<Transaction>()
        	{
        		public boolean query(Transaction transaction)
        		{
        			return transaction.getCount() < 50;
        		}
        	});
        log("size after query: " + transactions.size());
        for(int i=0; i<transactions.size(); i++)
        {
        	log("\ttransaction:"
        		+"\n\t\tid " + transactions.get(i).getId()
        		+"\n\t\tcount " + transactions.get(i).getCount()
        		+"\n\t\tdate " + transactions.get(i).getDate()
        		);
        }
    }
    static private void queryOrder()
    {
        List<Transaction> transactions = transactionsBox.get(new Query<Transaction>()
        	{
        		public boolean query(Transaction transaction)
        		{
        			return transaction.getCount() < 50;
        		}
        	}, new Comparator<Transaction>()
	        {
	            @Override
	            public int compare(Transaction t1, Transaction t2)
	            {
	                if(t1.getCount() < t2.getCount())
	                {
	                    return -1;
	                }
	                else if(t1.getCount() > t2.getCount())
	                {
	                    return 1;
	                }
	                else
	                {
	                    return 0;
	                }
	            }
	        });
        log("size after query: " + transactions.size());
        for(int i=0; i<transactions.size(); i++)
        {
        	log("\ttransaction:"
        		+"\n\t\tid " + transactions.get(i).getId()
        		+"\n\t\tcount " + transactions.get(i).getCount()
        		+"\n\t\tdate " + transactions.get(i).getDate()
        		);
        }
    }
    static private void queryRange()
    {
        List<Transaction> transactions = transactionsBox.get(new Query<Transaction>()
        	{
        		public boolean query(Transaction transaction)
        		{
        			return transaction.getCount() < 50;
        		}
        	}, new Range(1, 3));
        log("size after query: " + transactions.size());
        for(int i=0; i<transactions.size(); i++)
        {
        	log("\ttransaction:"
        		+"\n\t\tid " + transactions.get(i).getId()
        		+"\n\t\tcount " + transactions.get(i).getCount()
        		+"\n\t\tdate " + transactions.get(i).getDate()
        		);
        }
    }
    static private void queryOrderRange()
    {
        List<Transaction> transactions = transactionsBox.get(new Query<Transaction>()
        	{
        		public boolean query(Transaction transaction)
        		{
        			return transaction.getCount() < 50;
        		}
        	}, new Comparator<Transaction>()
	        {
	            @Override
	            public int compare(Transaction t1, Transaction t2)
	            {
	                if(t1.getCount() < t2.getCount())
	                {
	                    return -1;
	                }
	                else if(t1.getCount() > t2.getCount())
	                {
	                    return 1;
	                }
	                else
	                {
	                    return 0;
	                }
	            }
	        }, new Range(1, 3));
        log("size after query: " + transactions.size());
        for(int i=0; i<transactions.size(); i++)
        {
        	log("\ttransaction:"
        		+"\n\t\tid " + transactions.get(i).getId()
        		+"\n\t\tcount " + transactions.get(i).getCount()
        		+"\n\t\tdate " + transactions.get(i).getDate()
        		);
        }
    }
    static private void replace()
    {
        transactionsBox.add(new Transaction(1,nextInt(100),System.currentTimeMillis()));
        transactionsBox.add(new Transaction(2,nextInt(100),System.currentTimeMillis()));
        transactionsBox.add(new Transaction(3,nextInt(100),System.currentTimeMillis()));
        transactionsBox.add(new Transaction(4,nextInt(100),System.currentTimeMillis()));
        log("size after add: " + transactionsBox.getAll().size());
        List<Transaction> transactions = transactionsBox.getAll();
        for(int i=0; i<transactions.size(); i++)
        {
        	log("\ttransaction:"
        		+"\n\t\tid " + transactions.get(i).getId()
        		+"\n\t\tcount " + transactions.get(i).getCount()
        		+"\n\t\tdate " + transactions.get(i).getDate()
        		);
        }
        transactionsBox.replace(new Query<Transaction>()
        	{
        		public boolean query(Transaction transaction)
        		{
        			return transaction.getId() == 3;
        		}
        	}, new Transaction(3,-25,-100));
        log("size after replace: " + transactionsBox.getAll().size());
        transactions = transactionsBox.getAll();
        for(int i=0; i<transactions.size(); i++)
        {
        	log("\ttransaction:"
        		+"\n\t\tid " + transactions.get(i).getId()
        		+"\n\t\tcount " + transactions.get(i).getCount()
        		+"\n\t\tdate " + transactions.get(i).getDate()
        		);
        }
    }
    static private void removeFirst()
    {
        transactionsBox.removeFirst(new Query<Transaction>()
        	{
        		public boolean query(Transaction transaction)
        		{
        			return transaction.getId() == 1748145049;
        		}
        	});
        log("size after remove: " + transactionsBox.getAll().size());
        List<Transaction> transactions = transactionsBox.getAll();
        for(int i=0; i<transactions.size(); i++)
        {
        	log("\ttransaction:"
        		+"\n\t\tid " + transactions.get(i).getId()
        		+"\n\t\tcount " + transactions.get(i).getCount()
        		+"\n\t\tdate " + transactions.get(i).getDate()
        		);
        }
    }
    static private void removeAll()
    {
        transactionsBox.removeAll(new Query<Transaction>()
        	{
        		public boolean query(Transaction transaction)
        		{
        			return transaction.getDate() > 0;
        		}
        	});
        log("size after remove: " + transactionsBox.getAll().size());
        List<Transaction> transactions = transactionsBox.getAll();
        for(int i=0; i<transactions.size(); i++)
        {
        	log("\ttransaction:"
        		+"\n\t\tid " + transactions.get(i).getId()
        		+"\n\t\tcount " + transactions.get(i).getCount()
        		+"\n\t\tdate " + transactions.get(i).getDate()
        		);
        }
    }
    static private void clearBox()
    {
    	transactionsBox.clear();
        log("size after clear: " + transactionsBox.getAll().size());
    }

    static private final Random random = new Random();
    static private void log(String m)
    {
        System.err.println(m);
    }
    static private boolean nextBoolean()
    {
        return random.nextBoolean();
    }
    static private int nextInt()
    {
        return nextInt(Integer.MAX_VALUE-2)+1;
    }
    static private int nextInt(int range)
    {
        return random.nextInt(range);
    }
    static private String nextString()
    {
        byte[] array = new byte[random.nextInt(99)+1];
        random.nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}
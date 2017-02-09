package stan.boxes;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class App
{
    static private Box<Transaction> transactionsBox;
    static public void main(String[] args)
    {
        log("\n\tbegin");
    	String fullPath = "C:/Users/toha/data/stanleyprojects/boxes/transactionsbox";
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
					Transaction transaction = new Transaction(
							Long.valueOf((Long)map.get("id")).intValue()
							,Long.valueOf((Long)map.get("count")).intValue()
							,(Long)map.get("date")
						);
					return transaction;
				}
			}, fullPath);
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
        //add();
        //query();
        //queryOrder();
        //queryRange();
        //queryOrderRange();
        //removeFirst();
        //removeAll();
        log("\n\tend");
    }
    static private void add()
    {
        Transaction transaction = new Transaction(nextInt(),nextInt(100),System.currentTimeMillis());
        transactionsBox.add(transaction);
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
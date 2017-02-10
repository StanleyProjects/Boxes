# Boxes
Object-Relational Mapping experiment

<img src="media/icon.png" width="128" height="128" />

# Usage

##### Example 1. Create **Box**

```java
Box<Transaction> transactionsBox = new Box<Transaction>(new ORM<Transaction>()
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
		return new Transaction(Long.valueOf((Long)map.get("id")).intValue()
				,Long.valueOf((Long)map.get("count")).intValue()
				,(Long)map.get("date"));
	}
}, "/home/user/stan/boxes/transactionsbox");
```

##### Example 2. Put objects to **Box**

```java
transactionsBox.add(new Transaction(1, 100, System.currentTimeMillis())
	,new Transaction(2, 58, System.currentTimeMillis())
	,new Transaction(3, -45, System.currentTimeMillis())
	,new Transaction(4, 23, System.currentTimeMillis())
	,new Transaction(5, -78, System.currentTimeMillis()));
```

##### Example 3. Get list objects from **Box**

```java
List<Transaction> transactions = transactionsBox.getAll();
```

##### Example 4. Get list objects from **Box** with query

```java
List<Transaction> transactions = transactionsBox.get(new Query<Transaction>()
{
	public boolean query(Transaction transaction)
	{
		return transaction.getCount() < 50;
	}
});
```

##### Example 5. Replace object in **Box**

```java
transactionsBox.replace(new Query<Transaction>()
	{
		public boolean query(Transaction transaction)
		{
			return transaction.getId() == 3;
		}
	}, new Transaction(3, -25, System.currentTimeMillis()));
```


##### Example 6. Remove object from **Box**

```java
transactionsBox.removeFirst(new Query<Transaction>()
	{
		public boolean query(Transaction transaction)
		{
			return transaction.getId() == 1748145049;
		}
	});
```

##### And some more features...

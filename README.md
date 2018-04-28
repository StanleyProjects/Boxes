# Boxes
Object-Relational Mapping experiment

<img src="media/icon.png" width="128" height="128" />

# Box

##### Example 1.1. Create *Box*

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
		return new Transaction(((Long)map.get("id")).intValue(),
		    ((Long)map.get("count")).intValue(),
		    (Long)map.get("date"));
	}
}, "/home/user/stan/boxes/transactionsbox");
```

##### Example 1.2. Put objects to *Box*

```java
transactionsBox.add(new Transaction(1, 100, System.currentTimeMillis()),
    new Transaction(2, 58, System.currentTimeMillis()),
	new Transaction(3, -45, System.currentTimeMillis()),
	new Transaction(4, 23, System.currentTimeMillis()),
	new Transaction(5, -78, System.currentTimeMillis()));
```

##### Example 1.3. Get list objects from *Box*

```java
List<Transaction> transactions = transactionsBox.getAll();
```

##### Example 1.4. Get list objects from *Box* with query

```java
List<Transaction> transactions = transactionsBox.get(new Query<Transaction>()
{
	public boolean query(Transaction transaction)
	{
		return transaction.getCount() < 50;
	}
});
```

##### Example 1.5. Replace object in *Box*

```java
transactionsBox.replace(new Query<Transaction>()
{
	public boolean query(Transaction transaction)
	{
		return transaction.getId() == 3;
	}
}, new Transaction(3, -25, System.currentTimeMillis()));
```

##### Example 1.6. Remove object from *Box*

```java
transactionsBox.removeFirst(new Query<Transaction>()
{
	public boolean query(Transaction transaction)
	{
		return transaction.getId() == 1748145049;
	}
});
```

# Case

##### Example 2.1. Create *Case*

```java
Case<Settings> settingsCase = new Case<Settings>(new Settings(0, null, 0), new ORM<Settings>()
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
		return new Settings((Long)map.get("time"),
		    (String)map.get("name"),
		    Long.valueOf((Long)map.get("color")).intValue());
	}
}, "/home/user/stan/boxes/settingscase");
```

##### Example 2.2. Get object from *Case*

```java
Settings settings = settingsCase.get();
```

##### Example 2.3. Rewrite data in *Case*

```java
settingsCase.save(new Settings(System.currentTimeMillis(), "sometext", 1234));
```

##### And some more features...

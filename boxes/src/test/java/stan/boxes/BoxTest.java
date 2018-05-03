package stan.boxes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stan.boxes.utils.MainTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BoxTest
    extends MainTest
{
    static private final Comparator<TestData> testDataNumberComparator = new Comparator<TestData>()
    {
        public int compare(TestData o1, TestData o2)
        {
            return o1.number < o2.number ? -1 : o1.number == o2.number ? 0 : 1;
        }
    };

    private Box<TestData> box;

    @Before
    public void before()
    {
        box = new Box<TestData>(new ORM<TestData>()
        {
            private final String NUMBER = "number";
            private final String STRING = "string";
            private final String BOOL = "bool";
            private final String DATE = "date";
            private final String DBL = "dbl";

            public Map<String, Object> write(TestData data)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(NUMBER, data.number);
                map.put(STRING, data.string);
                map.put(BOOL, data.bool);
                map.put(DATE, data.date);
                map.put(DBL, data.dbl);
                return map;
            }
            public TestData read(Map<String, Object> map)
            {
                return new TestData(((Long)map.get(NUMBER)).intValue(),
                    (String)map.get(STRING),
                    (Boolean)map.get(BOOL),
                    (Long)map.get(DATE),
                    (Double)map.get(DBL));
            }
        }, System.getProperty("user.home") + "/stan/boxes/test");
        box.clear();
    }

    @Test
    public void initTest()
    {
        List<TestData> list = box.getAll();
        assertNotNull("List of test datas must be exist!", list);
        assertTrue("List of test datas must be empty on init!", list.isEmpty());
    }
    @Test
    public void addTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        box.add(fakeTestData());
        assertEquals("Size of list of test datas must be equals 1!", 1, box.getAll().size());
    }
    @Test
    public void addAllTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>(count);
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        box.addAll(list);
        assertEquals("Size of list of test datas must be equals " + count + "!", count, box.getAll().size());
    }
    @Test
    public void getAllTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>(count);
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        box.addAll(list);
        List<TestData> result = box.getAll();
        assertEquals("Size of list of test datas must be equals " + count + "!", count, result.size());
        for(TestData item: list)
        {
            assertTrue("Result list must contains item " + item, result.contains(item));
        }
    }
    @Test
    public void getQueryTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>();
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        int specialCount = nextInt(10) + 10;
        final String specialString = nextString();
        List<TestData> specialList = new ArrayList<TestData>(specialCount);
        for(int i=0; i<specialCount; i++)
        {
            specialList.add(new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble()));
        }
        list.addAll(specialList);
        box.addAll(list);
        List<TestData> result = box.get(new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.string.equals(specialString);
            }
        });
        assertEquals("Size of list of test datas with special string "+specialString+" must be equals " + specialCount + "!", specialCount, result.size());
        for(TestData item: specialList)
        {
            assertTrue("Result list must contains item " + item, result.contains(item));
        }
    }
    @Test
    public void getComparatorTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 100;
        List<TestData> list = new ArrayList<TestData>(count);
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        box.addAll(list);
        List<TestData> result = box.get(testDataNumberComparator);
        assertEquals("Size of list of test datas must be equals " + count + "!", count, result.size());
        TestData previousItem = result.get(0);
        for(TestData item: result)
        {
            assertTrue("Item with number " + item.number + " must stand after item with number " + previousItem.number, previousItem.number <= item.number);
            previousItem = item;
        }
    }
    @Test
    public void getRangeTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(100) + 100;
        List<TestData> list = new ArrayList<TestData>(count);
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        box.addAll(list);
        int rangeStart = nextInt(25) + 25;
        int rangeCount = nextInt(25) + 25;
        List<TestData> result = box.get(new Range(rangeStart, rangeCount));
        List<TestData> subList = list.subList(rangeStart, rangeStart + rangeCount);
        assertEquals("Size of list of test datas must be equals " + subList.size() + "!", subList.size(), result.size());
        for(TestData item: result)
        {
            assertTrue("Result list must contains item " + item, subList.contains(item));
        }
    }
    @Test
    public void getQueryComparatorTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>();
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        int specialCount = nextInt(10) + 10;
        final String specialString = nextString();
        List<TestData> specialList = new ArrayList<TestData>(specialCount);
        for(int i=0; i<specialCount; i++)
        {
            specialList.add(new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble()));
        }
        list.addAll(specialList);
        box.addAll(list);
        List<TestData> result = box.get(new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.string.equals(specialString);
            }
        }, new Comparator<TestData>()
        {
            public int compare(TestData o1, TestData o2)
            {
                return o1.number < o2.number ? -1 : o1.number == o2.number ? 0 : 1;
            }
        });
        assertEquals("Size of list of test datas with special string "+specialString+" must be equals " + specialCount + "!", specialCount, result.size());
        TestData previousItem = result.get(0);
        for(TestData item: result)
        {
            assertTrue("Item with number " + item.number + " must stand after item with number " + previousItem.number, previousItem.number <= item.number);
            previousItem = item;
            assertTrue("Result list must contains item " + item, specialList.contains(item));
        }
    }
    @Test
    public void getQueryRangeTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(200) + 300;
        List<TestData> list = new ArrayList<TestData>();
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        int specialCount = nextInt(100) + 100;
        final String specialString = nextString();
        List<TestData> specialList = new ArrayList<TestData>(specialCount);
        for(int i=0; i<specialCount; i++)
        {
            specialList.add(new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble()));
        }
        list.addAll(specialList);
        box.addAll(list);
        int rangeStart = nextInt(25) + 25;
        int rangeCount = nextInt(25) + 25;
        List<TestData> result = box.get(new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.string.equals(specialString);
            }
        }, new Range(rangeStart, rangeCount));
        List<TestData> subList = specialList.subList(rangeStart, rangeStart + rangeCount);
        assertEquals("Size of list of test datas with special string "+specialString+" must be equals " + subList.size() + "!", subList.size(), result.size());
        for(TestData item: result)
        {
            assertTrue("Result list must contains item " + item, subList.contains(item));
        }
    }
    @Test
    public void getComparatorRangeTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(200) + 300;
        List<TestData> list = new ArrayList<TestData>(count);
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        Collections.shuffle(list);
        box.addAll(list);
        int rangeStart = nextInt(25) + 25;
        int rangeCount = nextInt(25) + 25;
        List<TestData> result = box.get(testDataNumberComparator, new Range(rangeStart, rangeCount));
        Collections.sort(list, testDataNumberComparator);
        List<TestData> subList = list.subList(rangeStart, rangeStart + rangeCount);
        assertEquals("Size of list of test datas must be equals " + subList.size() + "!", subList.size(), result.size());
        TestData previousItem = result.get(0);
        for(int i=0; i<subList.size(); i++)
        {
            assertEquals("Result item " + result.get(i)
                    + "must be equals item " + subList.get(i),
                subList.get(i), result.get(i));
            assertTrue("Item with number " + result.get(i).number
                    + " must stand after item with number " + result.get(i).number,
                previousItem.number <= result.get(i).number);
            previousItem = result.get(i);
        }
    }
    @Test
    public void getQueryComparatorRangeTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(200) + 300;
        List<TestData> list = new ArrayList<TestData>(count);
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        int specialCount = nextInt(100) + 100;
        final String specialString = nextString();
        List<TestData> specialList = new ArrayList<TestData>(specialCount);
        for(int i=0; i<specialCount; i++)
        {
            specialList.add(new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble()));
        }
        list.addAll(specialList);
        Collections.shuffle(list);
        box.addAll(list);
        int rangeStart = nextInt(25) + 25;
        int rangeCount = nextInt(25) + 25;
        List<TestData> result = box.get(new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.string.equals(specialString);
            }
        }, testDataNumberComparator, new Range(rangeStart, rangeCount));
        Collections.sort(specialList, testDataNumberComparator);
        List<TestData> subList = specialList.subList(rangeStart, rangeStart + rangeCount);
        assertEquals("Size of list of test datas with special string "+specialString+" must be equals " + subList.size() + "!", subList.size(), result.size());
        TestData previousItem = result.get(0);
        for(int i=0; i<subList.size(); i++)
        {
            assertEquals("Result item " + result.get(i)
                    + "must be equals item " + subList.get(i),
                subList.get(i), result.get(i));
            assertTrue("Item with number " + result.get(i).number
                    + " must stand after item with number " + result.get(i).number,
                previousItem.number <= result.get(i).number);
            previousItem = result.get(i);
        }
    }
    @Test
    public void getFirstTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>();
        final String specialString = nextString();
        for(int i=0; i<count; i++)
        {
            TestData tmp = fakeTestData();
            while(tmp.string.equals(specialString))
            {
                tmp = fakeTestData();
            }
            list.add(tmp);
        }
        TestData specialTestData = new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble());
        list.add(specialTestData);
        Collections.shuffle(list);
        box.addAll(list);
        TestData result = box.getFirst(new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.string.equals(specialString);
            }
        });
        assertEquals("Result test data " + result + " must be equals special test data " + specialTestData + "!", specialTestData, result);
    }
    @Test
    public void getFirstNotFoundTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>();
        final String specialString = nextString();
        for(int i=0; i<count; i++)
        {
            TestData tmp = fakeTestData();
            while(tmp.string.equals(specialString))
            {
                tmp = fakeTestData();
            }
            list.add(tmp);
        }
        box.addAll(list);
        TestData result = box.getFirst(new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.string.equals(specialString);
            }
        });
        assertNull("Result test data " + result + " must be null!", result);
    }
    @Test
    public void updateFirstTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>();
        final String specialString = nextString();
        for(int i=0; i<count; i++)
        {
            TestData tmp = fakeTestData();
            while(tmp.string.equals(specialString))
            {
                tmp = fakeTestData();
            }
            list.add(tmp);
        }
        list.add(new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble()));
        Collections.shuffle(list);
        box.addAll(list);
        TestData updateTestData = new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble());
        Query<TestData> query = new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.string.equals(specialString);
            }
        };
        box.updateFirst(query, updateTestData);
        TestData result = box.getFirst(query);
        assertEquals("Result test data " + result + " must be equals special test data " + updateTestData + "!", updateTestData, result);
    }
    @Test
    public void updateAllTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>();
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        int specialCount = nextInt(10) + 10;
        final String specialString = nextString();
        for(int i=0; i<specialCount; i++)
        {
            list.add(new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble()));
        }
        Collections.shuffle(list);
        box.addAll(list);
        TestData updateTestData = new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble());
        Query<TestData> query = new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.string.equals(specialString);
            }
        };
        box.updateAll(query, updateTestData);
        List<TestData> result = box.get(query);
        assertEquals("Size of list of test datas with special string "+specialString+" must be equals " + specialCount + "!", specialCount, result.size());
        for(TestData item: result)
        {
            assertEquals("Result test data " + item + " must be equals special test data " + updateTestData + "!", updateTestData, item);
        }
    }
    @Test
    public void removeFirstTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>();
        final String specialString = nextString();
        for(int i=0; i<count; i++)
        {
            TestData tmp = fakeTestData();
            while(tmp.string.equals(specialString))
            {
                tmp = fakeTestData();
            }
            list.add(tmp);
        }
        list.add(new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble()));
        Collections.shuffle(list);
        box.addAll(list);
        Query<TestData> query = new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.string.equals(specialString);
            }
        };
        assertNotNull("Result \"getFirst\" test data must be exist!", box.getFirst(query));
        assertEquals("Size of list of test datas with special string "+specialString+" must be equals 1!", 1, box.get(query).size());
        box.removeFirst(query);
        assertNull("Result \"getFirst\" after \"removeFirst\" must be null!", box.getFirst(query));
        assertTrue("Result \"get\" after \"removeFirst\" must be empty!", box.get(query).isEmpty());
    }
    @Test
    public void removeAllTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>();
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        int specialCount = nextInt(10) + 10;
        final String specialString = nextString();
        for(int i=0; i<specialCount; i++)
        {
            list.add(new TestData(nextInt(), specialString, nextBoolean(), nextLong(), nextDouble()));
        }
        Collections.shuffle(list);
        box.addAll(list);
        Query<TestData> query = new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.string.equals(specialString);
            }
        };
        assertEquals("Size of list of test datas with special string "+specialString+" must be equals "+specialCount+"!", specialCount, box.get(query).size());
        box.removeAll(query);
        assertTrue("Result \"get\" after \"removeAll\" must be empty!", box.get(query).isEmpty());
        assertEquals("Size of list of test datas after \"removeAll\" must be equals " + count + "!", count, box.getAll().size());
    }
    @Test
    public void clearTest()
    {
        assertTrue("List of test datas must be empty on init!", box.getAll().isEmpty());
        int count = nextInt(50) + 50;
        List<TestData> list = new ArrayList<TestData>(count);
        for(int i=0; i<count; i++)
        {
            list.add(fakeTestData());
        }
        box.addAll(list);
        assertEquals("Size of list of test datas must be equals " + count + "!", count, box.getAll().size());
        box.clear();
        assertTrue("Result \"getAll\" after \"clear\" must be empty!", box.getAll().isEmpty());
    }

    @After
    public void after()
    {
        box.clear();
    }

    private TestData fakeTestData()
    {
        return new TestData(nextInt(), nextString(), nextBoolean(), nextLong(), nextDouble());
    }
}
package stan.boxes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stan.boxes.utils.MainTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BoxTest
    extends MainTest
{
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

            public Map write(TestData data)
            {
                Map map = new HashMap();
                map.put(NUMBER, data.number);
                map.put(STRING, data.string);
                map.put(BOOL, data.bool);
                map.put(DATE, data.date);
                map.put(DBL, data.dbl);
                return map;
            }
            public TestData read(Map map)
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
        final int specialNumber = nextInt();
        List<TestData> specialList = new ArrayList<TestData>(specialCount);
        for(int i=0; i<specialCount; i++)
        {
            specialList.add(new TestData(specialNumber, nextString(), nextBoolean(), nextLong(), nextDouble()));
        }
        list.addAll(specialList);
        box.addAll(list);
        List<TestData> result = box.get(new Query<TestData>()
        {
            public boolean query(TestData item)
            {
                return item.number == specialNumber;
            }
        });
        assertEquals("Size of list of test datas with special number "+specialNumber+" must be equals " + specialCount + "!", specialCount, result.size());
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
        List<TestData> result = box.get(new Comparator<TestData>()
        {
            public int compare(TestData o1, TestData o2)
            {
                return o1.number < o2.number ? -1 : o1.number == o2.number ? 0 : 1;
            }
        });
        assertEquals("Size of list of test datas must be equals " + count + "!", count, result.size());
        TestData previousItem = result.get(0);
        for(TestData item: result)
        {
            assertTrue("Item with number " + item.number + " must stand after item with number " + previousItem.number, previousItem.number <= item.number);
            previousItem = item;
        }
    }
    @Test
    public void updateTest()
    {
        int number = nextInt();
        String string = nextString();
        boolean bool = nextBoolean();
        long date = nextLong();
        double dbl = nextDouble();
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
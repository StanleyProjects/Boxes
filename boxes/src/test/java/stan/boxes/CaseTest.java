package stan.boxes;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import stan.boxes.utils.MainTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CaseTest
    extends MainTest
{
    private final TestData defaultItem = new TestData(1, "str", false, 1234L, 0.345);
    private Case<TestData> testCase;

    @Before
    public void before()
    {
        testCase = new Case<TestData>(defaultItem,
        new ORM<TestData>()
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
        }, System.getProperty("user.home") + "/stan/boxes/testCase");
        testCase.clear();
    }

    @Test
    public void initTest()
    {
        TestData item = testCase.get();
        assertNotNull("Test data item must be exist!", item);
        assertEquals("Test data item must be equals default value!", item, defaultItem);
    }
    @Test
    public void saveTest()
    {
        TestData initItem = testCase.get();
        assertNotNull("Test data item must be exist after init!", initItem);
        assertEquals("Test data item must be equals default value after init!", initItem, defaultItem);
        TestData newItem = testDataMock();
        testCase.save(newItem);
        TestData savedItem = testCase.get();
        assertNotNull("Test data item must be exist after save!", savedItem);
        assertEquals("Test data item must be equals saved value after save!", savedItem, newItem);
    }
    @Test
    public void clearTest()
    {
        TestData initItem = testCase.get();
        assertNotNull("Test data item must be exist after init!", initItem);
        assertEquals("Test data item must be equals default value after init!", initItem, defaultItem);
        TestData newItem = testDataMock();
        testCase.save(newItem);
        TestData savedItem = testCase.get();
        assertNotNull("Test data item must be exist after save!", savedItem);
        assertEquals("Test data item must be equals saved value after save!", savedItem, newItem);
        testCase.clear();
        TestData clearedItem = testCase.get();
        assertNotNull("Test data item must be exist after clear!", clearedItem);
        assertEquals("Test data item must be equals default value after clear!", clearedItem, defaultItem);
    }

    private TestData testDataMock() {
        return new TestData(nextInt(), nextString(), nextBoolean(), nextLong(), nextDouble());
    }
}
package stan.boxes;

public class TestData
{
    final int number;
    final String string;
    final boolean bool;
    final long date;
    final double dbl;

    TestData(int number, String string, boolean bool, long date, double dbl)
    {
        this.number = number;
        if(string == null)
        {
            throw new IllegalArgumentException("Property \"string\" must be exist!");
        }
        this.string = string;
        this.bool = bool;
        this.date = date;
        this.dbl = dbl;
    }

    public int hashCode()
    {
        return number + string.hashCode() + (bool ? 1 : 2) + Long.valueOf(date).intValue() + Double.valueOf(dbl).intValue();
    }
    public boolean equals(Object o)
    {
        return o != null && (o == this || o instanceof TestData && equals((TestData)o));
    }
    private boolean equals(TestData that)
    {
        return number == that.number
            && string.equals(that.string)
            && bool == that.bool
            && date == that.date
            && dbl == that.dbl;
    }
    public String toString()
    {
        return "{"+number+","+string+","+bool+","+date+","+dbl+"}";
    }
}
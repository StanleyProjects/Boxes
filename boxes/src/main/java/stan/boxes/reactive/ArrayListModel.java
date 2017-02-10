package stan.boxes.reactive;

import java.util.List;

public class ArrayListModel<MODEL>
        implements ListModel<MODEL>
{
    private List<MODEL> data;

    public ArrayListModel(List<MODEL> d)
    {
        data = d;
    }

    @Override
    public MODEL get(int i)
    {
        return data.get(i);
    }

    @Override
    public int size()
    {
        if(data == null)
        {
            return 0;
        }
        return data.size();
    }
}
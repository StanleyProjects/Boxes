package stan.boxes.reactive;

public interface ListModel<MODEL>
{
    MODEL get(int i);
    int size();
}
package musician101.common.java.util;

import java.util.ArrayList;

public class ListUtil<E> extends ArrayList<E>
{
    public ListUtil(E... items)
    {
        super();
        for (E item : items)
            add(item);
    }
}

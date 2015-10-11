package musician101.common.java.util;

import java.util.ArrayList;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ListUtil<E> extends ArrayList<E>
{
    @SafeVarargs
    public ListUtil(E... items)
    {
        super();
        for (E item : items)
            add(item);
    }
}

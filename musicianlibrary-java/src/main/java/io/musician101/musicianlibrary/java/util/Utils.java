package io.musician101.musicianlibrary.java.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;


public class Utils {
    private Utils() {

    }

    public static boolean isInteger(String string)//NOSONAR
    {
        if (string == null)
            return false;

        int length = string.length();
        if (length == 0)
            return false;

        int i = 0;
        if (string.charAt(0) == '-') {
            if (length == 1)
                return false;

            i = 1;
        }

        for (; i < length; i++) {
            char c = string.charAt(i);
            if (c < '0' || c > '9')
                return false;
        }

        return true;
    }

    public static <T> Collector<T, List<T>, T> singletonCollector() {
        return Collector.of(ArrayList::new, List::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                list -> {
                    if (list.size() != 1)
                        throw new IllegalStateException();

                    return list.get(0);
                });
    }
}

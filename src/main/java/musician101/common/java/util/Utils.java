package musician101.common.java.util;

public class Utils
{
    public boolean isInteger(String string)
    {
        if (string == null)
            return false;

        int length = string.length();
        if (length == 0)
            return false;

        int i = 0;
        if (string.charAt(0) == '-')
        {
            if (length == 1)
                return false;

            i = 1;
        }

        for (; i < length; i++)
        {
            char c = string.charAt(i);
            if (c < '0' || c > '9')
                return false;
        }

        return true;
    }
}

package musician101.common.java.config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "SameParameterValue"})
public class JSONConfig extends JSONObject
{
    public JSONConfig()
    {
        super();
    }

    public static JSONConfig load(File file) throws IOException, ParseException
    {
        return (JSONConfig) new JSONParser().parse(new FileReader(file));
    }

    public static JSONConfig load(String string) throws ParseException
    {
        return (JSONConfig) new JSONParser().parse(string);
    }

    public boolean getBoolean(String key)
    {
        return Boolean.parseBoolean(getString(key));
    }

    public boolean getBoolean(String key, boolean defaultValue)
    {
        return containsKey(key) ? getBoolean(key) : defaultValue;
    }

    public double getDouble(String key)
    {
        return Double.parseDouble(getString(key));
    }

    public double getDouble(String key, double defaultValue)
    {
        return containsKey(key) ? getDouble(key) : defaultValue;
    }

    public int getInteger(String key)
    {
        return Integer.parseInt(getString(key));
    }

    public int getInteger(String key, int defaultValue)
    {
        return containsKey(key) ? getInteger(key) : defaultValue;
    }

    public JSONConfig getJSONConfig(String key)
    {
        return (JSONConfig) get(key);
    }

    public <E> List<E> getList(String key)
    {
        List<E> list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) get(key);
        jsonArray.forEach(o -> list.add((E) o));

        return list;
    }

    public <K, V> Map<K, V> getMap(String key)
    {
        return (Map<K, V>) get(key);
    }

    public <K, V> Map<K, V> getMap(String key, Map<K, V> defaultValue)
    {
        return containsKey(key) ? getMap(key) : defaultValue;
    }

    public short getShort(String key)
    {
        return Short.parseShort(getString(key));
    }

    public short getShort(String key, short defaultValue)
    {
        return containsKey(key) ? getShort(key) : defaultValue;
    }

    public String getString(String key)
    {
        return get(key).toString();
    }

    public String getString(String key, String defaultValue)
    {
        return containsKey(key) ? getString(key) : defaultValue;
    }
}

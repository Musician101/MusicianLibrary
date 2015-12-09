package musician101.common.java.config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
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

    public List getList(String key)
    {
        List list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) get(key);
        jsonArray.forEach(list::add);
        return list;
    }

    public List getList(String key, List defaultValue)
    {
        return containsKey(key) ? getList(key) : defaultValue;
    }

    public List<String> getStringList(String key)
    {
        List<String> list = new ArrayList<>();
        getList(key).forEach(object -> list.add(object.toString()));
        return list;
    }

    public List<String> getStringList(String key, List<String> defaultValue)
    {
        return containsKey(key) ? getStringList(key) : defaultValue;
    }

    public List<Double> getDoubleList(String key)
    {
        List<Double> list = new ArrayList<>();
        getStringList(key).forEach(string -> list.add(Double.parseDouble(string)));
        return list;
    }

    public List<Double> getDoubleList(String key, List<Double> defaultValue)
    {
        return containsKey(key) ? getDoubleList(key) : defaultValue;
    }

    public List<Integer> getIntegerList(String key)
    {
        List<Integer> list = new ArrayList<>();
        getStringList(key).forEach(string -> list.add(Integer.parseInt(string)));
        return list;
    }

    public List<Integer> getIntegerList(String key, List<Integer> defaultValue)
    {
        return containsKey(key) ? getIntegerList(key) : defaultValue;
    }

    public List<Map<String, Object>> getMapList(String key)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        getList(key).forEach(object -> {
            Map objectMap = (Map) object;
            Map<String, Object> map = new HashMap<>();
            for (Object obj : objectMap.entrySet())
                map.put(obj.toString(), objectMap.get(obj));

            list.add(map);
        });

        return list;
    }

    public Map<String, Object> getMap(String key)
    {
        Map objectMap = (Map) get(key);
        Map<String, Object> map = new HashMap<>();
        for (Object object : objectMap.entrySet())
            map.put(object.toString(), objectMap.get(object));

        return map;
    }

    public Map<String, Object> getMap(String key, Map<String, Object> defaultValue)
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

    public void set(String key, Object value)
    {
        put(key, value);
    }
}

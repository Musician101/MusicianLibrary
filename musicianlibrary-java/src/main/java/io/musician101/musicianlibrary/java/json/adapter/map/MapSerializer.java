package io.musician101.musicianlibrary.java.json.adapter.map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapSerializer<V> implements JsonDeserializer<Map<String, V>>, JsonSerializer<Map<String, V>> {

    @Override
    public Map<String, V> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<String, V> map = new HashMap<>();
        json.getAsJsonObject().entrySet().forEach(entry -> map.put(entry.getKey(), context.<V>deserialize(entry.getValue(), new TypeToken<V>(){}.getType())));
        return map;
    }

    @Override
    public JsonElement serialize(Map<String, V> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        src.forEach((key, value) -> jsonObject.add(key, context.serialize(value)));
        return jsonObject;
    }
}

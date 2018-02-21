package io.musician101.musicianlibrary.java.json.adapter.list;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import io.musician101.musicianlibrary.java.util.Utils;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ShortListSerializer implements ListSerializer<Short> {

    @Override
    public List<Short> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return StreamSupport.stream(json.getAsJsonArray().spliterator(), false).map(JsonElement::getAsShort).collect(Collectors.toList());
    }

    @Override
    public JsonElement serialize(List<Short> src, Type typeOfSrc, JsonSerializationContext context) {
        return src.stream().map(JsonPrimitive::new).collect(Utils.jsonArrayCollector());
    }
}
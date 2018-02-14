package io.musician101.musicianlibrary.java.json.adapter.list;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import java.util.List;

public interface ListSerializer<V> extends JsonDeserializer<List<V>>, JsonSerializer<List<V>> {

}

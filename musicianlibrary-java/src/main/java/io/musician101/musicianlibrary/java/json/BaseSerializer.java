package io.musician101.musicianlibrary.java.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import javax.annotation.Nonnull;

public abstract class BaseSerializer<T> implements JsonDeserializer<T>, JsonSerializer<T> {

    @Nonnull
    protected final <V> V deserialize(@Nonnull JsonObject parent, @Nonnull JsonDeserializationContext context, @Nonnull JsonKey<V> key) {
        if (parent.has(key.getKey())) {
            return context.deserialize(parent.get(key.getKey()), key.getDefaultValue().getClass());
        }

        return key.getDefaultValue();
    }

    protected final <V> void serialize(@Nonnull JsonObject parent, @Nonnull JsonSerializationContext context, @Nonnull JsonKey<V> key, @Nonnull V value) {
        parent.add(key.getKey(), context.serialize(value));
    }
}

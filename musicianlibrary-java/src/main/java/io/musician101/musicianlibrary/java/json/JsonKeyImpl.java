package io.musician101.musicianlibrary.java.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static io.musician101.musicianlibrary.java.json.JsonKeyProcessor.GSON;

@SuppressWarnings("unchecked")
public class JsonKeyImpl<J extends JsonElement, V> {

    @Nullable
    private final Object serializer;
    @Nonnull
    private final TypeToken<V> tokenType;
    @Nonnull
    private final String key;

    private JsonKeyImpl(@Nonnull String key, @Nonnull TypeToken<V> tokenType) {
        this(key, tokenType, null);
    }

    JsonKeyImpl(@Nonnull String key, @Nonnull TypeToken<V> tokenType, @Nullable Object serializer) {
        this.key = key;
        this.tokenType = tokenType;
        this.serializer = serializer;
    }

    @Nonnull
    public Optional<Object> getTypeAdapter() {
        return Optional.ofNullable(serializer);
    }

    @Nonnull
    public TypeToken<V> getTokenType() {
        return tokenType;
    }

    @Nonnull
    public String getKey() {
        return key;
    }

    @Nonnull
    public J serialize(@Nonnull V value, @Nullable JsonSerializationContext context) {
        return (J) (context == null ? GSON.toJsonTree(value) : context.serialize(value));
    }

    public void serialize(@Nonnull V value, @Nonnull JsonObject jsonObject, @Nullable JsonSerializationContext context) {
        J json = serialize(value, context);
        jsonObject.add(key, json);
    }

    @Nonnull
    public Optional<V> deserializeFromParent(@Nonnull JsonObject jsonObject, @Nullable JsonDeserializationContext context) {
        return deserialize((J) jsonObject.get(key), context);
    }

    @Nonnull
    public Optional<V> deserialize(@Nonnull J json, @Nullable JsonDeserializationContext context) {
        try {
            return Optional.of(context == null ? GSON.fromJson(json, tokenType.getType()) : context.deserialize(json, tokenType.getType()));
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    public static JsonKeyImpl<JsonPrimitive, BigDecimal> bigDecimalKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(BigDecimal.class));
    }

    public static JsonKeyImpl<JsonPrimitive, BigInteger> bigIntegerKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(BigInteger.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Boolean> booleanKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Boolean.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Byte> byteKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Byte.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Character> characterKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Character.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Double> doubleKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Double.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Float> floatKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Float.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Integer> integerKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Integer.class));
    }

    public static <J extends JsonElement, V> JsonKeyImpl<J, V> key(@Nonnull String key, @Nonnull TypeToken<V> typeToken, @Nullable Object typeAdapter) {
        return new JsonKeyImpl<>(key, typeToken, typeAdapter);
    }

    public static <V> JsonKeyImpl<JsonArray, List<V>> listKey(@Nonnull String key, @Nullable Object typeAdapter) {
        return new JsonKeyImpl<>(key, new TypeToken<List<V>>(){}, typeAdapter);
    }

    public static JsonKeyImpl<JsonPrimitive, Long> longKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Long.class));
    }

    public static <K, V> JsonKeyImpl<JsonObject, Map<K, V>> mapKey(@Nonnull String key, @Nullable Object typeAdapter) {
        return new JsonKeyImpl<>(key, new TypeToken<Map<K, V>>(){}, typeAdapter);
    }

    public static JsonKeyImpl<JsonPrimitive, Number> numberKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Number.class));
    }

    public static <V> JsonKeyImpl<JsonObject, V> objectKey(@Nonnull String key, @Nullable Object typeAdapter) {
        return new JsonKeyImpl<>(key, new TypeToken<V>(){}, typeAdapter);
    }

    public static JsonKeyImpl<JsonPrimitive, Short> shortKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Short.class));
    }

    public static JsonKeyImpl<JsonPrimitive, String> stringKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(String.class));
    }
}

package io.musician101.musicianlibrary.java.json;

import javax.annotation.Nonnull;

public class JsonKey<V> {

    @Nonnull
    private final V defaultValue;
    @Nonnull
    private final String key;

    public JsonKey(@Nonnull String key, @Nonnull V defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Nonnull
    public V getDefaultValue() {
        return defaultValue;
    }

    @Nonnull
    public String getKey() {
        return key;
    }
}

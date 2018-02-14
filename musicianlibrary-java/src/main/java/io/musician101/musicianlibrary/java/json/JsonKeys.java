package io.musician101.musicianlibrary.java.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JsonKeys {

    @Nullable
    Class<?> typeAdapter();

    @Nonnull
    String[] keys();
}

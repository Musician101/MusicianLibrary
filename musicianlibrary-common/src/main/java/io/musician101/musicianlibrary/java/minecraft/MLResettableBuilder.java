package io.musician101.musicianlibrary.java.minecraft;

import javax.annotation.Nonnull;

public interface MLResettableBuilder<T, B extends MLResettableBuilder<T, B>> {

    @Nonnull
    T build() throws IllegalStateException;

    @Nonnull
    B reset();
}

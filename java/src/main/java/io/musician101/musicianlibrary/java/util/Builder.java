package io.musician101.musicianlibrary.java.util;

import javax.annotation.Nonnull;

public interface Builder<B extends Builder<?, ?>, T> {

    @Nonnull
    T build();

    @Nonnull
    B reset();
}

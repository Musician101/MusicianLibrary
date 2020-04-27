package io.musician101.musicianlibrary.java.minecraft.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;

public abstract class TextInput<P> {

    protected static final List<UUID> PLAYERS = new ArrayList<>();
    @Nonnull
    protected final P player;

    protected TextInput(@Nonnull P player) {
        this.player = player;
    }

    protected abstract void cancel();

    protected abstract void process(P player, String message);
}

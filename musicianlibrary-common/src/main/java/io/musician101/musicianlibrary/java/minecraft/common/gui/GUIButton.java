package io.musician101.musicianlibrary.java.minecraft.common.gui;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

public final class GUIButton<C, P, S> {

    @Nonnull
    private final Map<C, Consumer<P>> actions;
    @Nonnull
    private final S itemStack;
    private final int slot;

    public GUIButton(int slot, @Nonnull S itemStack) {
        this(slot, itemStack, ImmutableMap.of());
    }

    public GUIButton(int slot, @Nonnull S itemStack, @Nonnull Map<C, Consumer<P>> actions) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.actions = actions;
    }

    @Nonnull
    public Optional<Consumer<P>> getAction(C clickType) {
        return Optional.ofNullable(actions.get(clickType));
    }

    @Nonnull
    public final S getItemStack() {
        return itemStack;
    }

    public final int getSlot() {
        return slot;
    }
}

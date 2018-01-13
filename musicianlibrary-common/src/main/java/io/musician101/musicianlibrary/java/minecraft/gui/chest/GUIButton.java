package io.musician101.musicianlibrary.java.minecraft.gui.chest;

import java.util.Optional;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GUIButton<C, G, P, S> {

    @Nullable
    private final BiConsumer<G, P> action;
    @Nonnull
    private final C clickType;
    @Nonnull
    private final S itemStack;
    private final int slot;

    public GUIButton(int slot, @Nonnull C clickType, @Nonnull S itemStack, @Nullable BiConsumer<G, P> action) {
        this.slot = slot;
        this.clickType = clickType;
        this.itemStack = itemStack;
        this.action = action;
    }

    @Nonnull
    public Optional<BiConsumer<G, P>> getAction() {
        return Optional.ofNullable(action);
    }

    @Nonnull
    public final C getClickType() {
        return clickType;
    }

    @Nonnull
    public final S getItemStack() {
        return itemStack;
    }

    public final int getSlot() {
        return slot;
    }
}

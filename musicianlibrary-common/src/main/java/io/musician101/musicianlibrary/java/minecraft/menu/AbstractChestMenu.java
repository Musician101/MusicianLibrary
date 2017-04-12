package io.musician101.musicianlibrary.java.minecraft.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractChestMenu<F, G extends AbstractChestMenu<F, G, I, N, P, S, T>, I, N, P, S, T> {

    protected final Map<N, Consumer<P>> buttons = new HashMap<>();
    @Nonnull
    protected final I inventory;
    @Nonnull
    protected final P player;
    @Nullable
    protected final G prevMenu;

    public AbstractChestMenu(@Nonnull I inventory, @Nonnull P player, @Nullable G prevMenu) {
        this.inventory = inventory;
        this.player = player;
        this.prevMenu = prevMenu;
    }

    protected abstract void build();

    protected abstract void close();

    @SuppressWarnings("unchecked")
    @Nonnull
    protected abstract S createItem(@Nonnull T itemType, @Nonnull F name, @Nonnull F... description);

    public abstract void open();

    protected abstract void set(int slot, @Nonnull S itemStack, @Nonnull Consumer<P> consumer);

    protected abstract void set(int slot, @Nonnull S itemStack);

    protected abstract void setBackButton(int slot, @Nonnull T itemType);
}

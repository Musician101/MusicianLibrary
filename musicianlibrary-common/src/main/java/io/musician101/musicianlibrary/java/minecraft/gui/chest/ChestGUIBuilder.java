package io.musician101.musicianlibrary.java.minecraft.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.util.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public abstract class ChestGUIBuilder<B extends ChestGUIBuilder<B, C, G, I, J, P, S, T>, C, G extends AbstractChestGUI<C, G, I, J, P, S>, I, J, P, S, T> implements Builder<B, G> {

    protected final List<GUIButton<C, G, P, S>> buttons = new ArrayList<>();
    protected boolean manualOpen = false;
    protected T name;
    protected int page = 1;
    protected P player;
    protected J plugin;
    protected G prevGUI;
    protected int size = 27;

    @Nonnull
    @Override
    public B reset() {
        manualOpen = false;
        prevGUI = null;
        page = -1;
        size = -1;
        plugin = null;
        buttons.clear();
        player = null;
        name = null;
        return (B) this;
    }

    @Nonnull
    public abstract B setBackButton(int slot, @Nonnull C clickType);

    @Nonnull
    public final B setButton(@Nonnull GUIButton<C, G, P, S> button) {
        buttons.removeIf(b -> button.getSlot() == b.getSlot() && button.getClickType() == b.getClickType());
        buttons.add(button);
        return (B) this;
    }

    @Nonnull
    public final <O> B setContents(@Nonnull C clickType, @Nonnull List<O> contents, @Nonnull Function<O, S> itemStackMapper, @Nullable BiFunction<P, O, BiConsumer<G, P>> actionMapper) {
        int size = this.size - 9;
        for (int x = 0; x < size; x++) {
            try {
                O content = contents.get(x + (page - 1) * size);
                S itemStack = itemStackMapper.apply(content);
                setButton(new GUIButton<>(x, clickType, itemStack, actionMapper == null ? null : actionMapper.apply(player, content)));
            }
            catch (IndexOutOfBoundsException e) {
                //Used to skip populating slots if the list is too small to fill the page.
            }
        }

        return (B) this;
    }

    @Nonnull
    public abstract B setJumpToPage(int slot, int maxPage, @Nonnull BiConsumer<P, Integer> action);

    public void setManualOpen(boolean manualOpen) {
        this.manualOpen = manualOpen;
    }

    @Nonnull
    public final B setName(@Nonnull T name) {
        this.name = name;
        return (B) this;
    }

    @Nonnull
    public final B setPage(int page) {
        this.page = page;
        return (B) this;
    }

    @Nonnull
    public abstract B setPageNavigation(int slot, @Nonnull T name, @Nonnull BiConsumer<G, P> action);

    @Nonnull
    public final B setPlayer(@Nonnull P player) {
        this.player = player;
        return (B) this;
    }

    @Nonnull
    public final B setPlugin(@Nonnull J plugin) {
        this.plugin = plugin;
        return (B) this;
    }

    @Nonnull
    public final B setPreviousGUI(@Nullable G prevGUI) {
        this.prevGUI = prevGUI;
        return (B) this;
    }

    @Nonnull
    public final B setSize(int size) {
        this.size = size;
        return (B) this;
    }
}

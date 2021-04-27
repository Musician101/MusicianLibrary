package io.musician101.musicianlibrary.java.minecraft.common.gui.book;

import java.util.List;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;

public abstract class BookGUI<C, P, T> {

    @Nonnull
    protected final BiConsumer<P, List<T>> action;
    protected final int bookSlot;
    @Nonnull
    protected final P player;
    @Nonnull
    protected final C plugin;

    protected BookGUI(@Nonnull C plugin, @Nonnull P player, int bookSlot, @Nonnull BiConsumer<P, List<T>> action) {
        this.action = action;
        this.bookSlot = bookSlot;
        this.player = player;
        this.plugin = plugin;
    }

    protected abstract void remove();

    public static class PlayerHandNotEmptyException extends Exception {

        public PlayerHandNotEmptyException() {
            super("Player hand not empty.");
        }
    }

    public static class BookGUIAlreadyOpenException extends Exception {

        public BookGUIAlreadyOpenException(String playerName) {
            super(playerName + " already has a book GUI open.");
        }
    }
}

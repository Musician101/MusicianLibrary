package io.musician101.musicianlibrary.java.minecraft.menu;

import java.util.List;
import java.util.function.BiConsumer;

public abstract class AbstractBookMenu<I, J, P, T, W, X, Y, Z> {
    protected final BiConsumer<P, List<String>> biConsumer;
    protected final P player;
    protected final I book;
    protected I heldItem;
    protected T taskId;

    protected AbstractBookMenu(P player, I book, BiConsumer<P, List<String>> biConsumer) {
        this.player = player;
        this.book = book;
        this.biConsumer = biConsumer;
    }

    public abstract void bookInteract(W event);

    public abstract void clickBook(X event);

    protected abstract void close();

    public abstract void closeBook(Z event);

    public abstract void dropBook(Y event);

    protected abstract boolean isSameBook(I book);

    protected abstract void start(J plugin);
}

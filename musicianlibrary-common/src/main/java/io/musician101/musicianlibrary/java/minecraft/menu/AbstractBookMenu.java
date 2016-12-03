package io.musician101.musicianlibrary.java.minecraft.menu;

import java.util.List;
import java.util.function.BiConsumer;

public abstract class AbstractBookMenu<I, J, P, T, W, X, Y, Z>
{
    protected final BiConsumer<P, List<String>> biConsumer;
    protected I heldItem;
    protected I book;
    protected T taskId;
    protected final P player;

    protected AbstractBookMenu(P player, I book, BiConsumer<P, List<String>> biConsumer)
    {
        this.player = player;
        this.book = book;
        this.biConsumer = biConsumer;
    }

    protected abstract void start(J plugin);

    protected abstract void close();

    protected abstract boolean isSameBook(I book);

    public abstract void bookInteract(W event);

    public abstract void clickBook(X event);

    public abstract void dropBook(Y event);

    public abstract void closeBook(Z event);
}

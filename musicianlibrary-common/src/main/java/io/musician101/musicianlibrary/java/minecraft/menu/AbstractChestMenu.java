package io.musician101.musicianlibrary.java.minecraft.menu;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChestMenu<C, I, J, P, S, X, Y, Z>
{
    protected final Map<Integer, C> buttons = new HashMap<>();
    protected final I inventory;
    protected final P player;

    protected AbstractChestMenu(I inventory, P player)
    {
        this.inventory = inventory;
        this.player = player;
    }

    protected abstract void open(J plugin);

    protected abstract void close();

    protected void onOpen() {}

    protected void onClose() {}

    protected abstract void build();

    protected abstract void set(int slot, S itemStack);

    protected abstract void set(int slot, S itemStack, C consumer);

    public abstract void onInventoryItemDrag(X event);

    public abstract void onInventoryClick(Y event);

    public abstract void onInventoryClose(Z event);
}

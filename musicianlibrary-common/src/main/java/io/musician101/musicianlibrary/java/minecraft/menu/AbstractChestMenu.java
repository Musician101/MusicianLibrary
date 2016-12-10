package io.musician101.musicianlibrary.java.minecraft.menu;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChestMenu<C, I, J, P, S, X, Y, Z> {

    protected final Map<Integer, C> buttons = new HashMap<>();
    protected final I inventory;
    protected final P player;

    protected AbstractChestMenu(I inventory, P player) {
        this.inventory = inventory;
        this.player = player;
    }

    protected abstract void build();

    protected abstract void close();

    protected void onClose() {
    }

    public abstract void onInventoryClick(Y event);

    public abstract void onInventoryClose(Z event);

    public abstract void onInventoryItemDrag(X event);

    protected void onOpen() {
    }

    protected abstract void open(J plugin);

    protected abstract void set(int slot, S itemStack);

    protected abstract void set(int slot, S itemStack, C consumer);
}

package io.musician101.musicianlibrary.java.minecraft.gui.chest;

import java.util.List;

public abstract class ChestGUI<C, G extends ChestGUI<C, G, I, J, P, S>, I, J, P, S> {

    protected final List<GUIButton<C, G, I, J, P, S>> buttons;
    protected final I inventory;
    protected final int page;
    protected final P player;
    protected final J plugin;
    protected final G prevGUI;

    protected ChestGUI(I inventory, P player, int page, List<GUIButton<C, G, I, J, P, S>> buttons, G prevGUI, J plugin, boolean manualOpen) {
        this.inventory = inventory;
        this.player = player;
        this.page = page;
        this.buttons = buttons;
        this.prevGUI = prevGUI;
        this.plugin = plugin;
        if (!manualOpen) {
            open();
        }
    }

    public abstract void close();

    public abstract void open();
}

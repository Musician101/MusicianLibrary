package io.musician101.musicianlibrary.java.minecraft.gui.chest;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ChestGUI<C, G extends ChestGUI<C, G, I, J, P, S>, I, J, P, S> {

    @Nonnull
    protected final List<GUIButton<C, G, I, J, P, S>> buttons;
    @Nonnull
    protected final I inventory;
    protected final int page;
    @Nonnull
    protected final P player;
    @Nonnull
    protected final J plugin;
    @Nullable
    protected final G prevGUI;

    protected ChestGUI(@Nonnull I inventory, @Nonnull P player, int page, @Nonnull List<GUIButton<C, G, I, J, P, S>> buttons, @Nullable G prevGUI, @Nonnull J plugin, boolean manualOpen) {
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

    @Nullable
    public G getPreviousGUI() {
        return prevGUI;
    }

    public abstract void open();
}

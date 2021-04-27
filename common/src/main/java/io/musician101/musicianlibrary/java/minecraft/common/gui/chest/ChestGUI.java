package io.musician101.musicianlibrary.java.minecraft.common.gui.chest;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

public abstract class ChestGUI<ClickType, Inventory, PluginContainer, Player, ItemStack, Name, InventoryView, InventoryClose> {

    @Nonnull
    protected final List<GUIButton<ClickType, Player, ItemStack>> buttons = new ArrayList<>();
    @Nonnull
    protected final Inventory inventory;
    @Nonnull
    protected final Name name;
    @Nonnull
    protected final Player player;
    @Nonnull
    protected final PluginContainer plugin;
    @Nonnull
    protected Consumer<InventoryClose> extraCloseHandler = event -> {
    };

    protected ChestGUI(@Nonnull Inventory inventory, @Nonnull Name name, @Nonnull Player player, @Nonnull PluginContainer plugin, boolean manualOpen) {
        this.inventory = inventory;
        this.name = name;
        this.player = player;
        this.plugin = plugin;
        if (!manualOpen) {
            open();
        }
    }

    protected abstract void addItem(int slot, @Nonnull ItemStack itemStack);

    protected abstract boolean isCorrectInventory(@Nonnull InventoryView inventoryView);

    public abstract void open();

    public final void removeButton(int slot) {
        buttons.removeIf(g -> g.getSlot() == slot);
        removeItem(slot);
    }

    protected abstract void removeItem(int slot);

    public final void setButton(int slot, @Nonnull ItemStack itemStack) {
        setButton(slot, itemStack, ImmutableMap.of());
    }

    public final void setButton(int slot, @Nonnull ItemStack itemStack, Map<ClickType, Consumer<Player>> actions) {
        buttons.removeIf(g -> g.getSlot() == slot);
        buttons.add(new GUIButton<>(slot, itemStack, actions));
        addItem(slot, itemStack);
    }
}

package io.musician101.musicianlibrary.java.minecraft.spigot.gui;

import io.musician101.musicianlibrary.java.minecraft.common.gui.ChestGUI;
import io.musician101.musicianlibrary.java.minecraft.common.gui.GUIButton;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SpigotChestGUI<J extends JavaPlugin> extends ChestGUI<ClickType, Inventory, J, Player, ItemStack, String, InventoryView, InventoryCloseEvent> implements Listener {

    @Nonnull
    protected Consumer<InventoryDragEvent> extraDragHandler = event -> {
    };
    @Nonnull
    protected Consumer<InventoryClickEvent> extraClickHandler = event -> {
    };

    protected SpigotChestGUI(@Nonnull Player player, @Nonnull String name, int size, @Nonnull J plugin, boolean manualOpen) {
        super(parseInventory(player, name, size), name, player, plugin, manualOpen);
    }

    private static Inventory parseInventory(Player player, String name, int size) {
        return name == null ? Bukkit.createInventory(player, size) : Bukkit.createInventory(player, size, name);
    }

    @Override
    protected void addItem(int slot, @Nonnull ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    @Override
    protected boolean isCorrectInventory(@Nonnull InventoryView inventoryView) {
        return inventoryView.getTitle().equals(name) && inventoryView.getPlayer().getUniqueId().equals(player.getUniqueId());
    }

    @EventHandler
    public final void onInventoryClick(InventoryClickEvent event) {
        InventoryView inventoryView = event.getView();
        if (isCorrectInventory(inventoryView)) {
            if (buttons.stream().map(GUIButton::getSlot).anyMatch(i -> i == event.getRawSlot())) {
                event.setCancelled(true);
                buttons.stream().filter(button -> button.getSlot() == event.getRawSlot()).findFirst().flatMap(button -> button.getAction(event.getClick())).ifPresent(consumer -> consumer.accept(player));
            }

            extraClickHandler.accept(event);
        }
    }

    @EventHandler
    public final void onInventoryClose(InventoryCloseEvent event) {
        InventoryView inventory = event.getView();
        if (inventory.getTitle().equals(name) && player.getUniqueId().equals(inventory.getPlayer().getUniqueId())) {
            extraCloseHandler.accept(event);
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public final void onInventoryItemDrag(InventoryDragEvent event) {
        if (isCorrectInventory(event.getView())) {
            if (buttons.stream().map(GUIButton::getSlot).anyMatch(i -> event.getRawSlots().contains(i))) {
                event.setCancelled(true);
            }

            extraDragHandler.accept(event);
        }
    }

    @Override
    public final void open() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            inventory.clear();
            buttons.forEach(button -> inventory.setItem(button.getSlot(), button.getItemStack()));
            player.openInventory(inventory);
            Bukkit.getPluginManager().registerEvents(this, plugin);
        });
    }

    @Override
    protected void removeItem(int slot) {
        inventory.setItem(slot, null);
    }
}

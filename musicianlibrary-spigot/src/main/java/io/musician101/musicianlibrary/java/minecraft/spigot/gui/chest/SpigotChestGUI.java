package io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.gui.chest.ChestGUI;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotChestGUI<J extends JavaPlugin> extends ChestGUI<ClickType, SpigotChestGUI<J>, Inventory, J, Player, ItemStack> implements Listener {

    private static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static Field activeContainer;
    private static Field defaultContainer;
    private static Method getHandle;
    private static Method handleInventoryCloseEvent;

    static {
        try {
            final Class<?> entityHuman = Class.forName("net.minecraft.server." + SERVER_VERSION + ".EntityHuman");
            handleInventoryCloseEvent = Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + ".event.CraftEventFactory").getDeclaredMethod("handleInventoryCloseEvent", entityHuman);
            getHandle = Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + ".entity.CraftPlayer").getDeclaredMethod("getHandle");
            defaultContainer = entityHuman.getDeclaredField("defaultContainer");
            activeContainer = entityHuman.getDeclaredField("activeContainer");
        }
        catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    SpigotChestGUI(Player player, String name, int size, int page, SpigotChestGUI<J> prevMenu, @Nonnull J plugin, boolean manualOpen) {
        super(parseInventory(player, name, size), player, page, prevMenu, plugin, manualOpen);
    }

    public static <J extends JavaPlugin> SpigotChestGUIBuilder<J> builder() {
        return new SpigotChestGUIBuilder<>();
    }

    private static Inventory parseInventory(Player player, String name, int size) {
        return name == null ? Bukkit.createInventory(player, size) : Bukkit.createInventory(player, size, name);
    }

    @Override
    public final void close() {
        if (prevGUI == null) {
            player.closeInventory();
        }
        else {
            prevGUI.open();
        }
    }

    @EventHandler
    public final void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(inventory.getName()) && event.getInventory().getHolder().equals(player)) {
            event.setCancelled(true);
            buttons.stream().filter(button -> button.getSlot() == event.getRawSlot()).findFirst().flatMap(button -> button.getAction(event.getClick())).ifPresent(consumer -> consumer.accept(this, player));
        }
    }

    @EventHandler
    public final void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().equals(inventory.getName()) && event.getInventory().getHolder().equals(player)) {
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public final void onInventoryItemDrag(InventoryDragEvent event) {
        event.setCancelled(event.getInventory().getName().equals(inventory.getName()) && event.getInventory().getHolder().equals(player));
    }

    @Override
    public final void open() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            try {
                inventory.clear();
                buttons.forEach(button -> inventory.setItem(button.getSlot(), button.getItemStack()));

                final Object entityHuman = getHandle.invoke(player);
                handleInventoryCloseEvent.invoke(null, entityHuman);
                activeContainer.set(entityHuman, defaultContainer.get(entityHuman));

                player.openInventory(inventory);
                Bukkit.getPluginManager().registerEvents(this, plugin);
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}

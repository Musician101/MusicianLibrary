package io.musician101.musicianlibrary.java.minecraft.spigot.menu;

import io.musician101.musicianlibrary.java.minecraft.menu.AbstractChestMenu;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
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

public abstract class AbstractSpigotChestMenu<J extends JavaPlugin> extends AbstractChestMenu<BiConsumer<Player, ClickType>, Inventory, J, Player, ItemStack, InventoryDragEvent, InventoryClickEvent, InventoryCloseEvent> implements Listener {

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
        catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }

    protected AbstractSpigotChestMenu(Player player, int size, String name, J plugin) {
        this(player, size, name, plugin, false);
    }

    protected AbstractSpigotChestMenu(Player player, int size, String name, J plugin, boolean manualOpen) {
        super(Bukkit.createInventory(player, size, name), player);
        if (!manualOpen)
            open(plugin);
    }

    @Override
    protected void close() {
        onClose();
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getName().equals(inventory.getName()) && e.getInventory().getHolder().equals(player)) {
            e.setCancelled(true);
            if (buttons.containsKey(e.getRawSlot()))
                buttons.get(e.getRawSlot()).accept((Player) e.getWhoClicked(), e.getClick());
        }
    }

    @EventHandler
    @Override
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getName().equals(inventory.getName()) && e.getInventory().getHolder().equals(player))
            close();
    }

    @EventHandler
    @Override
    public void onInventoryItemDrag(InventoryDragEvent e) {
        e.setCancelled(e.getInventory().getName().equals(inventory.getName()) && e.getInventory().getHolder().equals(player));
    }

    @Override
    protected void open(J plugin) {
        try {
            inventory.clear();
            build();

            final Object entityHuman = getHandle.invoke(player);
            handleInventoryCloseEvent.invoke(null, entityHuman);
            activeContainer.set(entityHuman, defaultContainer.get(entityHuman));

            player.openInventory(inventory);
            Bukkit.getPluginManager().registerEvents(this, plugin);
            onOpen();
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void set(int slot, ItemStack stack, BiConsumer<Player, ClickType> consumer) {
        set(slot, stack);
        buttons.put(slot, consumer);
    }

    @Override
    protected void set(int slot, ItemStack stack) {
        inventory.setItem(slot, stack);
    }
}

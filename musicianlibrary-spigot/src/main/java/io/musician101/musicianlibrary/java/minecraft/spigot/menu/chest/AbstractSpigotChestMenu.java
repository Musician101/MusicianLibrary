package io.musician101.musicianlibrary.java.minecraft.spigot.menu.chest;

import io.musician101.musicianlibrary.java.minecraft.menu.AbstractChestMenu;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractSpigotChestMenu<J extends JavaPlugin> extends AbstractChestMenu<String, AbstractSpigotChestMenu<J>, Inventory, Integer, Player, ItemStack, Material> implements Listener {

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

    @Nonnull
    protected final J plugin;

    public AbstractSpigotChestMenu(@Nonnull Player player, int size, @Nonnull String name, @Nullable AbstractSpigotChestMenu<J> prevMenu, @Nonnull J plugin) {
        this(player, size, name, prevMenu, plugin, false);
    }

    public AbstractSpigotChestMenu(@Nonnull Player player, int size, @Nonnull String name, @Nullable AbstractSpigotChestMenu<J> prevMenu, @Nonnull J plugin, boolean manualOpen) {
        super(Bukkit.createInventory(player, size, name), player, prevMenu);
        this.plugin = plugin;
        if (!manualOpen) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::open);
        }
    }

    @Override
    protected void close() {
        HandlerList.unregisterAll(this);
        if (prevMenu != null)
            prevMenu.open();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getName().equals(inventory.getName()) && e.getInventory().getHolder().equals(player)) {
            e.setCancelled(true);
            if (buttons.containsKey(e.getRawSlot()))
                buttons.get(e.getRawSlot()).accept((Player) e.getWhoClicked());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getName().equals(inventory.getName()) && e.getInventory().getHolder().equals(player))
            close();
    }

    @EventHandler
    public void onInventoryItemDrag(InventoryDragEvent e) {
        e.setCancelled(e.getInventory().getName().equals(inventory.getName()) && e.getInventory().getHolder().equals(player));
    }

    @Override
    public void open() {
        try {
            inventory.clear();
            build();

            final Object entityHuman = getHandle.invoke(player);
            handleInventoryCloseEvent.invoke(null, entityHuman);
            activeContainer.set(entityHuman, defaultContainer.get(entityHuman));

            player.openInventory(inventory);
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack stack, @Nonnull Consumer<Player> consumer) {
        set(slot, stack);
        buttons.put(slot, consumer);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack stack) {
        inventory.setItem(slot, stack);
    }

    @Nonnull
    @Override
    protected ItemStack createItem(@Nonnull Material itemType, @Nonnull String name, @Nonnull String... description) {
        ItemStack itemStack = new ItemStack(itemType);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(description));
        return itemStack;
    }

    @Override
    protected void setBackButton(int slot, @Nonnull Material itemType) {
        ItemStack itemStack = createItem(itemType, ChatColor.RED + "Back", "Closes this menu and attempts", "to go back to the previous menu.");
        set(slot, itemStack, HumanEntity::closeInventory);
    }
}

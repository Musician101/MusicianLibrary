package io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.menu.AbstractChestMenu;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public abstract class AbstractSpigotChestGUI<J extends JavaPlugin> extends AbstractChestMenu<ClickType, PotionType, String, AbstractSpigotChestGUI<J>, Inventory, J, Player, ItemStack, Material> implements Listener {

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

    public AbstractSpigotChestGUI(@Nonnull Player player, @Nonnull String name, int size, @Nullable AbstractSpigotChestGUI<J> prevMenu, @Nonnull J plugin) {
        this(player, name, size, prevMenu, plugin, false);
    }

    public AbstractSpigotChestGUI(@Nonnull Player player, @Nonnull String name, int size, @Nullable AbstractSpigotChestGUI<J> prevMenu, @Nonnull J plugin, boolean manualOpen) {
        super(Bukkit.createInventory(player, size, name), player, prevMenu, plugin, manualOpen);
    }

    @Override
    protected final ItemStack addGlow(@Nonnull ItemStack itemStack) {
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            meta.addStoredEnchant(Enchantment.DURABILITY, 1, true);
            itemStack.setItemMeta(meta);
        }
        else {
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }

        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    protected final void closeGUI() {
        if (prevGUI == null) {
            player.closeInventory();
        }
        else {
            prevGUI.open();
        }
    }

    @Nonnull
    @Override
    protected final ItemStack createItem(@Nonnull Material itemType, @Nonnull String name, @Nonnull String... description) {
        ItemStack itemStack = new ItemStack(itemType);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(description));
        return itemStack;
    }

    @Override
    protected final void delayedOpen() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::open);
    }

    @Override
    protected void delayedOpen(Supplier<AbstractSpigotChestGUI<J>> gui) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, gui::get);
    }

    @EventHandler
    public final void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getName().equals(inventory.getName()) && e.getInventory().getHolder().equals(player)) {
            e.setCancelled(true);
            if (buttons.contains(e.getRawSlot(), e.getClick())) {
                buttons.get(e.getRawSlot(), e.getClick()).accept((Player) e.getWhoClicked());
            }
        }
    }

    @EventHandler
    public final void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getName().equals(inventory.getName()) && e.getInventory().getHolder().equals(player)) {
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public final void onInventoryItemDrag(InventoryDragEvent e) {
        e.setCancelled(e.getInventory().getName().equals(inventory.getName()) && e.getInventory().getHolder().equals(player));
    }

    @Override
    public final void open() {
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
    protected final void set(int slot, @Nonnull ItemStack stack) {
        inventory.setItem(slot, stack);
    }

    @Override
    protected final void set(int slot, @Nonnull ClickType clickType, @Nonnull ItemStack stack, @Nonnull Consumer<Player> consumer) {
        set(slot, stack);
        buttons.put(slot, clickType, consumer);
    }

    @Override
    protected final void setBackButton(int slot, @Nonnull ClickType clickType, @Nonnull Material itemType) {
        ItemStack itemStack = createItem(itemType, ChatColor.RED + "Back", "Closes this menu and attempts", "to go back to the previous menu.");
        set(slot, clickType, itemStack, HumanEntity::closeInventory);
    }

    @Override
    protected final ItemStack setDurability(@Nonnull ItemStack itemStack, int durability) {
        itemStack.setDurability((short) durability);
        return itemStack;
    }

    @Override
    protected final ItemStack setPotionEffect(@Nonnull ItemStack itemStack, @Nonnull PotionType potionEffectType) {
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setBasePotionData(new PotionData(potionEffectType));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}

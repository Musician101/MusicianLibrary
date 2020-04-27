package io.musician101.musicianlibrary.java.minecraft.spigot.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotBookGUI implements Listener {

    private static final String IDENTIFIER = "\\_o<";
    private static final BiPredicate<JavaPlugin, ItemStack> BOOK_FILTER = (plugin, itemStack) -> {
        Material material = itemStack.getType();
        if (material == Material.WRITABLE_BOOK || material == Material.WRITTEN_BOOK) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                PersistentDataContainer data = itemStack.getItemMeta().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(plugin, "book_gui_identifier");
                if (data.has(key, PersistentDataType.STRING)) {
                    return IDENTIFIER.equals(data.get(key, PersistentDataType.STRING));
                }
            }
        }

        return false;
    };
    private static final String KEY = "book_gui_identifier";
    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static Method AS_NMS_COPY;
    private static Method GET_HANDLE;
    private static Object MAIN_HAND;
    private static Method OPEN_BOOK;

    static {
        try {
            Class<?> nmsItemStack = Class.forName("net.minecraft.server." + VERSION + ".ItemStack");
            Class<?> enumHand = Class.forName("net.minecraft.server." + VERSION + ".EnumHand");
            AS_NMS_COPY = Class.forName("org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack").getDeclaredMethod("asNMSCopy", ItemStack.class);
            OPEN_BOOK = Class.forName("net.minecraft.server." + VERSION + ".EntityPlayer").getDeclaredMethod("a", nmsItemStack, enumHand);
            GET_HANDLE = Class.forName("org.bukkit.craftbukkit." + VERSION + ".entity.CraftPlayer").getDeclaredMethod("getHandle");
            MAIN_HAND = enumHand.getEnumConstants()[0];
        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private final BiConsumer<Player, List<String>> action;
    private final int bookSlot;
    private final Player player;
    private final JavaPlugin plugin;

    public SpigotBookGUI(@Nonnull JavaPlugin plugin, @Nonnull Player player, @Nonnull ItemStack book, @Nonnull BiConsumer<Player, List<String>> action) {
        this.plugin = plugin;
        this.player = player;
        this.bookSlot = player.getInventory().getHeldItemSlot();
        this.action = action;
        PersistentDataContainer data = book.getItemMeta().getPersistentDataContainer();
        data.set(new NamespacedKey(plugin, KEY), PersistentDataType.STRING, IDENTIFIER);
        player.getInventory().setItemInMainHand(book);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static boolean isEditing(@Nonnull JavaPlugin plugin, @Nonnull Player player) {
        return Stream.of(player.getInventory().getContents()).anyMatch(itemStack -> BOOK_FILTER.test(plugin, itemStack));
    }

    public static void openWrittenBook(@Nonnull Player player, @Nonnull ItemStack book) {
        if (book.getType() != Material.WRITABLE_BOOK) {
            return;
        }

        ItemStack old = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(book);
        try {
            OPEN_BOOK.invoke(GET_HANDLE.invoke(player), AS_NMS_COPY.invoke(null, book), MAIN_HAND);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        player.getInventory().setItemInMainHand(old);
    }

    @EventHandler
    public void clickBook(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            event.setCancelled(isEditing((Player) event.getWhoClicked(), event.getCurrentItem()));
        }
    }

    @EventHandler
    public void dropBook(PlayerDropItemEvent event) {
        event.setCancelled(isEditing(event.getPlayer(), event.getItemDrop().getItemStack()));
    }

    @EventHandler
    public void editBook(PlayerEditBookEvent event) {
        BookMeta oldMeta = event.getPreviousBookMeta();
        PersistentDataContainer data = oldMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, KEY);
        if (data.has(key, PersistentDataType.STRING)) {
            ItemStack itemStack = new ItemStack(Material.WRITABLE_BOOK);
            BookMeta meta = event.getNewBookMeta();
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, data.get(key, PersistentDataType.STRING));
            itemStack.setItemMeta(meta);
            Player player = event.getPlayer();
            if (isEditing(player, itemStack)) {
                action.accept(player, meta.getPages());
                remove();
            }
        }
    }

    private boolean isEditing(@Nonnull Player player, @Nullable ItemStack itemStack) {
        return player.getUniqueId().equals(this.player.getUniqueId()) && itemStack != null && BOOK_FILTER.test(plugin, itemStack);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if (isEditing(player, inv.getItem(bookSlot))) {
            remove();
        }
    }

    private void remove() {
        player.getInventory().setItem(bookSlot, null);
        HandlerList.unregisterAll(this);
    }
}

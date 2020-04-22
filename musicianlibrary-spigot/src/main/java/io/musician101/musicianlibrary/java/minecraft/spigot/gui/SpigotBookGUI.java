package io.musician101.musicianlibrary.java.minecraft.spigot.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotBookGUI<J extends JavaPlugin> implements Listener {

    private static final String LORE_IDENTIFIER = "\\_o<";
    private static final Predicate<ItemStack> BOOK_FILTER = itemStack -> {
        Material material = itemStack.getType();
        if (material == Material.WRITABLE_BOOK || material == Material.WRITTEN_BOOK) {
            if (itemStack.hasItemMeta()) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.hasLore()) {
                    return itemMeta.getLore().contains(LORE_IDENTIFIER);
                }
            }
        }

        return false;
    };
    private final int bookSlot;
    private final BiConsumer<Player, List<String>> action;
    private final Player player;

    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static Method AS_NMS_COPY;
    private static Method GET_HANDLE;
    private static Method OPEN_BOOK;
    private static Object MAIN_HAND;

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

    public SpigotBookGUI(@Nonnull J plugin, @Nonnull Player player, @Nonnull ItemStack book, @Nonnull BiConsumer<Player, List<String>> action) {
        this.player = player;
        this.bookSlot = player.getInventory().getHeldItemSlot();
        this.action = action;
        ItemMeta meta = book.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(LORE_IDENTIFIER);
        meta.setLore(lore);
        book.setItemMeta(meta);
        player.getInventory().setItemInMainHand(book);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static boolean isEditing(@Nonnull Player player) {
        return Stream.of(player.getInventory().getContents()).anyMatch(BOOK_FILTER);
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
        if (oldMeta.hasLore() && oldMeta.getLore().contains(LORE_IDENTIFIER)) {
            ItemStack itemStack = new ItemStack(Material.WRITABLE_BOOK);
            BookMeta meta = event.getNewBookMeta();
            meta.setLore(oldMeta.getLore());
            itemStack.setItemMeta(meta);
            Player player = event.getPlayer();
            if (isEditing(player, itemStack)) {
                action.accept(player, meta.getPages());
                remove();
            }
        }
    }

    private boolean isEditing(@Nonnull Player player, @Nullable ItemStack itemStack) {
        return player.getUniqueId().equals(this.player.getUniqueId()) && itemStack != null && BOOK_FILTER.test(itemStack);
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
}

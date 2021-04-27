package io.musician101.musicianlibrary.java.minecraft.spigot.gui.book;

import io.musician101.musicianlibrary.java.minecraft.common.gui.book.BookGUI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

public class SpigotBookGUI extends BookGUI<JavaPlugin, Player, String> implements Listener {

    private static final String IDENTIFIER = "\\_o<";
    private static final String KEY = "book_gui_identifier";
    private static final BiPredicate<JavaPlugin, ItemStack> BOOK_FILTER = (plugin, itemStack) -> {
        Material material = itemStack.getType();
        if (material == Material.WRITABLE_BOOK || material == Material.WRITTEN_BOOK) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                PersistentDataContainer data = itemStack.getItemMeta().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(plugin, KEY);
                if (data.has(key, PersistentDataType.STRING)) {
                    return IDENTIFIER.equals(data.get(key, PersistentDataType.STRING));
                }
            }
        }

        return false;
    };

    public SpigotBookGUI(@Nonnull JavaPlugin plugin, @Nonnull Player player, @Nonnull ItemStack book, @Nonnull BiConsumer<Player, List<String>> action) throws PlayerHandNotEmptyException, BookGUIAlreadyOpenException {
        super(plugin, player, player.getInventory().getHeldItemSlot(), action);
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!itemStack.getType().isAir()) {
            throw new PlayerHandNotEmptyException();
        }

        if (Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).map(ItemStack::getItemMeta).filter(Objects::nonNull).map(ItemMeta::getPersistentDataContainer).anyMatch(pdc -> {
            String s = pdc.get(new NamespacedKey(plugin, KEY), PersistentDataType.STRING);
            return IDENTIFIER.equals(s);
        })) {
            throw new BookGUIAlreadyOpenException(player.getName());
        }

        PersistentDataContainer data = book.getItemMeta().getPersistentDataContainer();
        data.set(new NamespacedKey(plugin, KEY), PersistentDataType.STRING, IDENTIFIER);
        player.getInventory().setItemInMainHand(book);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static boolean isEditing(@Nonnull JavaPlugin plugin, @Nonnull Player player) {
        return Stream.of(player.getInventory().getContents()).anyMatch(itemStack -> BOOK_FILTER.test(plugin, itemStack));
    }

    public static void open(@Nonnull Player player, @Nonnull ItemStack book) {
        Material material = book.getType();
        if (material != Material.WRITABLE_BOOK) {
            throw new IllegalArgumentException("ItemStack with item ID " + material.getKey().toString() + " given. " + Material.WRITABLE_BOOK.getKey().toString() + " is required.");
        }

        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        FakeBookView fakeBookView;
        switch (version) {
            case "v1_16_R3":
                fakeBookView = new FakeBookView_1_16_R3(player, book);
                break;
            default:
                throw new UnsupportedOperationException(version + " is not supported.");
        }

        ItemStack old = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(book);
        fakeBookView.openBook();
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

    @Override
    protected void remove() {
        player.getInventory().setItem(bookSlot, null);
        HandlerList.unregisterAll(this);
    }
}

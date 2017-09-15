package io.musician101.musicianlibrary.java.minecraft.spigot.gui;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    private final Player player;
    private final int bookSlot;
    private static final String LORE_IDENTIFIER = "\\_o<";
    private static final Predicate<ItemStack> BOOK_FILTER = itemStack -> {
        Material material = itemStack.getType();
        if (material == Material.BOOK_AND_QUILL || material == Material.WRITTEN_BOOK) {
            if (itemStack.hasItemMeta()) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.hasLore()) {
                    return itemMeta.getLore().contains(LORE_IDENTIFIER);
                }
            }
        }

        return false;
    };
    private final Consumer<List<String>> consumer;

    public SpigotBookGUI(J plugin, Player player, ItemStack book, Consumer<List<String>> consumer) {
        this.player = player;
        this.bookSlot = player.getInventory().getHeldItemSlot();
        this.consumer = consumer;
        ItemMeta meta = book.getItemMeta();
        meta.setLore(Collections.singletonList(LORE_IDENTIFIER));
        book.setItemMeta(meta);
        player.getInventory().setItemInMainHand(book);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static boolean isEditing(Player player) {
        return !Stream.of(player.getInventory().getContents()).filter(BOOK_FILTER).collect(Collectors.toList()).isEmpty();
    }

    private boolean isEditing(Player player, ItemStack itemStack) {
        return player.getUniqueId().equals(this.player.getUniqueId()) && itemStack != null && BOOK_FILTER.test(itemStack);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if (isEditing(player, inv.getItem(bookSlot))) {
            inv.setItem(bookSlot, null);
            remove();
        }
    }

    @EventHandler
    public void clickBook(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            event.setCancelled(isEditing((Player) event.getWhoClicked(), event.getCurrentItem()));
        }
    }

    @EventHandler
    public void editBook(PlayerEditBookEvent event) {
        BookMeta oldMeta = event.getPreviousBookMeta();
        if (oldMeta.hasLore() && oldMeta.getLore().contains(LORE_IDENTIFIER)) {
            ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
            BookMeta meta = event.getNewBookMeta();
            meta.setLore(oldMeta.getLore());
            itemStack.setItemMeta(meta);
            Player player = event.getPlayer();
            if (isEditing(player, itemStack)) {
                consumer.accept(meta.getPages());
                remove();
            }
        }
    }

    @EventHandler
    public void dropBook(PlayerDropItemEvent event) {
        event.setCancelled(isEditing(event.getPlayer(), event.getItemDrop().getItemStack()));
    }

    private void remove() {
        player.getInventory().setItem(bookSlot, null);
        HandlerList.unregisterAll(this);
    }
}

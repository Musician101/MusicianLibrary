package io.musician101.musicianlibrary.java.minecraft.spigot.menu;

import io.musician101.musicianlibrary.java.minecraft.menu.AbstractBookMenu;
import java.util.List;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotBookMenu<J extends JavaPlugin> extends AbstractBookMenu<ItemStack, J, Player, Integer, PlayerInteractEvent, InventoryClickEvent, PlayerDropItemEvent, PlayerEditBookEvent> implements Listener {

    public SpigotBookMenu(Player player, ItemStack book, BiConsumer<Player, List<String>> biConsumer) {
        super(player, book, biConsumer);
    }

    @EventHandler
    @Override
    public void bookInteract(PlayerInteractEvent event) {
        if (player.getUniqueId().equals(event.getPlayer().getUniqueId()) && event.hasItem() && isSameBook(event.getItem()))
            Bukkit.getScheduler().cancelTask(taskId);
    }

    @EventHandler
    @Override
    public void clickBook(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (player.getUniqueId().equals(this.player.getUniqueId()) && isSameBook(event.getCurrentItem()))
                event.setCancelled(true);
        }
    }

    @Override
    protected void close() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    @Override
    public void closeBook(PlayerEditBookEvent event) {
        if (player.getUniqueId().equals(event.getPlayer().getUniqueId())) {
            ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
            itemStack.setItemMeta(event.getPreviousBookMeta());
            if (isSameBook(itemStack))
                biConsumer.accept(player, event.getNewBookMeta().getPages());
        }
    }

    @EventHandler
    @Override
    public void dropBook(PlayerDropItemEvent event) {
        if (player.getUniqueId().equals(event.getPlayer().getUniqueId()) && isSameBook(event.getItemDrop().getItemStack()))
            event.setCancelled(true);
    }

    @Override
    protected boolean isSameBook(ItemStack book) {
        return book.getType() == this.book.getType() && book.getItemMeta().equals(this.book.getItemMeta());
    }

    @Override
    protected void start(J plugin) {
        try {
            heldItem = player.getInventory().getItemInMainHand();
            player.getInventory().setItemInMainHand(book);
            Bukkit.getPluginManager().registerEvents(this, plugin);
            taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> player.getInventory().setItem(2, null), 100).getTaskId();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

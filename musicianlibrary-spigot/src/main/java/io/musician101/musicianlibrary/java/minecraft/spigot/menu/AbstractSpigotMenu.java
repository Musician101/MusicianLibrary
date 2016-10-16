package io.musician101.musicianlibrary.java.minecraft.spigot.menu;

import io.musician101.musicianlibrary.java.minecraft.AbstractMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.UUID;

@SuppressWarnings({"WeakerAccess", "unused"})
public class AbstractSpigotMenu extends AbstractMenu<InventoryCloseEvent, Inventory, SpigotClickEventHandler, InventoryClickEvent, PlayerQuitEvent, ItemStack> implements Listener
{
    protected AbstractSpigotMenu(JavaPlugin plugin, Inventory inv, SpigotClickEventHandler handler)
    {
        super(inv, handler);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    protected void destroy()
    {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    @Override
    public void onClick(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player))
            return;

        if (!inv.equals(event.getInventory()))
            return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType() == Material.AIR)
            return;

        int slot = event.getRawSlot();
        SpigotClickEvent clickEvent = new SpigotClickEvent(player, itemStack, slot);
        handler.handle(clickEvent);
        if (clickEvent.willClose())
            player.closeInventory();

        if (clickEvent.willDestroy())
            destroy();
    }

    @EventHandler
    @Override
    public void onClose(InventoryCloseEvent event)
    {
        if (!(event.getPlayer() instanceof Player))
            return;

        Inventory inv = event.getInventory();
        if (inv.equals(this.inv))
            destroy();
    }

    @EventHandler
    @Override
    public void onQuit(PlayerQuitEvent event)
    {
        if (inv.equals(event.getPlayer().getOpenInventory().getTopInventory()))
            destroy();
    }

    @Override
    public void open(UUID uuid)
    {
        Bukkit.getPlayer(uuid).openInventory(inv);
    }

    @Override
    public void setOption(int slot, ItemStack itemStack)
    {
        setOption(slot, itemStack, " ");
    }

    @Override
    public void setOption(int slot, ItemStack itemStack, String name)
    {
        setOption(slot, itemStack, name, new String[0]);
    }

    @Override
    public void setOption(int slot, ItemStack itemStack, String name, String... description)
    {
        setOption(slot, itemStack, name, false, description);
    }

    @Override
    public void setOption(int slot, ItemStack itemStack, String name, boolean willGlow, String... description)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(description));
        if (willGlow)
        {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(itemMeta);
        inv.setItem(slot, itemStack);
    }
}

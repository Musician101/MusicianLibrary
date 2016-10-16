package io.musician101.musicianlibrary.java.minecraft.spigot.menu;

import io.musician101.musicianlibrary.java.minecraft.AbstractMenu.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SpigotClickEvent extends ClickEvent<ItemStack, Player>
{
    public SpigotClickEvent(Player player, ItemStack itemStack, int slot)
    {
        super(player, itemStack, slot);
    }
}

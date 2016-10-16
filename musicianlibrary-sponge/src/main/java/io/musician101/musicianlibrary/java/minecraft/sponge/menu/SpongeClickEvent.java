package io.musician101.musicianlibrary.java.minecraft.sponge.menu;

import io.musician101.musicianlibrary.java.minecraft.AbstractMenu.ClickEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

@SuppressWarnings("WeakerAccess")
public class SpongeClickEvent extends ClickEvent<ItemStack, Player>
{
    public SpongeClickEvent(Player player, ItemStack itemStack, int slot)
    {
        super(player, itemStack, slot);
    }
}

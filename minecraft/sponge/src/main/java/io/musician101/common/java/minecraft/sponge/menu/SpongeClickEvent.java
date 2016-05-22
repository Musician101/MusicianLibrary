package io.musician101.common.java.minecraft.sponge.menu;

import io.musician101.common.java.minecraft.AbstractMenu.ClickEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

public class SpongeClickEvent extends ClickEvent<ItemStack, Player>
{
    public SpongeClickEvent(Player player, ItemStack itemStack, int slot)
    {
        super(player, itemStack, slot);
    }
}

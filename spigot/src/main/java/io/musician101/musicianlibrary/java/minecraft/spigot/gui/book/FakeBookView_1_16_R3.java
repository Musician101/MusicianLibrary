package io.musician101.musicianlibrary.java.minecraft.spigot.gui.book;

import javax.annotation.Nonnull;
import net.minecraft.server.v1_16_R3.EnumHand;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class FakeBookView_1_16_R3 implements FakeBookView {

    @Nonnull
    private final ItemStack book;
    @Nonnull
    private final Player player;

    public FakeBookView_1_16_R3(@Nonnull Player player, @Nonnull ItemStack book) {
        this.player = player;
        this.book = book;
    }

    @Override
    public void openBook() {
        ((CraftPlayer) player).getHandle().openBook(CraftItemStack.asNMSCopy(book), EnumHand.MAIN_HAND);
    }
}

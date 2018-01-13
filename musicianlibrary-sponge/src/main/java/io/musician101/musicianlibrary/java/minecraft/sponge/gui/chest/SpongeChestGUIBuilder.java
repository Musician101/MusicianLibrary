package io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.gui.chest.ChestGUIBuilder;
import io.musician101.musicianlibrary.java.minecraft.gui.chest.GUIButton;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.SpongeIconBuilder;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.anvil.SpongeJumpToPage;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class SpongeChestGUIBuilder extends ChestGUIBuilder<SpongeChestGUIBuilder, Class<? extends ClickInventoryEvent>, SpongeChestGUI, Inventory, PluginContainer, Player, ItemStack, Text> {

    SpongeChestGUIBuilder() {

    }

    @Nonnull
    @Override
    public SpongeChestGUI build() {
        checkNotNull(name, "Inventory name cannot be null.");
        checkArgument(page > 0, "Page must be greater than 0");
        checkNotNull(player, "Player cannot be null.");
        checkNotNull(plugin, "Plugin cannot be null.");
        checkArgument(size > 0 && size % 9 == 0, "Size must be greater than 0 and be a multiple of 9.");
        return new SpongeChestGUI(player, name, size, page, prevGUI, plugin, manualOpen);
    }

    @Nonnull
    @Override
    public SpongeChestGUIBuilder setBackButton(int slot, @Nonnull Class<? extends ClickInventoryEvent> clickType) {
        return setButton(new GUIButton<>(slot, clickType, SpongeIconBuilder.builder(ItemTypes.BARRIER).name(Text.builder("Back").color(TextColors.RED).build()).description(Text.of("Close this GUI and go back"), Text.of("to the previous GUI.")).build(), (gui, player) -> gui.close()));
    }

    @Nonnull
    @Override
    public SpongeChestGUIBuilder setJumpToPage(int slot, int maxPage, @Nonnull BiConsumer<Player, Integer> action) {
        return setButton(new GUIButton<>(slot, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.BOOK).name(Text.of("Jump To Page")).amount(page).build(), (gui, player) -> new SpongeJumpToPage<>(plugin, player, maxPage, action)));
    }

    @Nonnull
    @Override
    public SpongeChestGUIBuilder setPageNavigation(int slot, @Nonnull Text name, @Nonnull BiConsumer<SpongeChestGUI, Player> action) {
        return setButton(new GUIButton<>(slot, ClickInventoryEvent.Primary.class, SpongeIconBuilder.of(ItemTypes.ARROW, name), action));
    }
}

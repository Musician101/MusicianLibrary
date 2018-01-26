package io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.gui.chest.ChestGUIBuilder;
import io.musician101.musicianlibrary.java.minecraft.gui.chest.GUIButton;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.SpigotIconBuilder;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.anvil.SpigotJumpToPage;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class SpigotChestGUIBuilder<J extends JavaPlugin> extends ChestGUIBuilder<SpigotChestGUIBuilder<J>, ClickType, SpigotChestGUI<J>, Inventory, J, Player, ItemStack, String> {

    SpigotChestGUIBuilder() {

    }

    @Nonnull
    @Override
    public SpigotChestGUI<J> build() {
        checkNotNull(name, "Inventory name cannot be null.");
        checkArgument(page > 0, "Page must be greater than 0");
        checkNotNull(player, "Player cannot be null.");
        checkNotNull(plugin, "Plugin cannot be null.");
        checkArgument(size > 0 && size % 9 == 0, "Size must be greater than 0 and be a multiple of 9.");
        return new SpigotChestGUI<>(player, name, size, page, prevGUI, plugin, manualOpen);
    }

    @Nonnull
    @Override
    public SpigotChestGUIBuilder<J> setBackButton(int slot, @Nonnull ClickType clickType) {
        return setButton(GUIButton.of(slot, clickType, SpigotIconBuilder.builder(Material.BARRIER).name(ChatColor.RED + "Back").description("Close this GUI and go back", "to the previous GUI.").build(), (gui, player) -> gui.close()));
    }

    @Nonnull
    @Override
    public SpigotChestGUIBuilder<J> setJumpToPage(int slot, int maxPage, @Nonnull BiConsumer<Player, Integer> action) {
        return setButton(GUIButton.of(slot, ClickType.LEFT, SpigotIconBuilder.builder(Material.BOOK).name("Jump To Page").amount(page).build(), (gui, player) -> new SpigotJumpToPage<>(plugin, player, maxPage, action)));
    }

    @Nonnull
    @Override
    public SpigotChestGUIBuilder<J> setPageNavigation(int slot, @Nonnull String name, @Nonnull BiConsumer<SpigotChestGUI<J>, Player> action) {
        return setButton(GUIButton.of(slot, ClickType.LEFT, SpigotIconBuilder.of(Material.ARROW, name), action));
    }
}

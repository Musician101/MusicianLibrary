package io.musician101.musicianlibrary.java.minecraft.spigot.menu.chest;

import io.musician101.musicianlibrary.java.minecraft.spigot.menu.anvil.JumpToPage;
import io.musician101.musicianlibrary.java.util.TriConsumer;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractSpigotPagedMenu<J extends JavaPlugin> extends AbstractSpigotChestMenu<J> {

    protected final int page;

    public AbstractSpigotPagedMenu(@Nonnull Player player, int size, @Nonnull String name, int page, @Nullable AbstractSpigotChestMenu<J> prevMenu, @Nonnull J plugin) {
        super(player, size, name, prevMenu, plugin);
        this.page = page;
    }

    public AbstractSpigotPagedMenu(@Nonnull Player player, int size, @Nonnull String name, int page, @Nullable AbstractSpigotChestMenu<J> prevMenu, @Nonnull J plugin, boolean manualOpen) {
        super(player, size, name, prevMenu, plugin, manualOpen);
        this.page = page;
    }

    protected void setContents(List<ItemStack> contents, BiFunction<Player, ItemStack, Consumer<Player>> consumerMapper) {
        for (int x = 0; x < 45; x++) {
            try {
                ItemStack itemStack = contents.get(x + (page - 1) * 45);
                if (consumerMapper != null) {
                    set(x, itemStack, consumerMapper.apply(player, itemStack));
                }
                else {
                    set(x, itemStack);
                }
            }
            catch (IndexOutOfBoundsException e) {
                //Used to skip populating slots if the list is too small to fill the page.
            }
        }
    }

    protected void setJumpToPage(int slot, int maxPage, TriConsumer<Player, Integer, AbstractSpigotChestMenu<J>> triConsumer) {
        ItemStack itemStack = createItem(Material.BOOK, "Jump To Page");
        itemStack.setAmount(page);
        set(slot, itemStack, player -> new JumpToPage<>(plugin, player, maxPage, prevMenu, triConsumer));
    }

    protected void setPageNavigationPage(int slot, @Nonnull String name, @Nonnull Consumer<Player> consumer) {
        set(slot, createItem(Material.ARROW, name), consumer);
    }
}

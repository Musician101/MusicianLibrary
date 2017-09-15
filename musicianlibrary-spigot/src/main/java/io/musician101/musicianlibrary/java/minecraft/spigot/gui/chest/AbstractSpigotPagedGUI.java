package io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.spigot.gui.anvil.SpigotJumpToPage;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractSpigotPagedGUI<J extends JavaPlugin> extends AbstractSpigotChestGUI<J> {

    protected final int page;

    public AbstractSpigotPagedGUI(@Nonnull Player player, int size, @Nonnull String name, int page, @Nullable AbstractSpigotChestGUI<J> prevMenu, @Nonnull J plugin) {
        super(player, name, size, prevMenu, plugin);
        if (size <= 9) {
            throw new IllegalArgumentException("Paged GUIs need to be at least 18 slots.");
        }

        this.page = page;
    }

    public AbstractSpigotPagedGUI(@Nonnull Player player, int size, @Nonnull String name, int page, @Nullable AbstractSpigotChestGUI<J> prevMenu, @Nonnull J plugin, boolean manualOpen) {
        super(player, name, size, prevMenu, plugin, manualOpen);
        if (size <= 9) {
            throw new IllegalArgumentException("Paged GUIs need to be at least 18 slots.");
        }

        this.page = page;
    }

    protected <T> void setContents(@Nonnull List<T> contents, @Nonnull Function<T, ItemStack> itemStackMapper, @Nullable BiFunction<Player, T, Consumer<Player>> consumerMapper) {
        int size = inventory.getSize() - 9;
        for (int x = 0; x < size; x++) {
            try {
                T content = contents.get(x + (page - 1) * size);
                ItemStack itemStack = itemStackMapper.apply(content);
                if (consumerMapper != null) {
                    set(x, ClickType.LEFT, itemStack, consumerMapper.apply(player, content));
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

    protected void setJumpToPage(int slot, int maxPage, BiConsumer<Player, Integer> triConsumer) {
        ItemStack itemStack = createItem(Material.BOOK, "Jump To Page");
        itemStack.setAmount(page);
        set(slot, ClickType.LEFT, itemStack, player -> new SpigotJumpToPage<>(plugin, player, maxPage, triConsumer));
    }

    protected void setPageNavigation(int slot, @Nonnull String name, @Nonnull Consumer<Player> consumer) {
        set(slot, ClickType.LEFT, createItem(Material.ARROW, name), consumer);
    }
}

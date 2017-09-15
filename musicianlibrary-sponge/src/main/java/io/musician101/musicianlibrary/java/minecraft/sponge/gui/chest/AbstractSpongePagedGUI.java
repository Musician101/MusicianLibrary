package io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.config.AbstractConfig;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.anvil.SpongeJumpToPage;
import io.musician101.musicianlibrary.java.minecraft.sponge.plugin.AbstractSpongePlugin;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public abstract class AbstractSpongePagedGUI<C extends AbstractConfig, J extends AbstractSpongePlugin<C>> extends AbstractSpongeChestGUI<C, J> {

    protected final int page;

    public AbstractSpongePagedGUI(@Nonnull Player player, @Nonnull String name, int size, int page, @Nullable AbstractSpongeChestGUI<C, J> prevMenu, @Nonnull J plugin) {
        super(player, name, size, prevMenu, plugin);
        if (size <= 9) {
            throw new IllegalArgumentException("Paged GUIs need to be at least 18 slots.");
        }

        this.page = page;
    }

    public AbstractSpongePagedGUI(@Nonnull Player player, @Nonnull String name, int size, int page, @Nullable AbstractSpongeChestGUI<C, J> prevMenu, @Nonnull J plugin, boolean manualOpen) {
        super(player, name, size, prevMenu, plugin, manualOpen);
        if (size <= 9) {
            throw new IllegalArgumentException("Paged GUIs need to be at least 18 slots.");
        }

        this.page = page;
    }

    protected <T> void setContents(@Nonnull List<T> contents, @Nonnull Function<T, ItemStack> itemStackMapper, @Nullable BiFunction<Player, T, Consumer<Player>> consumerMapper) {
        int size = inventory.capacity() - 9;
        for (int x = 0; x < size; x++) {
            try {
                T content = contents.get(x + (page - 1) * size);
                ItemStack itemStack = itemStackMapper.apply(content);
                if (consumerMapper != null) {
                    set(x, ClickInventoryEvent.Primary.class, itemStack, consumerMapper.apply(player, content));
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

    protected void setJumpToPage(int slot, int maxPage, BiConsumer<Player, Integer> biConsumer) {
        ItemStack itemStack = createItem(ItemTypes.BOOK, Text.of("Jump To Page"));
        itemStack.setQuantity(page);
        set(slot, ClickInventoryEvent.Primary.class, itemStack, player -> new SpongeJumpToPage<>(plugin, player, maxPage, biConsumer));
    }

    protected void setPageNavigation(int slot, @Nonnull String name, @Nonnull Consumer<Player> consumer) {
        set(slot, ClickInventoryEvent.Primary.class, createItem(ItemTypes.ARROW, Text.of(name)), consumer);
    }
}

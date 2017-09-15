package io.musician101.musicianlibrary.java.minecraft.menu;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractChestMenu<C, E, F, G extends AbstractChestMenu<C, E, F, G, I, J, P, S, T>, I, J, P, S, T> {

    protected final Table<Integer, C, Consumer<P>> buttons = HashBasedTable.create();
    @Nonnull
    protected final I inventory;
    @Nonnull
    protected final J plugin;
    @Nonnull
    protected final P player;
    @Nullable
    protected final G prevGUI;

    public AbstractChestMenu(@Nonnull I inventory, @Nonnull P player, @Nullable G prevGUI, @Nonnull J plugin, boolean manualOpen) {
        this.inventory = inventory;
        this.player = player;
        this.prevGUI = prevGUI;
        this.plugin = plugin;
        if (!manualOpen) {
            delayedOpen();
        }
    }

    protected abstract void build();

    @SuppressWarnings("unchecked")
    @Nonnull
    protected abstract S createItem(@Nonnull T itemType, @Nonnull F name, @Nonnull F... description);

    public abstract void open();

    protected abstract void delayedOpen();

    protected abstract void delayedOpen(Supplier<G> gui);

    protected abstract void set(int slot, @Nonnull C clickType, @Nonnull S itemStack, @Nonnull Consumer<P> consumer);

    protected abstract void set(int slot, @Nonnull S itemStack);

    protected abstract void setBackButton(int slot, @Nonnull C clickType, @Nonnull T itemType);

    protected abstract S addGlow(@Nonnull S itemStack);

    protected final S addGlowIfConditionsMet(@Nonnull S itemStack, @Nonnull Supplier<Boolean> conditionSupplier) {
        if (conditionSupplier.get()) {
            return addGlow(itemStack);
        }

        return itemStack;
    }

    protected abstract S setPotionEffect(@Nonnull S itemStack, @Nonnull E potionEffectType);

    protected abstract S setDurability(@Nonnull S itemStack, int durability);

    protected abstract void closeGUI();
}

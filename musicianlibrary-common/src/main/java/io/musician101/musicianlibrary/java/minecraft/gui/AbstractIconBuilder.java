package io.musician101.musicianlibrary.java.minecraft.gui;

import io.musician101.musicianlibrary.java.minecraft.util.Builder;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public abstract class AbstractIconBuilder<B extends AbstractIconBuilder<B, I, P, T>, I, P, T> implements Builder<B, I> {

    protected I itemStack;

    protected AbstractIconBuilder(I itemStack) {
        this.itemStack = itemStack;
    }

    @Nonnull
    public abstract B addGlow();

    @Nonnull
    @SuppressWarnings("unchecked")
    public final B addGlowIfConditionsMet(@Nonnull Supplier<Boolean> conditionSupplier) {
        return conditionSupplier.get() ? addGlow() : (B) this;
    }

    @Nonnull
    public abstract B amount(int amount);

    @Nonnull
    @Override
    public I build() {
        return itemStack;
    }

    @SafeVarargs
    @Nonnull
    public final B description(@Nonnull T... description) {
        return description(Arrays.asList(description));
    }

    @Nonnull
    public abstract B description(@Nonnull List<T> description);

    @Nonnull
    public abstract B durability(int durability);

    @Nonnull
    public abstract B name(@Nonnull T name);

    @Nonnull
    public abstract B potionEffect(@Nonnull P potionType);
}

package io.musician101.musicianlibrary.java.minecraft.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.util.Builder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class GUIButton<C, G extends ChestGUI<C, G, I, J, P, S>, I, J, P, S> {

    @Nonnull
    private final Map<C, BiConsumer<G, P>> actions;
    @Nonnull
    private final S itemStack;
    private final int slot;

    GUIButton(int slot, @Nonnull S itemStack, @Nonnull Map<C, BiConsumer<G, P>> actions) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.actions = actions;
    }

    public static <C, G extends ChestGUI<C, G, I, J, P, S>, I, J, P, S> GUIButtonBuilder<C, G, I, J, P, S> builder() {
        return new GUIButtonBuilder<>();
    }

    public static <C, G extends ChestGUI<C, G, I, J, P, S>, I, J, P, S> GUIButton<C, G, I, J, P, S> of(int slot, C clickType, S icon, BiConsumer<G, P> action) {
        return GUIButton.<C, G, I, J, P, S>builder().slot(slot).icon(icon).addButton(clickType, action).build();
    }

    public static <C, G extends ChestGUI<C, G, I, J, P, S>, I, J, P, S> GUIButton<C, G, I, J, P, S> of(int slot, S icon) {
        return GUIButton.<C, G, I, J, P, S>builder().slot(slot).icon(icon).build();
    }

    @Nonnull
    public Optional<BiConsumer<G, P>> getAction(C clickType) {
        return Optional.ofNullable(actions.get(clickType));
    }

    @Nonnull
    public final S getItemStack() {
        return itemStack;
    }

    public final int getSlot() {
        return slot;
    }

    public static final class GUIButtonBuilder<C, G extends ChestGUI<C, G, I, J, P, S>, I, J, P, S> implements Builder<GUIButtonBuilder<C, G, I, J, P, S>, GUIButton<C, G, I, J, P, S>> {

        private Map<C, BiConsumer<G, P>> actions = new HashMap<>();
        private S itemStack;
        private int slot;

        private GUIButtonBuilder() {

        }

        @Nonnull
        public GUIButtonBuilder<C, G, I, J, P, S> addButton(@Nonnull C clickType, @Nullable BiConsumer<G, P> action) {
            actions.put(clickType, action);
            return this;
        }

        @Nonnull
        public GUIButtonBuilder<C, G, I, J, P, S> addButtons(@Nonnull Map<C, BiConsumer<G, P>> actions) {
            this.actions = actions;
            return this;
        }

        @Nonnull
        @Override
        public GUIButton<C, G, I, J, P, S> build() {
            checkNotNull(itemStack, "Button Icon can not be null.");
            checkArgument(slot >= 0, "Slot must be grater than 0.");
            return new GUIButton<>(slot, itemStack, actions);
        }

        @Nonnull
        public GUIButtonBuilder<C, G, I, J, P, S> icon(@Nonnull S itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        @Nonnull
        @Override
        public GUIButtonBuilder<C, G, I, J, P, S> reset() {
            itemStack = null;
            slot = 0;
            actions.clear();
            return this;
        }

        @Nonnull
        public GUIButtonBuilder<C, G, I, J, P, S> slot(int slot) {
            this.slot = slot;
            return this;
        }
    }
}

package io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.common.gui.chest.AbstractIconBuilder;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public class SpongeIconBuilder extends AbstractIconBuilder<SpongeIconBuilder, ItemStack, PotionEffectType, Component> {

    private SpongeIconBuilder(ItemType itemType) {
        super(ItemStack.of(itemType, 1));
    }

    public SpongeIconBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    @Nonnull
    public static SpongeIconBuilder builder(@Nonnull ItemStack itemStack) {
        return new SpongeIconBuilder(itemStack);
    }

    @Nonnull
    public static SpongeIconBuilder builder(@Nonnull ItemType itemType) {
        return new SpongeIconBuilder(itemType);
    }

    @Nonnull
    public static ItemStack of(@Nonnull ItemType itemType, @Nonnull Component name) {
        return builder(itemType).name(name).build();
    }

    @Nonnull
    @Override
    public SpongeIconBuilder addGlow(boolean addGlow) {
        if (addGlow) {
            if (itemStack.getType() == ItemTypes.ENCHANTED_BOOK.get()) {
                itemStack.offer(Keys.STORED_ENCHANTMENTS, Collections.singletonList(Enchantment.of(EnchantmentTypes.UNBREAKING, 1)));
            }
            else {
                itemStack.offer(Keys.APPLIED_ENCHANTMENTS, Collections.singletonList(Enchantment.of(EnchantmentTypes.UNBREAKING, 1)));
            }

            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        return this;
    }

    @Nonnull
    @Override
    public SpongeIconBuilder amount(int amount) {
        itemStack.setQuantity(amount);
        return this;
    }

    @Nonnull
    @Override
    public SpongeIconBuilder description(@Nonnull List<Component> description) {
        itemStack.offer(Keys.LORE, description);
        return this;
    }

    @Nonnull
    @Override
    public SpongeIconBuilder durability(int durability) {
        itemStack.offer(Keys.ITEM_DURABILITY, durability);
        return this;
    }

    @Nonnull
    @Override
    public SpongeIconBuilder name(@Nonnull Component name) {
        itemStack.offer(Keys.DISPLAY_NAME, name);
        return this;
    }

    @Nonnull
    public <T> SpongeIconBuilder offer(Key<Value<T>> key, T value) {
        itemStack.offer(key, value);
        return this;
    }

    @Nonnull
    @Override
    public SpongeIconBuilder potionEffect(@Nonnull PotionEffectType potionType) {
        itemStack.offer(Keys.POTION_EFFECTS, Collections.singletonList(PotionEffect.of(potionType, 1, 1)));
        return this;
    }

    @Nonnull
    @Override
    public SpongeIconBuilder reset() {
        itemStack = ItemStack.of(itemStack.getType(), 1);
        return this;
    }
}

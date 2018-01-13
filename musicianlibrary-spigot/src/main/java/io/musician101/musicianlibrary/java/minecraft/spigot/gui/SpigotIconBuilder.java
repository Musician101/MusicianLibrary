package io.musician101.musicianlibrary.java.minecraft.spigot.gui;

import io.musician101.musicianlibrary.java.minecraft.gui.AbstractIconBuilder;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public final class SpigotIconBuilder extends AbstractIconBuilder<SpigotIconBuilder, ItemStack, PotionType, String> {

    private SpigotIconBuilder(Material material) {
        super(new ItemStack(material));
    }

    public static SpigotIconBuilder builder(@Nonnull Material material) {
        return new SpigotIconBuilder(material);
    }

    public static ItemStack of(@Nonnull Material material, @Nonnull String name) {
        return builder(material).name(name).build();
    }

    @Nonnull
    @Override
    public SpigotIconBuilder addGlow() {
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            meta.addStoredEnchant(Enchantment.DURABILITY, 1, true);
            itemStack.setItemMeta(meta);
        }
        else {
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }

        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public SpigotIconBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @Nonnull
    @Override
    public SpigotIconBuilder description(@Nonnull List<String> description) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(description);
        return this;
    }

    @Nonnull
    @Override
    public SpigotIconBuilder durability(int durability) {
        itemStack.setDurability((short) durability);
        return this;
    }

    @Nonnull
    @Override
    public SpigotIconBuilder name(@Nonnull String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Nonnull
    @Override
    public SpigotIconBuilder potionEffect(@Nonnull PotionType potionType) {
        if (itemStack.getItemMeta() instanceof PotionMeta) {
            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            potionMeta.setBasePotionData(new PotionData(potionType));
            potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemStack.setItemMeta(potionMeta);
        }

        return this;
    }

    @Nonnull
    @Override
    public SpigotIconBuilder reset() {
        itemStack = new ItemStack(itemStack.getType());
        return this;
    }
}

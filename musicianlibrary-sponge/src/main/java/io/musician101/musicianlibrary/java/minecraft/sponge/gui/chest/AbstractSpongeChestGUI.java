package io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.config.AbstractConfig;
import io.musician101.musicianlibrary.java.minecraft.menu.AbstractChestMenu;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.key.MLKeys;
import io.musician101.musicianlibrary.java.minecraft.sponge.plugin.AbstractSpongePlugin;
import io.musician101.musicianlibrary.java.minecraft.sponge.utils.TextUtils;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent.Close;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

public abstract class AbstractSpongeChestGUI<C extends AbstractConfig, J extends AbstractSpongePlugin<C>> extends AbstractChestMenu<Class<? extends ClickInventoryEvent>, PotionEffectType, Text, AbstractSpongeChestGUI<C, J>, Inventory, J, Player, ItemStack, ItemType> {

    public AbstractSpongeChestGUI(@Nonnull Player player, @Nonnull String name, int size, @Nullable AbstractSpongeChestGUI<C, J> prevMenu, J plugin) {
        this(player, name, size, prevMenu, plugin, false);
    }

    public AbstractSpongeChestGUI(@Nonnull Player player, @Nonnull String name, int size, @Nullable AbstractSpongeChestGUI<C, J> prevMenu, @Nonnull J plugin, boolean manualOpen) {
        super(parseInventory(name, size, plugin), player, prevMenu, plugin, manualOpen);
    }

    private static Inventory parseInventory(@Nonnull String name, int size, @Nonnull AbstractSpongePlugin plugin) {
        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(new InventoryCapacity(size)).title(Text.of(name));
        for (int i = 0; i < size; i++) {
            builder.with(InventoryArchetype.builder().from(InventoryArchetypes.SLOT).property(new SlotIndex(i)).build("minecraft:slot" + i, "Slot"));
        }

        return Inventory.builder().of(builder.build(plugin.getId() + ":" + name.replace("\\s", "_").toLowerCase(), name)).build(plugin);
    }

    @Nonnull
    @Override
    protected final ItemStack createItem(@Nonnull ItemType itemType, @Nonnull Text name, @Nonnull Text... description) {
        return ItemStack.builder().itemType(itemType).add(Keys.DISPLAY_NAME, name).add(Keys.ITEM_LORE, Stream.of(description).collect(Collectors.toList())).build();
    }

    private boolean isSameInventory(@Nonnull Inventory inventory, @Nonnull Player player) {
        return inventory.getName().equals(this.inventory.getName()) && player.getUniqueId().equals(this.player.getUniqueId());
    }

    @Listener
    public void onInventoryClick(ClickInventoryEvent event, @First Player player) {
        Container container = event.getTargetInventory();
        if (isSameInventory(container, player)) {
            event.setCancelled(true);
            event.getCursorTransaction().getFinal().createStack().get(MLKeys.SLOT).filter(slot -> buttons.contains(slot, event.getClass())).ifPresent(slot -> buttons.get(slot, event.getClass()).accept(player));
        }
    }

    @Listener
    public final void onInventoryClose(Close event, @First Player player) {
        if (event.getTargetInventory().getName().equals(inventory.getName()) && event.getTargetInventory().getViewers().contains(player)) {
            Sponge.getEventManager().unregisterListeners(this);
        }
    }

    @Override
    public void open() {
        inventory.clear();
        build();
        player.openInventory(inventory);
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    protected final void delayedOpen() {
        Task.builder().execute(this::open).delayTicks(1L).submit(plugin);
    }

    @Override
    protected void delayedOpen(Supplier<AbstractSpongeChestGUI<C, J>> gui) {
        Task.builder().execute(gui::get).delayTicks(1L).submit(plugin);
    }

    @Override
    protected final void set(int slot, @Nonnull ItemStack itemStack) {
        itemStack.offer(MLKeys.SLOT, slot);
        inventory.query(new SlotIndex(slot)).set(itemStack);
    }

    @Override
    protected final void set(int slot, @Nonnull Class<? extends ClickInventoryEvent> clickType, @Nonnull ItemStack itemStack, @Nonnull Consumer<Player> consumer) {
        set(slot, itemStack);
        buttons.put(slot, clickType, consumer);
    }

    @Override
    protected final void setBackButton(int slot, @Nonnull Class<? extends ClickInventoryEvent> clickType, @Nonnull ItemType itemType) {
        ItemStack itemStack = createItem(itemType, TextUtils.redText("Back"), Text.of("Closes this menu and attempts"), Text.of("to back to the previous menu."));
        set(slot, clickType, itemStack, player -> closeGUI());
    }

    @Override
    protected final void closeGUI() {
        if (prevGUI == null) {
            player.closeInventory();
        }
        else {
            prevGUI.open();
        }
    }

    @Override
    protected final ItemStack addGlow(@Nonnull ItemStack itemStack) {
        itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(new ItemEnchantment(Enchantments.UNBREAKING, 1)));
        itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        return itemStack;
    }

    @Override
    protected final ItemStack setPotionEffect(@Nonnull ItemStack itemStack, @Nonnull PotionEffectType potionEffectType) {
        itemStack.offer(Keys.POTION_EFFECTS, Collections.singletonList(PotionEffect.of(potionEffectType, 1, 1)));
        return itemStack;
    }

    @Override
    protected final ItemStack setDurability(@Nonnull ItemStack itemStack, int durability) {
        itemStack.offer(Keys.ITEM_DURABILITY, durability);
        return itemStack;
    }

    protected final <T extends CatalogType> ItemStack setType(ItemStack itemStack, Key<Value<T>> key, T type) {
        itemStack.offer(key, type);
        return itemStack;
    }
}

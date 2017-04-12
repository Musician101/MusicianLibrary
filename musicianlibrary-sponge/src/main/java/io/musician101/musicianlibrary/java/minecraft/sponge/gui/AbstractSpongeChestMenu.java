package io.musician101.musicianlibrary.java.minecraft.sponge.gui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.musician101.musicianlibrary.java.minecraft.menu.AbstractChestMenu;
import io.musician101.musicianlibrary.java.minecraft.sponge.utils.TextUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent.Close;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

public abstract class AbstractSpongeChestMenu<J> extends AbstractChestMenu<Text, AbstractSpongeChestMenu<J>, Inventory, String, Player, ItemStack, ItemType> {

    private final Map<Integer, ItemStack> slots = new HashMap<>();

    public AbstractSpongeChestMenu(@Nonnull Player player, @Nonnull String name, int size, @Nullable AbstractSpongeChestMenu<J> prevMenu, PluginContainer plugin) {
        this(player, name, size, prevMenu, plugin, false);
    }

    public AbstractSpongeChestMenu(@Nonnull Player player, @Nonnull String name, int size, @Nullable AbstractSpongeChestMenu<J> prevMenu, @Nonnull PluginContainer plugin, boolean manualOpen) {
        super(parseInventory(name, size, plugin), player, prevMenu);
    }

    private static Inventory parseInventory(@Nonnull String name, int size, @Nonnull PluginContainer plugin) {
        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(new InventoryCapacity(size)).title(Text.of(name));
        for (int i = 0; i < size; i++) {
            builder.with(InventoryArchetype.builder().from(InventoryArchetypes.SLOT).property(new SlotIndex(i)).build("minecraft:slot" + i, "Slot"));
        }

        return Inventory.builder().of(builder.build(plugin.getId() + ":" + name.replace("\\s", "_").toLowerCase(), name)).build(plugin);
    }

    @Override
    protected void close() {
        Sponge.getEventManager().unregisterListeners(this);
        if (prevMenu != null)
            prevMenu.open();
    }

    @Listener
    public void onInventoryClick(ClickInventoryEvent event, @First Player player) {
        Container container = event.getTargetInventory();
        if (isSameInventory(container, player)) {
            event.setCancelled(true);
            if (event instanceof ClickInventoryEvent.Creative || event instanceof ClickInventoryEvent.Drag || event instanceof ClickInventoryEvent.Shift || event instanceof ClickInventoryEvent.NumberPress || event instanceof ClickInventoryEvent.Drop) {
                player.sendMessage(Text.of("That click type is not supported."));
            }
            else {
                BiMap<ItemStack, Integer> inverseSlots = HashBiMap.create(slots).inverse();
                ItemStack itemStack = event.getCursorTransaction().getFinal().createStack();
                if (inverseSlots.containsKey(itemStack)) {
                    int slot = inverseSlots.get(itemStack);
                    if (buttons.containsKey(slot)) {
                        buttons.get(slot).accept(player);
                    }
                }
            }
        }
    }

    @Listener
    public void onInventoryClose(Close event, @First Player player) {
        if (event.getTargetInventory().getName().equals(inventory.getName()) && event.getTargetInventory().getViewers().contains(player))
            close();
    }

    private boolean isSameInventory(@Nonnull Inventory inventory, @Nonnull Player player) {
        return inventory.getName().equals(this.inventory.getName()) && inventory.getPlugin().getId().equals(this.inventory.getPlugin().getId()) && player.getUniqueId().equals(this.player.getUniqueId());
    }

    @Override
    public void open() {
        inventory.clear();
        build();
        for (int i = 0; i < inventory.capacity(); i++) {
            inventory.query(new SlotIndex(i)).set(slots.getOrDefault(i, ItemStack.of(ItemTypes.NONE, 1)));
        }

        PluginContainer plugin = inventory.getPlugin();
        player.openInventory(inventory, Cause.of(NamedCause.source(plugin)));
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack, @Nonnull Consumer<Player> consumer) {
        set(slot, itemStack);
        buttons.put(itemStack.get(Keys.DISPLAY_NAME).map(Text::toPlain).orElse(itemStack.getTranslation().get()), consumer);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack) {
        inventory.query(SlotIndex.of(slot)).set(itemStack);
        slots.put(slot, itemStack);
    }

    @Nonnull
    @Override
    protected ItemStack createItem(@Nonnull ItemType itemType, @Nonnull Text name, @Nonnull Text... description) {
        return ItemStack.builder().itemType(itemType).add(Keys.DISPLAY_NAME, name)
                .add(Keys.ITEM_LORE, Stream.of(description).map(Text::of).collect(Collectors.toList())).build();
    }

    @Override
    protected void setBackButton(int slot, @Nonnull ItemType itemType) {
        ItemStack itemStack = createItem(itemType, TextUtils.redText("Back"), Text.of("Closes this menu and attempts"), Text.of("to back to the previous menu."));
        set(slot, itemStack, player -> player.closeInventory(Cause.of(NamedCause.source(inventory.getPlugin()))));
    }
}
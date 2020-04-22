package io.musician101.musicianlibrary.java.minecraft.sponge.gui;

import io.musician101.musicianlibrary.java.minecraft.common.gui.ChestGUI;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.key.MLKeys;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent.Close;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.type.OrderedInventory;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public abstract class SpongeChestGUI extends ChestGUI<Class<? extends ClickInventoryEvent>, Inventory, PluginContainer, Player, ItemStack, Text, Container, ClickInventoryEvent, Close> {

    protected SpongeChestGUI(@Nonnull Player player, @Nonnull Text name, int size, @Nonnull PluginContainer plugin, boolean manualOpen) {
        super(parseInventory(name, size, plugin), name, player, plugin, manualOpen);
    }

    private static Inventory parseInventory(@Nonnull Text name, int size, @Nonnull PluginContainer plugin) {
        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(InventoryCapacity.of(size)).title(Text.of(name));
        for (int i = 0; i < size; i++) {
            builder.with(InventoryArchetype.builder().from(InventoryArchetypes.SLOT).property(SlotIndex.of(i)).build("minecraft:slot" + i, "Slot"));
        }

        String plainName = TextSerializers.PLAIN.serialize(name);
        return Inventory.builder().of(builder.build(plugin.getId() + ":" + plainName.replace("\\s", "_").toLowerCase(), plainName)).build(plugin);
    }

    private boolean isSameInventory(@Nonnull Inventory inventory, @Nonnull Player player) {
        return inventory.getName().equals(this.inventory.getName()) && player.getUniqueId().equals(this.player.getUniqueId());
    }

    @Listener
    public void onInventoryClick(ClickInventoryEvent event, @First Player player) {
        Container container = event.getTargetInventory();
        if (isSameInventory(container, player)) {
            event.setCancelled(true);
            int slot = event.getCursorTransaction().getFinal().createStack().get(MLKeys.SLOT).orElse(-1);
            buttons.stream().filter(button -> button.getSlot() == slot).findFirst().flatMap(button -> button.getAction(event.getClass())).ifPresent(consumer -> consumer.accept(player));
        }
    }

    @Listener
    public final void onInventoryClose(Close event, @First Player player) {
        if (event.getTargetInventory().getName().equals(inventory.getName()) && event.getTargetInventory().getViewers().stream().map(Player::getUniqueId).anyMatch(uuid -> uuid.equals(player.getUniqueId()))) {
            Sponge.getEventManager().unregisterListeners(this);
        }
    }

    @Override
    protected boolean isCorrectInventory(@Nonnull Container inventories) {
        return inventories.getInventoryProperty(InventoryTitle.class).filter(inventoryTitle -> {
            Text title = inventoryTitle.getValue();
            if (title == null) {
                return false;
            }

            return title.equals(name) && inventories.getViewers().stream().anyMatch(p -> p.getUniqueId().equals(player.getUniqueId()));
        }).isPresent();
    }

    @Override
    protected void addItem(int slot, @Nonnull ItemStack itemStack) {
        inventory.<OrderedInventory>query(QueryOperationTypes.INVENTORY_TYPE.of(OrderedInventory.class)).getSlot(new SlotIndex(slot)).ifPresent(slotIndex -> slotIndex.set(itemStack));
    }

    @Override
    protected void removeItem(int slot) {
        inventory.<OrderedInventory>query(QueryOperationTypes.INVENTORY_TYPE.of(OrderedInventory.class)).getSlot(new SlotIndex(slot)).ifPresent(slotIndex -> slotIndex.set(ItemStack.empty()));
    }

    @Override
    public void open() {
        Task.builder().execute(() -> {
            inventory.clear();
            buttons.forEach(button -> inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(button.getSlot()))).set(button.getItemStack()));
            player.openInventory(inventory);
            Sponge.getEventManager().registerListeners(plugin, this);
        }).delayTicks(1L).submit(plugin);
    }
}

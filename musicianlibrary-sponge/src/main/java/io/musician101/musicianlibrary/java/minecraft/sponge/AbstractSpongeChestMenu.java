package io.musician101.musicianlibrary.java.minecraft.sponge;

import io.musician101.musicianlibrary.java.minecraft.menu.AbstractChestMenu;
import java.util.function.Consumer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent.Drag;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent.Close;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.type.OrderedInventory;

public abstract class AbstractSpongeChestMenu<J> extends AbstractChestMenu<Consumer<Player>, OrderedInventory, J, Player, ItemStack, Drag, ClickInventoryEvent, Close> {

    protected AbstractSpongeChestMenu(Player player, InventoryArchetype inventoryArchetype, String name, J plugin) {
        this(player, inventoryArchetype, name, plugin, false);
    }

    protected AbstractSpongeChestMenu(Player player, InventoryArchetype inventoryArchetype, String name, J plugin, boolean manualOpen) {
        super((OrderedInventory) Inventory.builder().of(inventoryArchetype).build(plugin), player);
        if (!manualOpen)
            open(plugin);
    }

    @Override
    protected void close() {
        onClose();
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Override
    public void onInventoryClick(ClickInventoryEvent event) {
        if (event.getTargetInventory().getName().equals(inventory.getName()) && event.getTargetInventory().getViewers().contains(player)) {
            event.setCancelled(true);
            int slotIndex = -1;
            for (int i = 0; i < inventory.size(); i++)
                if (inventory.getSlot(new SlotIndex(i)).map(slot ->
                        slot.peek().filter(itemStack -> itemStack.equalTo(event.getTransactions().get(0).getOriginal().createStack()))).isPresent())
                    slotIndex = i;

            final int finalSlotIndex = slotIndex;
            if (buttons.containsKey(slotIndex))
                event.getCause().first(Player.class).ifPresent(player ->
                        buttons.get(finalSlotIndex).accept(player));
        }
    }

    @Override
    public void onInventoryClose(Close event) {
        if (event.getTargetInventory().getName().equals(inventory.getName()) && event.getTargetInventory().getViewers().contains(player))
            close();
    }

    @Override
    public void onInventoryItemDrag(Drag event) {
        event.setCancelled(event.getTargetInventory().getName().equals(inventory.getName()) && event.getTargetInventory().getViewers().contains(player));
    }

    @Override
    protected void open(J plugin) {
        inventory.forEach(slot -> slot.set(ItemStack.of(ItemTypes.NONE, 1)));
        build();
        player.openInventory(inventory, Cause.of(NamedCause.source(plugin)));
        Sponge.getEventManager().registerListeners(plugin, this);
        onOpen();
    }

    @Override
    protected void set(int slot, ItemStack itemStack, Consumer<Player> consumer) {
        set(slot, itemStack);
        buttons.put(slot, consumer);
    }

    @Override
    protected void set(int slot, ItemStack itemStack) {
        inventory.set(SlotIndex.of(slot), itemStack);
    }
}

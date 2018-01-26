package io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest;

import io.musician101.musicianlibrary.java.minecraft.gui.chest.ChestGUI;
import io.musician101.musicianlibrary.java.minecraft.gui.chest.GUIButton;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.key.MLKeys;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public final class SpongeChestGUI extends ChestGUI<Class<? extends ClickInventoryEvent>, SpongeChestGUI, Inventory, PluginContainer, Player, ItemStack> {

    public SpongeChestGUI(@Nonnull Player player, @Nonnull Text name, int size, int page, @Nonnull List<GUIButton<Class<? extends ClickInventoryEvent>, SpongeChestGUI, Inventory, PluginContainer, Player, ItemStack>> buttons, @Nullable SpongeChestGUI prevMenu, @Nonnull PluginContainer plugin, boolean manualOpen) {
        super(parseInventory(name, size, plugin), player, page, buttons, prevMenu, plugin, manualOpen);
    }

    public static SpongeChestGUIBuilder builder() {
        return new SpongeChestGUIBuilder();
    }

    private static Inventory parseInventory(@Nonnull Text name, int size, @Nonnull PluginContainer plugin) {
        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(new InventoryCapacity(size)).title(Text.of(name));
        for (int i = 0; i < size; i++) {
            builder.with(InventoryArchetype.builder().from(InventoryArchetypes.SLOT).property(new SlotIndex(i)).build("minecraft:slot" + i, "Slot"));
        }

        String plainName = TextSerializers.PLAIN.serialize(name);
        return Inventory.builder().of(builder.build(plugin.getId() + ":" + plainName.replace("\\s", "_").toLowerCase(), plainName)).build(plugin);
    }

    @Override
    public final void close() {
        if (prevGUI == null) {
            player.closeInventory();
        }
        else {
            prevGUI.open();
        }
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
            buttons.stream().filter(button -> button.getSlot() == slot).findFirst().flatMap(button -> button.getAction(event.getClass())).ifPresent(consumer -> consumer.accept(this, player));
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
        Task.builder().execute(() -> {
            inventory.clear();
            buttons.forEach(button -> inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(new SlotIndex(button.getSlot()))).first().set(button.getItemStack()));
            player.openInventory(inventory);
            Sponge.getEventManager().registerListeners(plugin, this);
        }).delayTicks(1L).submit(plugin);
    }
}

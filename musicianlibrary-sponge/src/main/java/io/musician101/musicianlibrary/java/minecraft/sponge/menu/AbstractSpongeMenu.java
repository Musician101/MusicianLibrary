package io.musician101.musicianlibrary.java.minecraft.sponge.menu;

import io.musician101.musicianlibrary.java.minecraft.AbstractMenu;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent.Close;
import org.spongepowered.api.event.network.ClientConnectionEvent.Disconnect;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.type.OrderedInventory;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

public class AbstractSpongeMenu extends AbstractMenu<Close, OrderedInventory, SpongeClickEventHandler, ClickInventoryEvent, Disconnect, ItemStack> {
    private final PluginContainer plugin;

    protected AbstractSpongeMenu(PluginContainer plugin, OrderedInventory inv, SpongeClickEventHandler handler) {
        super(inv, handler);
        this.plugin = plugin;
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    protected void destroy() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    @Override
    public void onClick(ClickInventoryEvent event)
    {
        Optional<Player> playerOptional = event.getCause().first(Player.class);
        if (!playerOptional.isPresent())
            return;

        if (!inv.equals(event.getTargetInventory()))
            return;

        if (event.getTransactions().isEmpty())
            return;

        event.setCancelled(true);
        Player player = playerOptional.get();
        ItemStack itemStack = event.getCursorTransaction().getOriginal().createStack();
        if (itemStack.getItem() == BlockTypes.AIR)
            return;

        int slot = -1;
        OrderedInventory inv = (OrderedInventory) event.getTargetInventory();
        for (int x = 0; x < inv.size(); x++) {
            Optional<Slot> slotOptional = inv.getSlot(new SlotIndex(x));
            if (slotOptional.isPresent()) {
                Optional<ItemStack> itemStackOptional = slotOptional.get().peek();
                if (itemStackOptional.isPresent() && itemStack.equals(itemStackOptional.get()))
                    slot = x;
            }
        }

        SpongeClickEvent clickEvent = new SpongeClickEvent(player, itemStack, slot);
        handler.handle(clickEvent);
        if (clickEvent.willClose())
            player.closeInventory(Cause.of(NamedCause.source(plugin)));

        if (clickEvent.willDestroy())
            destroy();
    }

    @Listener
    @Override
    public void onClose(Close event) {
        Optional<Player> playerOptional = event.getCause().first(Player.class);
        if (!playerOptional.isPresent())
            return;

        OrderedInventory inv = (OrderedInventory) event.getTargetInventory();
        if (inv.equals(this.inv))
            destroy();
    }

    @Listener
    @Override
    public void onQuit(Disconnect event) {
        Optional<Container> invOptional = event.getTargetEntity().getOpenInventory();
        if (!invOptional.isPresent())
            return;

        if (inv.equals(invOptional.get()))
            destroy();
    }

    @Override
    public void open(UUID uuid) {
        Sponge.getServer().getPlayer(uuid).ifPresent(player1 -> player1.openInventory(inv, Cause.of(NamedCause.source(plugin))));
    }

    @Override
    public void setOption(int slot, ItemStack itemStack) {
        setOption(slot, itemStack, " ");
    }

    @Override
    public void setOption(int slot, ItemStack itemStack, String name) {
        setOption(slot, itemStack, name, new String[0]);
    }

    @Override
    public void setOption(int slot, ItemStack itemStack, String name, String... description) {
        setOption(slot, itemStack, name, false, description);
    }

    @Override
    public void setOption(int slot, ItemStack itemStack, String name, boolean willGlow, String... description) {
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(name));
        itemStack.offer(Keys.ITEM_LORE, Arrays.stream(description).map(Text::of).collect(Collectors.toList()));
        if (willGlow) {
            itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(new ItemEnchantment(Enchantments.AQUA_AFFINITY, 1)));
            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        inv.set(new SlotIndex(slot), itemStack);
    }
}

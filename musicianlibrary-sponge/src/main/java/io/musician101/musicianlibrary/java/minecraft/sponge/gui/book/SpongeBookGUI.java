package io.musician101.musicianlibrary.java.minecraft.sponge.gui.book;

import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.minecraft.common.gui.book.BookGUI;
import io.musician101.musicianlibrary.java.minecraft.sponge.event.AffectBookEvent;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.container.ClickContainerEvent;
import org.spongepowered.api.event.lifecycle.RegisterDataEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.plugin.PluginContainer;

/**
 * @deprecated nonfunctional atm due to mixins not working properly for me
 */
@Deprecated
public class SpongeBookGUI extends BookGUI<PluginContainer, ServerPlayer, Component> {

    public SpongeBookGUI(@Nonnull PluginContainer plugin, @Nonnull ServerPlayer player, @Nonnull ItemStack book, @Nonnull BiConsumer<ServerPlayer, List<Component>> action) {
        super(plugin, player, player.getInventory().getHotbar().getSelectedSlotIndex(), action);
        book.offer(getBookUUIDKey(plugin), player.getUniqueId());
        player.getInventory().getHotbar().set(bookSlot, book);
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    private static Key<Value<UUID>> getBookUUIDKey(@Nonnull PluginContainer plugin) {
        return Key.of(plugin, "uuid", new TypeToken<Value<UUID>>() {

        });
    }

    public static void init(@Nonnull PluginContainer plugin, @Nonnull RegisterDataEvent event) {
        event.register(DataRegistration.of(getBookUUIDKey(plugin), ItemStack.class));
    }

    public static boolean isEditing(@Nonnull PluginContainer plugin, @Nonnull ServerPlayer player) {
        return player.getInventory().slots().stream().map(Inventory::peek).anyMatch(itemStack -> itemStack.get(getBookUUIDKey(plugin)).filter(uuid -> uuid.equals(player.getUniqueId())).isPresent());
    }

    @Listener
    public void clickBook(ClickContainerEvent event, @Getter("getCursorTransaction") Transaction<ItemStackSnapshot> transaction, @First ServerPlayer player) {
        event.setCancelled(isEditing(plugin, player) && transaction.getFinal().createStack().get(getBookUUIDKey(plugin)).isPresent());
    }

    @Listener
    public void editBook(AffectBookEvent event, @Getter("getTransaction") Transaction<ItemStackSnapshot> transaction, @Getter("getPlayer") ServerPlayer player) {
        ItemStack finalStack = transaction.getFinal().createStack();
        if (finalStack.get(getBookUUIDKey(plugin)).isPresent()) {
            if (isEditing(plugin, player)) {
                action.accept(player, finalStack.get(Keys.PAGES).orElse(Collections.emptyList()));
                remove();
            }
        }
    }

    @Listener
    public void playerQuit(ServerSideConnectionEvent.Disconnect event, @First ServerPlayer player) {
        if (isEditing(plugin, player)) {
            remove();
        }
    }

    @Override
    protected void remove() {
        player.getInventory().getHotbar().set(bookSlot, ItemStack.empty());
        Sponge.getEventManager().unregisterListeners(this);
    }
}

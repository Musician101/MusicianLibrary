package io.musician101.musicianlibrary.java.minecraft.sponge.gui;

import io.musician101.musicianlibrary.java.minecraft.sponge.SpongeMusicianLibrary;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.key.MLKeys;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.mutable.UUIDData;
import io.musician101.musicianlibrary.java.minecraft.sponge.event.AffectBookEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;

public class SpongeBookGUIManager {

    private final Map<UUID, Consumer<List<Text>>> consumers = new HashMap<>();

    public SpongeBookGUIManager() {
        Sponge.getEventManager().registerListeners(SpongeMusicianLibrary.instance(), this);
    }

    public void addPlayer(@Nonnull Player player, @Nonnull ItemStack book, @Nonnull Consumer<List<Text>> consumer) {
        UUID uuid = player.getUniqueId();
        book.offer(MLKeys.UUID, uuid);
        book.offer(MLKeys.SLOT, player.getInventory().<Hotbar>query(Hotbar.class).getSelectedSlotIndex());
        player.setItemInHand(HandTypes.MAIN_HAND, book);
        consumers.put(uuid, consumer);
    }

    @Listener
    public void clickBook(ClickInventoryEvent event, @Getter("getCursorTransaction") Transaction<ItemStackSnapshot> transaction, @First Player player) {
        event.setCancelled(isEditing(player) && transaction.getFinal().createStack().get(UUIDData.class).isPresent());
    }

    @Listener
    public void editBook(AffectBookEvent event, @Getter("getTransaction") Transaction<ItemStackSnapshot> transaction, @Getter("getTargetEntity") Player player) {
        ItemStack finalStack = transaction.getFinal().createStack();
        if (finalStack.get(UUIDData.class).isPresent()) {
            if (isEditing(player)) {
                consumers.get(player.getUniqueId()).accept(finalStack.get(Keys.BOOK_PAGES).orElse(Collections.emptyList()));
                remove(player);
            }
        }
    }

    public boolean isEditing(@Nonnull Player player) {
        if (consumers.containsKey(player.getUniqueId())) {
            return !StreamSupport.stream(player.getInventory().query(ItemTypes.WRITTEN_BOOK, ItemTypes.WRITABLE_BOOK).spliterator(), false).map(Inventory::peek).map(Optional::get).filter(itemStack -> itemStack.get(MLKeys.UUID).filter(uuid -> uuid.equals(player.getUniqueId())).isPresent()).collect(Collectors.toList()).isEmpty();
        }

        consumers.remove(player.getUniqueId());
        return false;
    }

    @Listener
    public void playerQuit(ClientConnectionEvent.Disconnect event, @First Player player) {
        if (isEditing(player)) {
            remove(player);
        }
    }

    public void remove(Player player) {
        Hotbar inv = player.getInventory().query(Hotbar.class);
        StreamSupport.stream(inv.query(ItemTypes.WRITTEN_BOOK, ItemTypes.WRITABLE_BOOK).spliterator(), false).map(Inventory::peek).map(Optional::get).filter(itemStack -> itemStack.get(UUIDData.class).isPresent()).map(itemStack -> itemStack.get(MLKeys.SLOT)).findFirst().ifPresent(optional -> optional.ifPresent(slot -> inv.set(new SlotIndex(slot), ItemStack.empty())));
        consumers.remove(player.getUniqueId());
    }
}

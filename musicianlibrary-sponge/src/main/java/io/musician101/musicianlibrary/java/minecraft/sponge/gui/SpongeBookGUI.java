package io.musician101.musicianlibrary.java.minecraft.sponge.gui;

import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.mutable.UUIDData;
import io.musician101.musicianlibrary.java.minecraft.sponge.event.AffectBookEvent;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class SpongeBookGUI {

    private static final String LORE_IDENTIFIER = "\\_o<";
    private static final Predicate<ItemStack> BOOK_FILTER = itemStack -> {
        ItemType itemType = itemStack.getType();
        return (itemType == ItemTypes.WRITABLE_BOOK || itemType == ItemTypes.WRITTEN_BOOK) && itemStack.get(Keys.ITEM_LORE).map(lore -> lore.stream().map(TextSerializers.PLAIN::serialize).collect(Collectors.toList())).filter(lore -> lore.contains(LORE_IDENTIFIER)).isPresent();

    };
    private final BiConsumer<Player, List<Text>> action;
    private final int bookSlot;
    private final Player player;

    public SpongeBookGUI(Object plugin, Player player, ItemStack book, BiConsumer<Player, List<Text>> action) {
        this.player = player;
        Hotbar hotbar = player.getInventory().query(QueryOperationTypes.TYPE.of(Hotbar.class));
        this.bookSlot = hotbar.getSelectedSlotIndex();
        this.action = action;
        List<Text> lore = book.get(Keys.ITEM_LORE).orElse(Collections.singletonList(Text.of(LORE_IDENTIFIER)));
        book.offer(Keys.ITEM_LORE, lore);
        hotbar.set(SlotIndex.of(bookSlot), book);
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    public static boolean isEditing(Player player) {
        return StreamSupport.stream(player.getInventory().slots().spliterator(), false).map(Inventory::peek).filter(Optional::isPresent).map(Optional::get).anyMatch(BOOK_FILTER);
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
                action.accept(player, finalStack.get(Keys.BOOK_PAGES).orElse(Collections.emptyList()));
                remove();
            }
        }
    }

    @Listener
    public void playerQuit(ClientConnectionEvent.Disconnect event, @First Player player) {
        if (isEditing(player)) {
            remove();
        }
    }

    private void remove() {
        player.getInventory().<Hotbar>query(QueryOperationTypes.TYPE.of(Hotbar.class)).set(SlotIndex.of(bookSlot), ItemStack.empty());
        Sponge.getEventManager().unregisterListeners(this);
    }
}

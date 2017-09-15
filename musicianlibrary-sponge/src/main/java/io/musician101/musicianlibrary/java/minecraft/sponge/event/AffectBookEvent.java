package io.musician101.musicianlibrary.java.minecraft.sponge.event;

import javax.annotation.Nonnull;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class AffectBookEvent extends AbstractEvent implements TargetPlayerEvent, Cancellable {

    private final Cause cause;
    private final Player player;
    private final Transaction<ItemStackSnapshot> transaction;
    private boolean cancelled = false;

    public AffectBookEvent(Player player, Transaction<ItemStackSnapshot> transaction, Cause cause) {
        this.player = player;
        this.transaction = transaction;
        this.cause = cause;
    }

    @Nonnull
    @Override
    public Cause getCause() {
        return cause;
    }

    @Nonnull
    @Override
    public Player getTargetEntity() {
        return player;
    }

    @Nonnull
    public Transaction<ItemStackSnapshot> getTransaction() {
        return transaction;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static class EditBookEvent extends AffectBookEvent {

        public EditBookEvent(Player player, Transaction<ItemStackSnapshot> transaction, Cause cause) {
            super(player, transaction, cause);
        }
    }

    public static class SignBookEvent extends AffectBookEvent {

        public SignBookEvent(Player player, Transaction<ItemStackSnapshot> transaction, Cause cause) {
            super(player, transaction, cause);
        }
    }
}

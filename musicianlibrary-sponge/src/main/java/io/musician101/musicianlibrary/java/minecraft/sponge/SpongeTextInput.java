package io.musician101.musicianlibrary.java.minecraft.sponge;

import io.musician101.musicianlibrary.java.minecraft.common.TextInput;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public abstract class SpongeTextInput extends TextInput<Player> {

    private static final List<UUID> PLAYERS = new ArrayList<>();

    public SpongeTextInput(@Nonnull Object plugin, @Nonnull Player player) {
        super(player);
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    public static boolean isWaitingForInput(Player player) {
        return PLAYERS.contains(player.getUniqueId());
    }

    @Listener
    public final void onPlayerChat(MessageChannelEvent.Chat event, @First Player player) {
        if (player.getUniqueId().equals(this.player.getUniqueId())) {
            event.setCancelled(true);
            process(player, event.getRawMessage().toPlain());
            cancel();
        }
    }

    @Listener
    public final void onQuit(ClientConnectionEvent.Disconnect event) {
        Player player = event.getTargetEntity();
        if (player.getUniqueId().equals(this.player.getUniqueId())) {
            cancel();
        }
    }

    @Override
    protected void cancel() {
        Sponge.getEventManager().unregisterListeners(this);
        PLAYERS.remove(player.getUniqueId());
    }
}

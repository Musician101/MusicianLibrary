package io.musician101.musicianlibrary.java.minecraft.sponge;

import io.musician101.musicianlibrary.java.minecraft.common.TextInput;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.PlayerChatEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.plugin.PluginContainer;

public abstract class SpongeTextInput extends TextInput<Player> {

    private static final List<UUID> PLAYERS = new ArrayList<>();

    public SpongeTextInput(@Nonnull PluginContainer plugin, @Nonnull Player player) {
        super(player);
        if (PLAYERS.contains(player.getUniqueId())) {
            player.sendMessage(Component.text().content("An error has occurred. Something is already waiting for your input.").color(NamedTextColor.RED));
            return;
        }
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    public static boolean isWaitingForInput(Player player) {
        return PLAYERS.contains(player.getUniqueId());
    }

    @Override
    protected void cancel() {
        Sponge.getEventManager().unregisterListeners(this);
        PLAYERS.remove(player.getUniqueId());
    }

    @Listener
    public final void onPlayerChat(PlayerChatEvent event, @First Player player) {
        if (player.getUniqueId().equals(this.player.getUniqueId())) {
            event.setCancelled(true);
            process(player, PlainComponentSerializer.plain().serialize(event.getOriginalMessage()));
            cancel();
        }
    }

    @Listener
    public final void onQuit(ServerSideConnectionEvent.Disconnect event, @First Player player) {
        if (player.getUniqueId().equals(this.player.getUniqueId())) {
            cancel();
        }
    }
}

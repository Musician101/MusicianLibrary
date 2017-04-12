package io.musician101.musicianlibrary.java.minecraft.sponge.textinput;

import io.musician101.musicianlibrary.java.minecraft.sponge.gui.AbstractSpongeChestGUI;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;

public abstract class TextInput<I> {

    @Nonnull
    protected final Player player;
    @Nullable
    protected final AbstractSpongeChestGUI<I> prevMenu;
    @Nonnull
    protected final BiFunction<String, Player, Boolean> biFunction;

    public TextInput(@Nonnull I plugin, @Nonnull Player player, @Nullable AbstractSpongeChestGUI<I> prevMenu, @Nonnull BiFunction<String, Player, Boolean> biFunction) {
        this.player = player;
        this.prevMenu = prevMenu;
        this.biFunction = biFunction;
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    private void end() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onChat(MessageChannelEvent.Chat event, @First Player player) {
        if (player.getUniqueId().equals(this.player.getUniqueId())) {
            event.setCancelled(true);
            if (biFunction.apply(event.getRawMessage().toPlain(), player)) {
                end();
            }
        }
    }
}

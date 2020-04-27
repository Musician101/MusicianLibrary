package io.musician101.musicianlibrary.java.minecraft.spigot;

import io.musician101.musicianlibrary.java.minecraft.common.TextInput;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SpigotTextInput extends TextInput<Player> implements Listener {

    public SpigotTextInput(@Nonnull JavaPlugin plugin, @Nonnull Player player) {
        super(player);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static boolean isWaitingForInput(Player player) {
        return PLAYERS.contains(player.getUniqueId());
    }

    public void cancel() {
        HandlerList.unregisterAll(this);
        PLAYERS.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.getUniqueId().equals(this.player.getUniqueId())) {
            event.setCancelled(true);
            process(player, event.getMessage());
            cancel();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.getUniqueId().equals(this.player.getUniqueId())) {
            cancel();
        }
    }
}

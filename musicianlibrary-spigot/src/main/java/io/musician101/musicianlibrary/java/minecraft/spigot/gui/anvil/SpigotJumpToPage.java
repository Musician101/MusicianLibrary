package io.musician101.musicianlibrary.java.minecraft.spigot.gui.anvil;

import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotJumpToPage<J extends JavaPlugin> extends AbstractAnvilGUI<J> {

    public SpigotJumpToPage(@Nonnull J plugin, @Nonnull Player player, int maxPage, @Nonnull BiConsumer<Player, Integer> biConsumer) {
        super(plugin, player, (p, name) -> {
            int page;
            try {
                page = Integer.parseInt(name);
            }
            catch (NumberFormatException e) {
                return "That is not a number!";
            }

            if (page > maxPage) {
                return "Page cannot exceed " + maxPage;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> biConsumer.accept(player, page));
            return null;
        });
    }
}

package io.musician101.musicianlibrary.java.minecraft.spigot.gui.anvil;

import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.AbstractSpigotChestGUI;
import io.musician101.musicianlibrary.java.util.TriConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JumpToPage<J extends JavaPlugin> extends AbstractAnvilGUI<J> {

    public JumpToPage(@Nonnull J plugin, @Nonnull Player player, int maxPage, @Nullable AbstractSpigotChestGUI<J> prevMenu, @Nonnull TriConsumer<Player, Integer, AbstractSpigotChestGUI<J>> triConsumer) {
        super(plugin, player, prevMenu, (p, name) -> {
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

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> triConsumer.accept(player, page, prevMenu));
            return null;
        });
    }
}

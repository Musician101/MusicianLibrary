package io.musician101.musicianlibrary.java.minecraft.sponge.gui.anvil;

import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

public class SpongeJumpToPage<P extends PluginContainer> extends SpongeAnvilGUI {

    public SpongeJumpToPage(@Nonnull P plugin, @Nonnull Player player, int maxPage, @Nonnull BiConsumer<Player, Integer> biConsumer) {
        super(player, (p, name) -> {
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

            Task.builder().delayTicks(1L).execute(() -> biConsumer.accept(player, page)).submit(plugin.getInstance().get());
            return null;
        });
    }
}

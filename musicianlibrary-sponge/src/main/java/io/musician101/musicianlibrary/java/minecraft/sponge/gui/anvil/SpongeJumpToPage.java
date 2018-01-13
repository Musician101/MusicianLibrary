package io.musician101.musicianlibrary.java.minecraft.sponge.gui.anvil;

import java.util.Optional;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

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

            Optional instance = plugin.getInstance();
            if (instance.isPresent()) {
                Task.builder().delayTicks(1L).execute(() -> biConsumer.accept(player, page)).submit(instance.get());
            }
            else {
                player.closeInventory();
                player.sendMessage(Text.builder(plugin.getId() + " does not have a valid instance. This GUI will not work until it does.").color(TextColors.RED).build());
            }

            return null;
        });
    }
}

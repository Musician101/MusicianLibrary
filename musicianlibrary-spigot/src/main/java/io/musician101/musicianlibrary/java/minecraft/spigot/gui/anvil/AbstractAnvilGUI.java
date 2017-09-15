package io.musician101.musicianlibrary.java.minecraft.spigot.gui.anvil;

import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractAnvilGUI<J extends JavaPlugin> extends AnvilGUI {

    public AbstractAnvilGUI(@Nonnull J plugin, @Nonnull Player holder, @Nonnull BiFunction<Player, String, String> biFunction) {
        super(plugin, holder, "Rename Me", biFunction);
    }
}

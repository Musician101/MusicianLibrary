package io.musician101.musicianlibrary.java.minecraft.spigot.gui.anvil;

import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.AbstractSpigotChestGUI;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractAnvilGUI<J extends JavaPlugin> extends AnvilGUI {

    @Nullable
    protected final AbstractSpigotChestGUI<? extends JavaPlugin> prevMenu;

    public <P extends JavaPlugin> AbstractAnvilGUI(@Nonnull J plugin, @Nonnull Player holder, @Nullable AbstractSpigotChestGUI<P> prevMenu, @Nonnull BiFunction<Player, String, String> biFunction) {
        super(plugin, holder, "Rename Me", biFunction);
        this.prevMenu = prevMenu;
    }
}

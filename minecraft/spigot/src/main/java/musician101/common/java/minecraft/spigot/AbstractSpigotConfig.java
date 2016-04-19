package musician101.common.java.minecraft.spigot;

import musician101.common.java.minecraft.config.AbstractConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class AbstractSpigotConfig<Plugin extends AbstractSpigotPlugin> extends AbstractConfig
{
    protected boolean updateCheck;
    protected final Plugin plugin;

    protected AbstractSpigotConfig(Plugin plugin)
    {
        super(new File(plugin.getDataFolder(), "config.yml"));
        this.plugin = plugin;
    }

    public boolean isUpdateCheckEnabled()
    {
        return updateCheck;
    }
}

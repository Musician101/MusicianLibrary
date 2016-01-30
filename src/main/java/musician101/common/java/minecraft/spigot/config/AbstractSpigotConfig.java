package musician101.common.java.minecraft.spigot.config;

import musician101.common.java.minecraft.config.AbstractConfig;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractSpigotConfig<Plugin extends JavaPlugin> extends AbstractConfig
{
    Plugin plugin;

    protected AbstractSpigotConfig(Plugin plugin)
    {
        this.plugin = plugin;
    }
}

package musician101.common.java.minecraft.spigot.config;

import musician101.common.java.minecraft.config.AbstractConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class AbstractSpigotConfig<Plugin extends JavaPlugin> extends AbstractConfig
{
    protected final Plugin plugin;

    protected AbstractSpigotConfig(Plugin plugin)
    {
        super(new File(plugin.getDataFolder(), "config.yml"));
        this.plugin = plugin;
    }
}

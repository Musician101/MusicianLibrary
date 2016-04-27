package io.musician101.common.java.minecraft.spigot;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractListener<Plugin extends JavaPlugin> implements Listener
{
    protected final Plugin plugin;

    protected AbstractListener(Plugin plugin)
    {
        this.plugin = plugin;
    }
}

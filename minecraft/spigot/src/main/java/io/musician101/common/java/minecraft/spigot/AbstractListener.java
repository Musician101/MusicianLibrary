package io.musician101.common.java.minecraft.spigot;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractListener<P extends JavaPlugin> implements Listener
{
    protected final P plugin;

    protected AbstractListener(P plugin)
    {
        this.plugin = plugin;
    }
}

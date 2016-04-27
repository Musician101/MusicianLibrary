package io.musician101.common.java.minecraft.spigot;

import io.musician101.common.java.minecraft.AbstractConfig;

import java.io.File;

public abstract class AbstractSpigotConfig<P extends AbstractSpigotPlugin> extends AbstractConfig<P>
{
    protected boolean updateCheck;

    protected AbstractSpigotConfig(P plugin)
    {
        super(plugin, new File(plugin.getDataFolder(), "config.yml"));
    }

    public boolean isUpdateCheckEnabled()
    {
        return updateCheck;
    }
}

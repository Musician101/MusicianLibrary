package io.musician101.common.java.minecraft;

import java.io.File;

public abstract class AbstractConfig<P>
{
    protected final P plugin;
    protected final File configFile;

    protected AbstractConfig(P plugin, File configFile)
    {
        this.plugin = plugin;
        this.configFile = configFile;
    }

    public abstract void reload();
}

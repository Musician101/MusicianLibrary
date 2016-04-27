package io.musician101.common.java.minecraft;

import java.io.File;

public abstract class AbstractConfig
{
    protected final File configFile;

    protected AbstractConfig(File configFile)
    {
        this.configFile = configFile;
    }

    public abstract void reload();
}

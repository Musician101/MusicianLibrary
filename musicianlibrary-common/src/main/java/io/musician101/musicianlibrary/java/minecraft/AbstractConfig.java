package io.musician101.musicianlibrary.java.minecraft;

import java.io.File;


public abstract class AbstractConfig
{
    protected boolean updateCheck;
    protected final File configFile;

    protected AbstractConfig(File configFile)
    {
        this.configFile = configFile;
    }

    public boolean isUpdateCheckEnabled()
    {
        return updateCheck;
    }


    public abstract void reload();
}

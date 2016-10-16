package io.musician101.musicianlibrary.java.minecraft;

import java.io.File;

@SuppressWarnings({"WeakerAccess", "unused"})
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

    @SuppressWarnings("unused")
    public abstract void reload();
}

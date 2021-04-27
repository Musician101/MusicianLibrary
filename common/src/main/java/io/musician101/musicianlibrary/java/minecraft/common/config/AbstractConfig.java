package io.musician101.musicianlibrary.java.minecraft.common.config;

import java.io.File;

public abstract class AbstractConfig {

    protected final File configFile;
    protected boolean updateCheck;

    protected AbstractConfig(File configFile) {
        this.configFile = configFile;
    }

    public boolean isUpdateCheckEnabled() {
        return updateCheck;
    }

    public abstract void reload();
}

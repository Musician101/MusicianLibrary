package io.musician101.common.java.minecraft.sponge;

import io.musician101.common.java.minecraft.AbstractConfig;
import org.slf4j.Logger;

import java.io.File;

public abstract class AbstractSpongeConfig extends AbstractConfig
{
    protected final Logger logger;

    protected AbstractSpongeConfig(File configFile, Logger logger)
    {
        super(configFile);
        this.logger = logger;
    }
}

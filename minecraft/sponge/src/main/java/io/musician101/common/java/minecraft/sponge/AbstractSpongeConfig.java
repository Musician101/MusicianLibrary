package io.musician101.common.java.minecraft.sponge;

import io.musician101.common.java.minecraft.AbstractConfig;
import org.slf4j.Logger;

import java.io.File;

public abstract class AbstractSpongeConfig<P> extends AbstractConfig<P>
{
    protected AbstractSpongeConfig(P plugin, File configFile)
    {
        super(plugin, configFile);
    }
}

package io.musician101.musicianlibrary.java.minecraft.sponge;

import io.musician101.musicianlibrary.java.minecraft.AbstractConfig;
import org.slf4j.Logger;


public abstract class AbstractSpongePlugin<C extends AbstractConfig> {

    protected C config;

    public C getConfig() {
        return config;
    }

    public abstract Logger getLogger();
}

package io.musician101.musicianlibrary.java.minecraft.sponge.plugin;

import io.musician101.musicianlibrary.java.minecraft.config.AbstractConfig;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.PluginContainer;


public abstract class AbstractSpongePlugin<C extends AbstractConfig> {

    protected C config;

    public C getConfig() {
        return config;
    }

    public String getId() {
        return getPluginContainer().getId();
    }

    public Logger getLogger() {
        return getPluginContainer().getLogger();
    }

    public String getName() {
        return getPluginContainer().getName();
    }

    public abstract PluginContainer getPluginContainer();
}

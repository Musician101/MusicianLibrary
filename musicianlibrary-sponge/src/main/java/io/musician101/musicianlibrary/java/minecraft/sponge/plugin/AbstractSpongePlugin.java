package io.musician101.musicianlibrary.java.minecraft.sponge.plugin;

import io.musician101.musicianlibrary.java.minecraft.config.AbstractConfig;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.PluginContainer;


public abstract class AbstractSpongePlugin<C extends AbstractConfig> {

    protected C config;

    public C getConfig() {
        return config;
    }

    public abstract PluginContainer getPluginContainer();

    public String getId() {
        return getPluginContainer().getId();
    }

    public String getName() {
        return getPluginContainer().getName();
    }

    public Logger getLogger() {
        return getPluginContainer().getLogger();
    }
}

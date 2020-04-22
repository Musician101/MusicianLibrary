package io.musician101.musicianlibrary.java.minecraft.sponge.plugin;

import io.musician101.musicianlibrary.java.minecraft.common.config.AbstractConfig;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.PluginContainer;

public abstract class AbstractSpongePlugin<C extends AbstractConfig> {

    protected C config;

    @Nonnull
    public C getConfig() {
        return config;
    }

    @Nonnull
    public String getId() {
        return getPluginContainer().getId();
    }

    @Nonnull
    public Logger getLogger() {
        return getPluginContainer().getLogger();
    }

    @Nonnull
    public String getName() {
        return getPluginContainer().getName();
    }

    @Nonnull
    public abstract PluginContainer getPluginContainer();
}

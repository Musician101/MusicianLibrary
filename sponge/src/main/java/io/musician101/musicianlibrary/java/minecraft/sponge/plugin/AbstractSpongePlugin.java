package io.musician101.musicianlibrary.java.minecraft.sponge.plugin;

import io.musician101.musicianlibrary.java.minecraft.common.config.AbstractConfig;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.Logger;
import org.spongepowered.plugin.PluginContainer;

public abstract class AbstractSpongePlugin<C extends AbstractConfig> {

    protected C config;

    @Nonnull
    public C getConfig() {
        return config;
    }

    @Nonnull
    public String getId() {
        return getPluginContainer().getMetadata().getId();
    }

    @Nonnull
    public Logger getLogger() {
        return getPluginContainer().getLogger();
    }

    @Nonnull
    public Optional<String> getName() {
        return getPluginContainer().getMetadata().getName();
    }

    @Nonnull
    public abstract PluginContainer getPluginContainer();
}

package io.musician101.musicianlibrary.java.minecraft.sponge;

import io.musician101.musicianlibrary.java.minecraft.AbstractConfig;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;


public abstract class AbstractSpongePlugin<C extends AbstractConfig>
{
    protected C config;

    public abstract void preInit(GamePreInitializationEvent event);

    public C getConfig()
    {
        return config;
    }

    public abstract Logger getLogger();
}

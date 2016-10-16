package io.musician101.musicianlibrary.java.minecraft.sponge;

import io.musician101.musicianlibrary.java.minecraft.AbstractConfig;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AbstractSpongePlugin<C extends AbstractConfig>
{
    @SuppressWarnings("CanBeFinal")
    protected C config;

    @SuppressWarnings("EmptyMethod")
    public abstract void preInit(GamePreInitializationEvent event);

    public C getConfig()
    {
        return config;
    }

    public abstract Logger getLogger();
}

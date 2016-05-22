package io.musician101.common.java.minecraft.sponge;

import io.musician101.common.java.minecraft.AbstractConfig;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AbstractSpongePlugin<C extends AbstractConfig>
{
    protected C config;

    public abstract void onServerStart(GameStartedServerEvent event);

    public C getConfig()
    {
        return config;
    }
}

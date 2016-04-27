package io.musician101.common.java.minecraft.sponge;

import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;

public abstract class AbstractSpongePlugin<Config extends AbstractSpongeConfig>
{
    public Config config;
    public Logger logger;

    public abstract void onServerStart(GameStartedServerEvent event);

    public Config getConfig()
    {
        return config;
    }

    public Logger getLogger()
    {
        return logger;
    }
}

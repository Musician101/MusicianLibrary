package musician101.common.java.minecraft.sponge;

import musician101.common.java.minecraft.config.AbstractConfig;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;

import java.io.File;

public abstract class AbstractSpongeConfig extends AbstractConfig
{
    protected final Logger logger;

    protected AbstractSpongeConfig(File configFile, Logger logger)
    {
        super(configFile);
        this.logger = logger;
    }
}

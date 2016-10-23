package io.musician101.musicianlibrary.java.minecraft.sponge;

import io.musician101.musicianlibrary.java.minecraft.AbstractConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@SuppressWarnings("WeakerAccess")
@Plugin(id = SpongeMusicianLibrary.ID, name = "Musician Library - Sponge", authors = {"Musician101"}, version = "3.0-SNAPSHOT", description = "A library used to house common classes across multiple projects.")
public class SpongeMusicianLibrary extends AbstractSpongePlugin<AbstractConfig>
{
    static final String ID = "sponge_musician_library";

    @Override
    public void preInit(GamePreInitializationEvent event)//NOSONAR
    {

    }

    @Override
    public Logger getLogger()
    {
        return LoggerFactory.getLogger(ID);
    }
}

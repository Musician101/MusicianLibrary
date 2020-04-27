package io.musician101.musicianlibrary.java.minecraft.sponge;

import com.google.inject.Inject;
import io.musician101.musicianlibrary.java.minecraft.common.config.AbstractConfig;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.builder.UUIDDataBuilder;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.immutable.ImmutableUUIDData;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.mutable.UUIDData;
import io.musician101.musicianlibrary.java.minecraft.sponge.plugin.AbstractSpongePlugin;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(id = "sponge_musician_library", name = "Musician Library - Sponge", authors = {"Musician101"}, version = "3.0-SNAPSHOT", description = "A library used to house common classes across multiple projects.")
public class SpongeMusicianLibrary extends AbstractSpongePlugin<AbstractConfig> {

    @Inject
    private PluginContainer pluginContainer;

    public static Optional<SpongeMusicianLibrary> instance() {
        return Sponge.getPluginManager().getPlugin("sponge_musician_library").flatMap(PluginContainer::getInstance).filter(SpongeMusicianLibrary.class::isInstance).map(SpongeMusicianLibrary.class::cast);
    }

    @Nonnull
    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        DataRegistration.builder().name("BookGUIData").id("book_gui").dataClass(UUIDData.class).immutableClass(ImmutableUUIDData.class).builder(new UUIDDataBuilder()).build();
    }
}

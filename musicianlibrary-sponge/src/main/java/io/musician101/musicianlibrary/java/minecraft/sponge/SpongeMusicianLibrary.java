package io.musician101.musicianlibrary.java.minecraft.sponge;

import com.google.inject.Inject;
import io.musician101.musicianlibrary.java.minecraft.config.AbstractConfig;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.builder.UUIDDataBuilder;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.builder.InventorySlotDataBuilder;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.immutable.ImmutableUUIDData;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.immutable.ImmutableInventorySlotData;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.mutable.UUIDData;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.mutable.InventorySlotData;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.SpongeBookGUIManager;
import io.musician101.musicianlibrary.java.minecraft.sponge.plugin.AbstractSpongePlugin;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(id = "sponge_musician_library", name = "Musician Library - Sponge", authors = {"Musician101"}, version = "3.0-SNAPSHOT", description = "A library used to house common classes across multiple projects.")
public class SpongeMusicianLibrary extends AbstractSpongePlugin<AbstractConfig> {
    private static SpongeMusicianLibrary instance;

    @Inject
    private PluginContainer pluginContainer;

    private SpongeBookGUIManager spongeBookGUIManager;

    public SpongeBookGUIManager getSpongeBookGUIManager() {
        return spongeBookGUIManager;
    }

    @Listener
    public void onConstruct(GameConstructionEvent event) {
        instance = this;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        spongeBookGUIManager = new SpongeBookGUIManager();
        registerData("InventorySlot", getId() + ":inventory_slot", InventorySlotData.class, ImmutableInventorySlotData.class, new InventorySlotDataBuilder());
        registerData("BookGUIData", getId() + ":book_gui", UUIDData.class, ImmutableUUIDData.class, new UUIDDataBuilder());
    }

    private <D extends DataManipulator<D, M>, M extends ImmutableDataManipulator<M, D>, T extends DataManipulator<T, I>, I extends ImmutableDataManipulator<I, T>> void registerData(String dataName, String id, Class<D> manipulatorClass, Class<M> immutableDataClass, DataManipulatorBuilder<T, I> builder) {
        DataRegistration.builder().dataClass(manipulatorClass).dataName(dataName).manipulatorId(id).immutableClass(immutableDataClass).buildAndRegister(pluginContainer);
    }

    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    public static SpongeMusicianLibrary instance() {
        return instance;
    }
}

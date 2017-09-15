package io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.builder;

import io.musician101.musicianlibrary.java.minecraft.sponge.data.key.MLKeys;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.immutable.ImmutableInventorySlotData;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.mutable.InventorySlotData;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

public class InventorySlotDataBuilder implements DataManipulatorBuilder<InventorySlotData, ImmutableInventorySlotData> {

    private int slot;

    @Nonnull
    @Override
    public Optional<InventorySlotData> build(@Nonnull DataView container) throws InvalidDataException {
        container.getInt(MLKeys.SLOT.getQuery()).ifPresent(this::setSlot);
        return Optional.of(create());
    }

    @Nonnull
    @Override
    public InventorySlotData create() {
        return new InventorySlotData(slot);
    }

    @Nonnull
    @Override
    public Optional<InventorySlotData> createFrom(@Nonnull DataHolder dataHolder) {
        dataHolder.get(MLKeys.SLOT).ifPresent(this::setSlot);
        return Optional.of(create());
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}

package io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.mutable;

import io.musician101.musicianlibrary.java.minecraft.sponge.data.key.MLKeys;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.immutable.ImmutableUUIDData;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

public class UUIDData extends AbstractSingleData<UUID, UUIDData, ImmutableUUIDData> {

    public UUIDData(@Nonnull UUID uuid) {
        super(uuid, MLKeys.UUID);
    }

    @Nonnull
    @Override
    public ImmutableUUIDData asImmutable() {
        return new ImmutableUUIDData(getValue());
    }

    @Nonnull
    @Override
    public UUIDData copy() {
        return new UUIDData(getValue());
    }

    @Nonnull
    @Override
    public Optional<UUIDData> fill(@Nonnull DataHolder dataHolder, @Nonnull MergeFunction overlap) {
        dataHolder.get(UUIDData.class).ifPresent(bookGUIData -> overlap.merge(this, bookGUIData));
        return Optional.of(this);
    }

    @Nonnull
    @Override
    public Optional<UUIDData> from(@Nonnull DataContainer container) {
        container.getString(MLKeys.UUID.getQuery()).map(UUID::fromString).ifPresent(this::setValue);
        return Optional.of(this);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    protected Value<UUID> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue((Key<Value<UUID>>) usedKey, getValue());
    }

    @Nonnull
    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(MLKeys.UUID, getValue());
    }
}

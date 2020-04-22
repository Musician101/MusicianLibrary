package io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.immutable;

import io.musician101.musicianlibrary.java.minecraft.sponge.data.key.MLKeys;
import io.musician101.musicianlibrary.java.minecraft.sponge.data.manipulator.mutable.UUIDData;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

public class ImmutableUUIDData extends AbstractImmutableSingleData<UUID, ImmutableUUIDData, UUIDData> {

    public ImmutableUUIDData(@Nonnull UUID uuid) {
        super(MLKeys.UUID, uuid);
    }

    @Nonnull
    @Override
    public UUIDData asMutable() {
        return new UUIDData(getValue());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    protected ImmutableValue<UUID> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue((Key<Value<UUID>>) usedKey, getValue()).asImmutable();
    }
}

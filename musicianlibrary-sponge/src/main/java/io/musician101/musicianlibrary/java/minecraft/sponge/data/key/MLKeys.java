package io.musician101.musicianlibrary.java.minecraft.sponge.data.key;

import io.musician101.musicianlibrary.java.minecraft.sponge.SpongeMusicianLibrary;
import java.util.UUID;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public final class MLKeys {

    public static final Key<Value<Integer>> SLOT = KeyFactory.makeSingleKey(TypeTokens.INTEGER_TOKEN, TypeTokens.INTEGER_VALUE_TOKEN, DataQuery.of("Slot"), SpongeMusicianLibrary.instance().getId() + ":slot", "Slot");
    public static final Key<Value<UUID>> UUID = KeyFactory.makeSingleKey(TypeTokens.UUID_TOKEN, TypeTokens.UUID_VALUE_TOKEN, DataQuery.of("UUID"), SpongeMusicianLibrary.instance().getId() + ":uuid", "UUID");

    private MLKeys() {

    }
}

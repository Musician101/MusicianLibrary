package io.musician101.musicianlibrary.java.minecraft.sponge.data.key;

import java.util.UUID;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public final class MLKeys {

    public static final Key<Value<Integer>> SLOT = Key.builder().type(TypeTokens.INTEGER_VALUE_TOKEN).query(DataQuery.of("Slot")).id("slot").name("Slot").build();
    public static final Key<Value<UUID>> UUID = Key.builder().type(TypeTokens.UUID_VALUE_TOKEN).query(DataQuery.of("UUID")).id("uuid").name("UUID").build();

    private MLKeys() {

    }
}

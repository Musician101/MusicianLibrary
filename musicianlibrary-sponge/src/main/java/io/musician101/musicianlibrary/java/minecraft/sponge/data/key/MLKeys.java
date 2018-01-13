package io.musician101.musicianlibrary.java.minecraft.sponge.data.key;

import io.musician101.musicianlibrary.java.minecraft.sponge.SpongeMusicianLibrary;
import java.util.UUID;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public final class MLKeys {

    /* While it's technically not possible for the plugin instance to not exist if this jar is loaded,
     * on the off chance the instance does not exist, we let Java throw the NoSuchElementException from Optional#get()
     */
    public static final Key<Value<Integer>> SLOT = Key.builder().type(TypeTokens.INTEGER_VALUE_TOKEN).query(DataQuery.of("Slot")).id(SpongeMusicianLibrary.instance().get().getId() + ":slot").name("Slot").build();
    public static final Key<Value<UUID>> UUID = Key.builder().type(TypeTokens.UUID_VALUE_TOKEN).query(DataQuery.of("UUID")).id(SpongeMusicianLibrary.instance().get().getId() + ":uuid").name("UUID").build();

    private MLKeys() {

    }
}

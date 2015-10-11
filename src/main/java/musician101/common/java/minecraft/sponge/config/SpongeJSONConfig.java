package musician101.common.java.minecraft.sponge.config;

import musician101.common.java.config.JSONConfig;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.persistence.DataBuilder;
import org.spongepowered.api.service.persistence.SerializationService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "unused"})
public class SpongeJSONConfig extends JSONConfig
{
    public SpongeJSONConfig()
    {
        super();
    }

    public static SpongeJSONConfig load(File file) throws IOException, ParseException
    {
        return (SpongeJSONConfig) JSONConfig.load(file);
    }

    public static SpongeJSONConfig load(String string) throws ParseException
    {
        return (SpongeJSONConfig) JSONConfig.load(string);
    }

    public SpongeJSONConfig getSpongeJSONConfig(String key)
    {
        return (SpongeJSONConfig) getJSONConfig(key);
    }

    public ItemStack getItemStack(SerializationService ss, String key)
    {
        DataBuilder<ItemStack> db = ss.getBuilder(ItemStack.class).get();
        return db.build(new MemoryDataContainer().set(DataQuery.of(), get(key))).get();
    }

    public void setItemStack(String key, ItemStack itemStack)
    {
        put(key, itemStack.toContainer().toString());
    }

    public List<ItemStack> getItemStackList(SerializationService ss, String key)
    {
        return getList(key).stream().map(object -> ss.getBuilder(ItemStack.class).get().build(new MemoryDataContainer().set(DataQuery.of(), object)).get()).collect(Collectors.toList());
    }

    public void setItemStackList(String key, List<ItemStack> list)
    {
        put(key, list.stream().map(itemStack -> itemStack.toContainer().toString()));
    }
}

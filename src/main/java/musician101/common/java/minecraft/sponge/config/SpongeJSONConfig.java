package musician101.common.java.minecraft.sponge.config;

import musician101.common.java.config.JSONConfig;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.data.DataManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class SpongeJSONConfig extends JSONConfig
{
    DataManager dm;

    public SpongeJSONConfig()
    {
        super();
    }

    public SpongeJSONConfig(DataManager dm)
    {
        this();
        this.dm = dm;
    }

    public void setDataManager(DataManager dm)
    {
        this.dm = dm;
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

    public ItemStack getItemStack(String key)
    {
        return getDataView(key, ItemStack.class).get();
    }

    public void setItemStack(String key, ItemStack itemStack)
    {
        put(key, itemStack.toContainer().toString());
    }

    public List<ItemStack> getItemStackList(String key)
    {
        return getStringList(key).stream().map(object -> getDataView(object, ItemStack.class).get()).collect(Collectors.toList());
    }

    public void setItemStackList(String key, List<ItemStack> list)
    {
        put(key, list.stream().map(itemStack -> itemStack.toContainer().toString()));
    }

    public List<PotionEffect> getPotionEffectsList(String key)
    {
        return getStringList(key).stream().map(object -> getDataView(object, PotionEffect.class).get()).collect(Collectors.toList());
    }

    public void setPotionEffectsList(String key, List<PotionEffect> list)
    {
        put(key, list.stream().map(effect -> effect.toContainer().toString()));
    }

    public PotionEffect getPotionEffect(String key)
    {
        return getDataView(key, PotionEffect.class).get();
    }

    private <T extends DataSerializable> Optional<T> getDataView(Object object, Class<T> clazz)
    {
        return dm.getBuilder(clazz).get().build(new MemoryDataContainer().set(DataQuery.of(), get(object)));
    }

    private <T extends DataSerializable> Optional<T> getDataView(String key, Class<T> clazz)
    {
        return dm.getBuilder(clazz).get().build(new MemoryDataContainer().set(DataQuery.of(), get(key)));
    }
}

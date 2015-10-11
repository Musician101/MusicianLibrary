package musician101.common.java.minecraft.spigot.config;

import musician101.common.java.config.JSONConfig;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "SameParameterValue", "WeakerAccess", "unused"})
public class SpigotJSONConfig extends JSONConfig
{
    public SpigotJSONConfig()
    {
        super();
    }

    public static SpigotJSONConfig load(File file) throws IOException, ParseException
    {
        return (SpigotJSONConfig) JSONConfig.load(file);
    }

    public static SpigotJSONConfig load(String string) throws ParseException
    {
        return (SpigotJSONConfig) JSONConfig.load(string);
    }

    public SpigotJSONConfig getSpigotJSONConfig(String key)
    {
        return (SpigotJSONConfig) getJSONConfig(key);
    }

    public Color getColor(String key)
    {
        SpigotJSONConfig colorJson = getSpigotJSONConfig(key);
        return Color.fromRGB(colorJson.getInteger("blue", 0), colorJson.getInteger("green", 0), colorJson.getInteger("red", 0));
    }

    public void setColor(String key, Color color)
    {
        SpigotJSONConfig colorJson = new SpigotJSONConfig();
        colorJson.put("red", color.getRed());
        colorJson.put("green", color.getGreen());
        colorJson.put("blue", color.getBlue());
        put(key, colorJson);
    }

    public List<Color> getColorList(String key)
    {
        List<Color> colors = new ArrayList<>();
        for (Object object : getList(key))
        {
            SpigotJSONConfig colorJson = (SpigotJSONConfig) object;
            colors.add(Color.fromRGB(colorJson.getInteger("blue", 0), colorJson.getInteger("green", 0), colorJson.getInteger("red", 0)));
        }

        return colors;
    }

    public void setColorList(String key, List<Color> colors)
    {
        List<SpigotJSONConfig> colorsJson = new ArrayList<>();
        for (Color color : colors)
        {
            SpigotJSONConfig colorJson = new SpigotJSONConfig();
            colorJson.put("red", color.getRed());
            colorJson.put("green", color.getGreen());
            colorJson.put("blue", color.getBlue());
            colorsJson.add(colorJson);
        }

        put(key, colorsJson);
    }

    public Map<Enchantment, Integer> getEnchantments(String key)
    {
        SpigotJSONConfig enchantsJson = getSpigotJSONConfig(key);
        Map<Enchantment, Integer> enchants = new HashMap<>();
        for (Enchantment enchant : Enchantment.values())
            if (enchantsJson.containsKey(enchant.toString()))
                enchants.put(enchant, enchantsJson.getInteger(enchant.toString()));

        return enchants;
    }

    public void setEnchantments(String key, Map<Enchantment, Integer> enchants)
    {
        SpigotJSONConfig enchantsJson = new SpigotJSONConfig();
        for (Enchantment enchant : enchants.keySet())
            enchantsJson.put(enchant.toString(), enchants.get(enchant));

        put(key, enchantsJson);
    }

    public FireworkEffect getFireworkEffect(String key)
    {
        SpigotJSONConfig fwJson = getSpigotJSONConfig(key);
        Builder fw = FireworkEffect.builder();
        if (fwJson.containsKey("flicker"))
            fw.flicker(fwJson.getBoolean("flicker"));

        if (fwJson.containsKey("trail"))
            fw.trail(fwJson.getBoolean("trail"));

        if (fwJson.containsKey("type"))
            fw.with(Type.valueOf(fwJson.getString("type")));

        if (fwJson.containsKey("colors"))
            getColorList("colors").forEach(fw::withColor);

        if (fwJson.containsKey("fade-colors"))
            getColorList("fade-colors").forEach(fw::withFade);

        return fw.build();
    }

    public void setFireworkEffect(String key, FireworkEffect effect)
    {
        SpigotJSONConfig fwJson = new SpigotJSONConfig();
        fwJson.put("flicker", effect.hasFlicker());
        fwJson.put("trail", effect.hasTrail());
        fwJson.put("type", effect.getType().toString());
        fwJson.setColorList("colors", effect.getColors());
        fwJson.setColorList("fade-colors", effect.getFadeColors());
        put(key, fwJson);
    }

    public List<FireworkEffect> getFireworkEffectList(String key)
    {
        List<FireworkEffect> effects = new ArrayList<>();
        for (Object object : getList(key))
        {
            SpigotJSONConfig fwJson = (SpigotJSONConfig) object;
            Builder fw = FireworkEffect.builder();
            if (fwJson.containsKey("flicker"))
                fw.flicker(fwJson.getBoolean("flicker"));

            if (fwJson.containsKey("trail"))
                fw.trail(fwJson.getBoolean("trail"));

            if (fwJson.containsKey("type"))
                fw.with(Type.valueOf(fwJson.getString("type")));

            if (fwJson.containsKey("colors"))
                getColorList("colors").forEach(fw::withColor);

            if (fwJson.containsKey("fade-colors"))
                getColorList("fade-colors").forEach(fw::withFade);

            effects.add(fw.build());
        }

        return effects;
    }

    public void setFireworkEffectList(String key, List<FireworkEffect> effects)
    {
        for (FireworkEffect effect : effects)
        {
            SpigotJSONConfig fwJson = new SpigotJSONConfig();
            fwJson.put("flicker", effect.hasFlicker());
            fwJson.put("trail", effect.hasTrail());
            fwJson.put("type", effect.getType().toString());
            fwJson.setColorList("colors", effect.getColors());
            fwJson.setColorList("fade-colors", effect.getFadeColors());
            put(key, fwJson);
        }
    }

    public Inventory getInventory(String key)
    {
        if (!containsKey(key))
            return null;

        SpigotJSONConfig invJson = getSpigotJSONConfig(key);
        Inventory inv;
        InventoryType invType = InventoryType.valueOf(invJson.getString("type"));
        if (invType == InventoryType.CHEST)
            inv = Bukkit.createInventory(null, invJson.getInteger("slots"), invJson.getString("title"));
        else
            inv = Bukkit.createInventory(null, invType, invJson.getString("title"));

        if (invJson.containsKey("items"))
        {
            SpigotJSONConfig items = invJson.getSpigotJSONConfig("items");
            for (int x = 0; x < inv.getSize(); x++)
                inv.setItem(x, items.getItemStack(x));
        }

        return inv;
    }

    public void setInventory(String key, Inventory inv)
    {
        SpigotJSONConfig invJson = new SpigotJSONConfig();
        invJson.put("type", inv.getType().toString());
        invJson.put("slots", inv.getSize());
        invJson.put("title", inv.getTitle());
        for (int x = 0; x > inv.getSize(); x++)
            if (inv.getItem(x) != null)
                invJson.setItemStack(x, inv.getItem(x));

        put(key, invJson);
    }

    public ItemMeta getItemMeta(String key)
    {
        SpigotJSONConfig metaJson = getSpigotJSONConfig(key);
        return metaJson.toItemMeta();
    }

    public void setItemMeta(String key, ItemMeta meta, Material material)
    {
        SpigotJSONConfig metaJson = new SpigotJSONConfig();
        String type;
        if (material == Material.ENCHANTED_BOOK)
        {
            setEnchantmentStorageMeta((EnchantmentStorageMeta) meta, metaJson);
            type = "ENCHANTED_STORAGE";
        }
        else if (material == Material.FIREWORK)
        {
            setFireworkMeta((FireworkMeta) meta, metaJson);
            type = "FIREWORK";
        }
        else if (material == Material.FIREWORK_CHARGE)
        {
            setFireworkEffectMeta((FireworkEffectMeta) meta, metaJson);
            type = "FIREWORK_EFFECT";
        }
        else if (material == Material.LEATHER_BOOTS || material == Material.LEATHER_CHESTPLATE || material == Material.LEATHER_HELMET || material == Material.LEATHER_LEGGINGS)
        {
            setLeatherArmorMeta((LeatherArmorMeta) meta, metaJson);
            type = "LEATHER_ARMOR";
        }
        else if (material == Material.MAP)
        {
            setMapMeta((MapMeta) meta, metaJson);
            type = "MAP";
        }
        else if (material == Material.POTION)
        {
            setPotionMeta((PotionMeta) meta, metaJson);
            type = "POTION";
        }
        else if (material == Material.SKULL_ITEM)
        {
            setSkullMeta((SkullMeta) meta, metaJson);
            type = "SKULL";
        }
        else if (material == Material.BOOK_AND_QUILL || material == Material.WRITTEN_BOOK)
        {
            setBookMeta((BookMeta) meta, metaJson);
            type = "WRITTEN_BOOK";
        }
        else
        {
            setGeneralItemMeta(meta, metaJson);
            type = "GENERAL";
        }

        metaJson.put("type", type);
        put(key, metaJson);
    }

    public ItemMeta toItemMeta()
    {
        String metaType = getString("type");
        if (metaType.equals("ENCHANT_STORAGE"))
            return getEnchantedBookMeta(this);
        else if (metaType.equalsIgnoreCase("FIREWORK"))
            return getFireworkMeta(this);
        else if (metaType.equals("FIREWORK_EFFECT"))
            return getFireworkEffectMeta(this);
        else if (metaType.equals("LEATHER_ARMOR"))
            return getLeatherArmorMeta(this);
        else if (metaType.equals("MAP"))
            return getMapMeta(this);
        else if (metaType.equals("POTION"))
            return getPotionMeta(this);
        else if (metaType.equals("SKULL"))
            return getSkullMeta(this);
        else if (metaType.equals("WRITTEN_BOOK"))
            return getWrittenBookMeta(this);

        ItemMeta im = newItemMeta();
        getGeneralItemMeta(im, this);
        return im;
    }

    private EnchantmentStorageMeta getEnchantedBookMeta(SpigotJSONConfig metaJson)
    {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) newItemMeta();
        if (metaJson.containsKey("stored-enchants"))
        {
            Map<Enchantment, Integer> enchants = getEnchantments("stored-enchants");
            for (Enchantment enchant : enchants.keySet())
                meta.addStoredEnchant(enchant, enchants.get(enchant), false);
        }

        getGeneralItemMeta(meta, metaJson);
        return meta;
    }

    private void setEnchantmentStorageMeta(EnchantmentStorageMeta meta, SpigotJSONConfig metaJson)
    {
        metaJson.setEnchantments("stored-enchants", meta.getStoredEnchants());
        setGeneralItemMeta(meta, metaJson);
    }

    private FireworkEffectMeta getFireworkEffectMeta(SpigotJSONConfig metaJson)
    {
        FireworkEffectMeta meta = (FireworkEffectMeta) newItemMeta();
        if (metaJson.containsKey("effect"))
            meta.setEffect(getFireworkEffect("effect"));

        getGeneralItemMeta(meta, metaJson);
        return meta;
    }

    private void setFireworkEffectMeta(FireworkEffectMeta meta, SpigotJSONConfig metaJson)
    {
        if (meta.hasEffect())
            metaJson.setFireworkEffect("effect", meta.getEffect());

        setGeneralItemMeta(meta, metaJson);
    }

    private FireworkMeta getFireworkMeta(SpigotJSONConfig metaJson)
    {
        FireworkMeta meta = (FireworkMeta) newItemMeta();
        if (metaJson.containsKey("effects"))
            getFireworkEffectList("effects").forEach(meta::addEffect);

        getGeneralItemMeta(meta, metaJson);
        return meta;
    }

    private void setFireworkMeta(FireworkMeta meta, SpigotJSONConfig metaJson)
    {
        if (meta.hasEffects())
            metaJson.setFireworkEffectList("effects", meta.getEffects());

        getGeneralItemMeta(meta, metaJson);
    }

    private LeatherArmorMeta getLeatherArmorMeta(SpigotJSONConfig metaJson)
    {
        LeatherArmorMeta meta = (LeatherArmorMeta) newItemMeta();
        if (metaJson.containsKey("color"))
            meta.setColor(metaJson.getColor("color"));

        getGeneralItemMeta(meta, metaJson);
        return meta;
    }

    private void setLeatherArmorMeta(LeatherArmorMeta meta, SpigotJSONConfig metaJson)
    {
        metaJson.setColor("color", meta.getColor());
        getGeneralItemMeta(meta, metaJson);
    }

    private MapMeta getMapMeta(SpigotJSONConfig metaJson)
    {
        MapMeta meta = (MapMeta) newItemMeta();
        if (metaJson.containsKey("scaling"))
            meta.setScaling(metaJson.getBoolean("scaling"));

        getGeneralItemMeta(meta, metaJson);
        return meta;
    }

    private void setMapMeta(MapMeta meta, SpigotJSONConfig metaJson)
    {
        metaJson.put("scaling", meta.isScaling());
        getGeneralItemMeta(meta, metaJson);
    }

    private PotionMeta getPotionMeta(SpigotJSONConfig metaJson)
    {
        PotionMeta meta = (PotionMeta) newItemMeta();
        if (metaJson.containsKey("effects"))
            for (PotionEffect effect : getPotionEffectList("effects"))
                meta.addCustomEffect(effect, true);

        getGeneralItemMeta(meta, metaJson);
        return meta;
    }

    private void setPotionMeta(PotionMeta meta, SpigotJSONConfig metaJson)
    {
        if (meta.hasCustomEffects())
            metaJson.setPotionEffectList("effects", meta.getCustomEffects());

        setGeneralItemMeta(meta, metaJson);
    }

    private SkullMeta getSkullMeta(SpigotJSONConfig metaJson)
    {
        SkullMeta meta = (SkullMeta) newItemMeta();
        if (metaJson.containsKey("owner"))
            meta.setOwner(metaJson.getString("owner"));

        getGeneralItemMeta(meta, metaJson);
        return meta;
    }

    private void setSkullMeta(SkullMeta meta, SpigotJSONConfig metaJson)
    {
        if (meta.hasOwner())
            metaJson.put("owner", meta.getOwner());

        setGeneralItemMeta(meta, metaJson);
    }

    private BookMeta getWrittenBookMeta(SpigotJSONConfig metaJson)
    {
        BookMeta meta = (BookMeta) newItemMeta();
        meta.setAuthor(metaJson.getString("author"));

        if (metaJson.containsKey("pages"))
            for (Object page : metaJson.getList("pages"))
                meta.addPage(page.toString());

        if (metaJson.containsKey("title"))
            meta.setTitle(metaJson.get("title").toString());

        getGeneralItemMeta(meta, metaJson);
        return meta;
    }

    private void setBookMeta(BookMeta meta, SpigotJSONConfig metaJson)
    {
        if (meta.hasAuthor())
            metaJson.put("author", meta.getAuthor());

        if (meta.hasPages())
            metaJson.put("pages", meta.getPages());

        if (meta.hasTitle())
            metaJson.put("title", meta.getTitle());

        setGeneralItemMeta(meta, metaJson);
    }

    @SuppressWarnings("UnusedReturnValue")
    private SpigotJSONConfig getGeneralItemMeta(ItemMeta im, SpigotJSONConfig metaJson)
    {
        if (metaJson.containsKey("displayName"))
            im.setDisplayName(metaJson.getString("displayName"));

        if (metaJson.containsKey("enchants"))
        {
            Map<Enchantment, Integer> enchants = getEnchantments("enchants");
            for (Enchantment enchant : enchants.keySet())
                im.addEnchant(enchant, enchants.get(enchant), false);
        }

        if (metaJson.containsKey("lore"))
        {
            List<String> lore = metaJson.getList("lore");
            im.setLore(lore);
        }

        return this;
    }

    private void setGeneralItemMeta(ItemMeta meta, SpigotJSONConfig metaJson)
    {
        if (meta.hasDisplayName())
            metaJson.put("name", meta.getDisplayName());

        if (meta.hasEnchants())
            metaJson.setEnchantments("enchants", meta.getEnchants());

        if (meta.hasLore())
            metaJson.put("lore", meta.getLore());
    }

    private ItemMeta newItemMeta()
    {
        return new ItemStack(Material.AIR).getItemMeta();
    }

    private ItemStack getItemStack(int key)
    {
        return getItemStack(key + "");
    }

    private void setItemStack(int key, ItemStack item)
    {
        setItemStack(key + "", item);
    }

    public ItemStack getItemStack(String key)
    {
        SpigotJSONConfig itemJson = getSpigotJSONConfig(key);
        ItemStack item = new ItemStack(itemJson.getMaterial("material"), itemJson.getInteger("amount"), itemJson.getShort("durability"));
        if (itemJson.containsKey("meta"))
            item.setItemMeta(itemJson.getItemMeta("meta"));

        return item;
    }

    public void setItemStack(String key, ItemStack item)
    {
        SpigotJSONConfig itemJson = getSpigotJSONConfig(key);
        itemJson.setMaterial("material", item.getType());
        itemJson.put("amount", item.getAmount());
        itemJson.put("durability", item.getDurability());
        if (item.hasItemMeta())
            itemJson.setItemMeta("meta", item.getItemMeta(), item.getType());

        put(key, itemJson);
    }

    public Material getMaterial(String key)
    {
        return Material.matchMaterial(getString(key).toUpperCase());
    }

    public void setMaterial(String key, Material material)
    {
        put(key, material.toString());
    }

    public PotionEffect getPotionEffect(String key)
    {
        SpigotJSONConfig potionJson = getSpigotJSONConfig(key);
        boolean ambient = potionJson.getBoolean("ambient");
        int amplifier = potionJson.getInteger("amplifier");
        int duration = potionJson.getInteger("duration");
        PotionEffectType type = PotionEffectType.getByName(potionJson.getString("effect"));
        return new PotionEffect(type, duration, amplifier, ambient);
    }

    public void setPotionEffect(String key, PotionEffect effect)
    {
        SpigotJSONConfig potionJson = new SpigotJSONConfig();
        potionJson.put("effect", effect.getType().toString());
        potionJson.put("amplifier", effect.getAmplifier());
        potionJson.put("duration", effect.getDuration());
        potionJson.put("ambient", effect.isAmbient());
        put(key, potionJson);
    }

    public List<PotionEffect> getPotionEffectList(String key)
    {
        List<PotionEffect> effects = new ArrayList<>();
        for (Object object : getList(key))
        {
            SpigotJSONConfig potionJson = (SpigotJSONConfig) object;
            boolean ambient = potionJson.getBoolean("ambient");
            int amplifier = potionJson.getInteger("amplifier");
            int duration = potionJson.getInteger("duration");
            PotionEffectType type = PotionEffectType.getByName(potionJson.getString("effect"));
            effects.add(new PotionEffect(type, duration, amplifier, ambient));
        }

        return effects;
    }

    public void setPotionEffectList(String key, List<PotionEffect> effects)
    {
        List<SpigotJSONConfig> potionsJson = new ArrayList<>();
        for (PotionEffect effect : effects)
        {
            SpigotJSONConfig potionJson = new SpigotJSONConfig();
            potionJson.put("effect", effect.getType().toString());
            potionJson.put("amplifier", effect.getAmplifier());
            potionJson.put("duration", effect.getDuration());
            potionJson.put("ambient", effect.isAmbient());
            potionsJson.add(potionJson);
        }

        put(key, potionsJson);
    }
}

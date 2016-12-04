package io.musician101.musicianlibrary.java.minecraft.spigot;

import io.musician101.musicianlibrary.java.minecraft.AbstractRegion;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;


public class SpigotRegion extends AbstractRegion<Location> {
    private String worldName = "";

    private SpigotRegion(Location location) {
        this(location, location);
    }

    private SpigotRegion(Location location, Location location2) {
        super(Math.min((int) location.getX(), (int) location2.getX()),
                Math.max((int) location.getX(), (int) location2.getX()),
                Math.min((int) location.getY(), (int) location2.getY()),
                Math.max((int) location.getY(), (int) location2.getY()),
                Math.min((int) location.getZ(), (int) location2.getZ()),
                Math.max((int) location.getZ(), (int) location2.getZ()));

        this.worldName = location.getWorld().getName();
    }

    public SpigotRegion(Map<String, Object> map) {
        super(map.containsKey("MinX") ? (Integer) map.get("MinX") : 0,
                map.containsKey("MaxX") ? (Integer) map.get("MaxX") : 0,
                map.containsKey("MinY") ? (Integer) map.get("MinY") : 0,
                map.containsKey("MaxY") ? (Integer) map.get("MaxY") : 0,
                map.containsKey("MinZ") ? (Integer) map.get("MinZ") : 0,
                map.containsKey("MaxZ") ? (Integer) map.get("MaxZ") : 0);

        this.worldName = map.containsKey("World") ? (String) map.get("World") : "";
    }

    public static SpigotRegion createFromLocationRadius(Location location, double radius) {
        return createFromLocationRadius(location, radius, radius, radius);
    }

    public static SpigotRegion createFromLocationRadius(Location location, double xRadius, double yRadius, double zRadius) {
        Validate.notNull(location);
        if (xRadius < 0 || yRadius < 0 || zRadius < 0)
            throw new IllegalArgumentException("The radius cannot be negative!");

        return xRadius > 0 || yRadius > 0 || zRadius > 0 ? new SpigotRegion(location.clone().subtract(xRadius, yRadius, zRadius), location.clone().add(xRadius, yRadius, zRadius)) : new SpigotRegion(location);
    }

    public World getWorld() {
        World world = Bukkit.getServer().getWorld(this.worldName);
        if (world == null)
            world = Bukkit.getServer().createWorld(WorldCreator.name(this.worldName));

        return world;
    }

    @Override
    public boolean isInRegion(Location location) {
        return location.getWorld().getName().equals(worldName) && location.getX() > getMinX() && location.getX() < getMaxX() && location.getZ() > getMinZ() && location.getZ() < getMaxZ();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("World", this.worldName);
        return map;
    }
}

package io.musician101.musicianlibrary.java.minecraft.spigot.region;

import io.musician101.musicianlibrary.java.minecraft.common.region.AbstractRegion;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class SpigotRegion extends AbstractRegion<Vector, World> {

    private SpigotRegion(@Nonnull Vector position, @Nonnull World world) {
        this(position, position, world);
    }

    private SpigotRegion(@Nonnull Vector location, @Nonnull Vector location2, @Nonnull World world) {
        super(world, Math.min((int) location.getX(), (int) location2.getX()), Math.max((int) location.getX(), (int) location2.getX()), Math.min((int) location.getY(), (int) location2.getY()), Math.max((int) location.getY(), (int) location2.getY()), Math.min((int) location.getZ(), (int) location2.getZ()), Math.max((int) location.getZ(), (int) location2.getZ()));
    }

    public SpigotRegion(@Nonnull Map<String, Object> map) {
        super(getWorld(map.get("world")), map.containsKey("min_x") ? (Integer) map.get("min_x") : 0, map.containsKey("max_x") ? (Integer) map.get("max_x") : 0, map.containsKey("min_y") ? (Integer) map.get("min_y") : 0, map.containsKey("max_y") ? (Integer) map.get("max_y") : 0, map.containsKey("min_z") ? (Integer) map.get("min_z") : 0, map.containsKey("max_z") ? (Integer) map.get("max_z") : 0);
    }

    public static SpigotRegion createFromLocationRadius(Location location, double radius) {
        return createFromLocationRadius(location, radius, radius, radius);
    }

    //Location#getWorld() shouldn't be @Nullable because it throws an error if it returns a NULL world.
    @SuppressWarnings("ConstantConditions")
    public static SpigotRegion createFromLocationRadius(Location location, double xRadius, double yRadius, double zRadius) {
        Validate.notNull(location);
        if (xRadius < 0 || yRadius < 0 || zRadius < 0) {
            throw new IllegalArgumentException("The radius cannot be negative!");
        }

        return xRadius > 0 || yRadius > 0 || zRadius > 0 ? new SpigotRegion(location.clone().subtract(xRadius, yRadius, zRadius).toVector(), location.clone().add(xRadius, yRadius, zRadius).toVector(), location.getWorld()) : new SpigotRegion(location.toVector(), location.getWorld());
    }

    @Nonnull
    private static World getWorld(@Nullable Object worldName) {
        if (worldName == null) {
            throw new IllegalArgumentException("World name not set.");
        }

        World world = Bukkit.getWorld(worldName.toString());
        if (world == null) {
            throw new IllegalArgumentException(worldName.toString() + " does not exist.");
        }

        return world;
    }

    @Override
    public boolean isInRegion(Vector position, World world) {
        return world.getUID().equals(getWorld().getUID()) && position.getX() > getMinX() && position.getX() < getMaxX() && position.getZ() > getMinZ() && position.getZ() < getMaxZ();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("world", getWorld().getName());
        return map;
    }
}

package io.musician101.musicianlibrary.java.minecraft.sponge.region;

import io.musician101.musicianlibrary.java.minecraft.common.region.AbstractRegion;
import java.util.Map;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeRegion extends AbstractRegion<Location<World>> {

    private String worldName;

    private SpongeRegion(Location<World> location) {
        this(location, location);
    }

    private SpongeRegion(Location<World> location, Location<World> location2) {
        super(Math.min((int) location.getPosition().getX(), (int) location2.getPosition().getX()), Math.min((int) location.getPosition().getY(), (int) location2.getPosition().getY()), Math.min((int) location.getPosition().getZ(), (int) location2.getPosition().getZ()), Math.max((int) location.getPosition().getX(), (int) location2.getPosition().getX()), Math.max((int) location.getPosition().getY(), (int) location2.getPosition().getY()), Math.max((int) location.getPosition().getZ(), (int) location2.getPosition().getZ()));

        this.worldName = location.getExtent().getName();
    }

    public SpongeRegion(ConfigurationNode node) {
        super(node.getNode("MinX").getInt(0), node.getNode("MinY").getInt(0), node.getNode("MinZ").getInt(0), node.getNode("MaxX").getInt(0), node.getNode("MaxY").getInt(0), node.getNode("MaxZ").getInt(0));

        this.worldName = !node.getNode("World").isVirtual() ? node.getNode("World").getString() : "";
    }

    public static SpongeRegion createFromLocationRadius(Location<World> location, double radius) {
        return createFromLocationRadius(location, radius, radius, radius);
    }

    public static SpongeRegion createFromLocationRadius(Location<World> location, double xRadius, double yRadius, double zRadius) {
        if (xRadius < 0 || yRadius < 0 || zRadius < 0) {
            throw new IllegalArgumentException("The radius cannot be negative!");
        }

        return xRadius > 0 || yRadius > 0 || zRadius > 0 ? new SpongeRegion(location.sub(xRadius, yRadius, zRadius), location.add(xRadius, yRadius, zRadius)) : new SpongeRegion(location);
    }

    public World getWorld() {
        return Sponge.getGame().getServer().getWorld(worldName).orElseThrow(() -> new NullPointerException("World does not exist."));
    }

    @Override
    public boolean isInRegion(Location<World> location) {
        return location.getExtent().getName().equals(worldName) && location.getPosition().getX() > getMinX() && location.getPosition().getX() < getMaxX() && location.getPosition().getY() > getMinY() && location.getPosition().getY() < getMaxY() && location.getPosition().getZ() > getMinZ() && location.getPosition().getZ() < getMaxZ();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("World", this.worldName);
        return map;
    }
}

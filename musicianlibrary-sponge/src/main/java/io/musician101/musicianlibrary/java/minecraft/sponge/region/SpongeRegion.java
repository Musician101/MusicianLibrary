package io.musician101.musicianlibrary.java.minecraft.sponge.region;

import com.flowpowered.math.vector.Vector3d;
import io.musician101.musicianlibrary.java.minecraft.common.region.AbstractRegion;
import java.util.Map;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeRegion extends AbstractRegion<Vector3d, World> {

    private SpongeRegion(Vector3d position, World world) {
        this(position, position, world);
    }

    private SpongeRegion(Vector3d position, Vector3d position2, World world) {
        super(world, Math.min((int) position.getX(), (int) position2.getX()), Math.min((int) position.getY(), (int) position2.getY()), Math.min((int) position.getZ(), (int) position2.getZ()), Math.max((int) position.getX(), (int) position2.getX()), Math.max((int) position.getY(), (int) position2.getY()), Math.max((int) position.getZ(), (int) position2.getZ()));
    }

    public SpongeRegion(ConfigurationNode node) {
        super(getWorld(node.getNode("world")), node.getNode("min_x").getInt(0), node.getNode("min_y").getInt(0), node.getNode("min_z").getInt(0), node.getNode("max_x").getInt(0), node.getNode("max_y").getInt(0), node.getNode("max_z").getInt(0));
    }

    public static SpongeRegion createFromLocationRadius(Location<World> location, double radius) {
        return createFromLocationRadius(location, radius, radius, radius);
    }

    public static SpongeRegion createFromLocationRadius(Location<World> location, double xRadius, double yRadius, double zRadius) {
        if (xRadius < 0 || yRadius < 0 || zRadius < 0) {
            throw new IllegalArgumentException("The radius cannot be negative!");
        }

        return xRadius > 0 || yRadius > 0 || zRadius > 0 ? new SpongeRegion(location.getPosition().sub(xRadius, yRadius, zRadius), location.getPosition().add(xRadius, yRadius, zRadius), location.getExtent()) : new SpongeRegion(location.getPosition(), location.getExtent());
    }

    @Nonnull
    private static World getWorld(@Nonnull ConfigurationNode node) {
        if (node.isVirtual()) {
            throw new IllegalArgumentException("World name not set.");
        }

        String worldName = node.getString();
        if (worldName == null) {
            throw new IllegalArgumentException("World name not set.");
        }

        return Sponge.getServer().getWorld(node.getString()).orElseThrow(() -> new IllegalArgumentException(node.toString() + " does not exist."));
    }

    @Override
    public boolean isInRegion(Vector3d position, World world) {
        return world.getUniqueId().equals(getWorld().getUniqueId()) && position.getX() > getMinX() && position.getX() < getMaxX() && position.getY() > getMinY() && position.getY() < getMaxY() && position.getZ() > getMinZ() && position.getZ() < getMaxZ();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("world", getWorld().getName());
        return map;
    }
}

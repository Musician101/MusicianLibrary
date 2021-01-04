package io.musician101.musicianlibrary.java.minecraft.sponge.region;

import io.musician101.musicianlibrary.java.minecraft.common.region.AbstractRegion;
import java.util.Map;
import javax.annotation.Nonnull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.math.vector.Vector3i;

public class SpongeRegion extends AbstractRegion<Vector3i, ServerWorld> {

    private SpongeRegion(Vector3i position, ServerWorld world) {
        this(position, position, world);
    }

    private SpongeRegion(Vector3i position, Vector3i position2, ServerWorld world) {
        super(world, Math.min(position.getX(), position2.getX()), Math.min(position.getY(), position2.getY()), Math.min(position.getZ(), position2.getZ()), Math.max(position.getX(), position2.getX()), Math.max(position.getY(), position2.getY()), Math.max(position.getZ(), position2.getZ()));
    }

    public SpongeRegion(ConfigurationNode node) {
        super(getWorld(node.node("world")), node.node("min_x").getInt(0), node.node("min_y").getInt(0), node.node("min_z").getInt(0), node.node("max_x").getInt(0), node.node("max_y").getInt(0), node.node("max_z").getInt(0));
    }

    public static SpongeRegion createFromLocationRadius(ServerLocation location, double radius) {
        return createFromLocationRadius(location, radius, radius, radius);
    }

    public static SpongeRegion createFromLocationRadius(ServerLocation location, double xRadius, double yRadius, double zRadius) {
        if (xRadius < 0 || yRadius < 0 || zRadius < 0) {
            throw new IllegalArgumentException("The radius cannot be negative!");
        }

        return xRadius > 0 || yRadius > 0 || zRadius > 0 ? new SpongeRegion(location.getBlockPosition().sub(xRadius, yRadius, zRadius), location.getBlockPosition().add(xRadius, yRadius, zRadius), location.getWorld()) : new SpongeRegion(location.getBlockPosition(), location.getWorld());
    }

    @Nonnull
    private static ServerWorld getWorld(@Nonnull ConfigurationNode node) {
        if (node.empty()) {
            throw new IllegalArgumentException("World name not set.");
        }

        String worldName = node.getString();
        if (worldName == null) {
            throw new IllegalArgumentException("World name not set.");
        }

        return Sponge.getServer().getWorldManager().world(ResourceKey.resolve(worldName)).orElseThrow(() -> new IllegalArgumentException(node.toString() + " does not exist."));
    }

    @Override
    public boolean isInRegion(Vector3i position, ServerWorld world) {
        return world.getUniqueId().equals(getWorld().getUniqueId()) && position.getX() > getMinX() && position.getX() < getMaxX() && position.getY() > getMinY() && position.getY() < getMaxY() && position.getZ() > getMinZ() && position.getZ() < getMaxZ();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("world", getWorld().getKey().asString());
        return map;
    }
}

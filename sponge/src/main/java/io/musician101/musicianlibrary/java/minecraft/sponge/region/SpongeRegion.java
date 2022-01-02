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
        super(world, Math.min(position.x(), position2.x()), Math.min(position.y(), position2.y()), Math.min(position.z(), position2.z()), Math.max(position.x(), position2.x()), Math.max(position.y(), position2.y()), Math.max(position.z(), position2.z()));
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

        return xRadius > 0 || yRadius > 0 || zRadius > 0 ? new SpongeRegion(location.blockPosition().sub(xRadius, yRadius, zRadius), location.blockPosition().add(xRadius, yRadius, zRadius), location.world()) : new SpongeRegion(location.blockPosition(), location.world());
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

        return Sponge.server().worldManager().world(ResourceKey.resolve(worldName)).orElseThrow(() -> new IllegalArgumentException(node + " does not exist."));
    }

    @Override
    public boolean isInRegion(Vector3i position, ServerWorld world) {
        return world.uniqueId().equals(getWorld().uniqueId()) && position.x() > getMinX() && position.x() < getMaxX() && position.y() > getMinY() && position.y() < getMaxY() && position.z() > getMinZ() && position.z() < getMaxZ();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("world", getWorld().key().asString());
        return map;
    }
}

package io.musician101.musicianlibrary.java.minecraft.common.region;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

public abstract class AbstractRegion<V, W> {

    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private final int minX;
    private final int minY;
    private final int minZ;
    @Nonnull
    private final W world;

    protected AbstractRegion(@Nonnull W world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    @Nonnull
    public W getWorld() {
        return world;
    }

    public abstract boolean isInRegion(V position, W world);

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("min_x", minX);
        map.put("max_x", maxX);
        map.put("min_y", minY);
        map.put("max_y", maxY);
        map.put("min_z", minZ);
        map.put("max_z", maxZ);
        return map;
    }
}

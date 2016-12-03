package io.musician101.musicianlibrary.java.minecraft;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractRegion<L> {
    private int maxX = 0;
    private int maxY = 0;
    private int maxZ = 0;
    private int minX = 0;
    private int minY = 0;
    private int minZ = 0;

    protected AbstractRegion(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
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

    public abstract boolean isInRegion(L location);

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("minX", minX);
        map.put("maxX", maxX);
        map.put("minY", minY);
        map.put("maxY", maxY);
        map.put("minZ", minZ);
        map.put("maxZ", maxZ);
        return map;
    }
}

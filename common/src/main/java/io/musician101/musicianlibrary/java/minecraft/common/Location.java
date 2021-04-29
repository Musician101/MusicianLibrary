package io.musician101.musicianlibrary.java.minecraft.common;

import io.musician101.musicianlibrary.java.storage.database.mongo.MongoSerializable;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.Document;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public class Location {

    @Nonnull
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;

    public Location(@Nonnull String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Nonnull
    public String getWorldName() {
        return worldName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public static final class Serializer implements MongoSerializable<Location>, TypeSerializer<Location> {

        @Override
        public Location deserialize(Type type, ConfigurationNode node) throws SerializationException {
            String worldName = node.node("world_name").getString();
            if (worldName == null) {
                throw new SerializationException("World name can not be null.");
            }

            double x = node.node("x").getDouble();
            double y = node.node("y").getDouble();
            double z = node.node("z").getDouble();
            return new Location(worldName, x, y, z);
        }

        @Override
        public Location deserialize(@Nullable Document document) {
            if (document == null) {
                return null;
            }

            String worldName = document.getString("world_name");
            if (worldName == null) {
                throw new IllegalStateException("World name can not be null.");
            }

            double x = document.getDouble("x");
            double y = document.getDouble("y");
            double z = document.getDouble("z");
            return new Location(worldName, x, y, z);
        }

        @Override
        public Document serialize(@Nonnull Location src) {
            Document document = new Document();
            document.put("name", src.worldName);
            document.put("x", src.x);
            document.put("y", src.y);
            document.put("z", src.z);
            return document;
        }

        @Override
        public void serialize(Type type, @Nullable Location obj, ConfigurationNode node) throws SerializationException {
            if (obj == null) {
                return;
            }

            node.node("name").set(obj.worldName);
            node.node("x").set(obj.x);
            node.node("y").set(obj.y);
            node.node("z").set(obj.z);
        }
    }
}

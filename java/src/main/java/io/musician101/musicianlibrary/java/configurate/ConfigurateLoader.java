package io.musician101.musicianlibrary.java.configurate;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@FunctionalInterface
public interface ConfigurateLoader {

    ConfigurateLoader HOCON = (path, typeSerializerCollection) -> HoconConfigurationLoader.builder().source(() -> Files.newBufferedReader(path, StandardCharsets.UTF_8)).sink(() -> Files.newBufferedWriter(path, StandardCharsets.UTF_8)).defaultOptions(opts -> opts.serializers(build -> {
        if (typeSerializerCollection != null) {
            build.registerAll(typeSerializerCollection);
        }
    })).build();
    ConfigurateLoader JSON = (path, typeSerializerCollection) -> GsonConfigurationLoader.builder().indent(2).source(() -> Files.newBufferedReader(path, StandardCharsets.UTF_8)).sink(() -> Files.newBufferedWriter(path, StandardCharsets.UTF_8)).defaultOptions(opts -> opts.serializers(build -> {
        if (typeSerializerCollection != null) {
            build.registerAll(typeSerializerCollection);
        }
    })).build();
    ConfigurateLoader YAML = (path, typeSerializerCollection) -> YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).indent(2).source(() -> Files.newBufferedReader(path, StandardCharsets.UTF_8)).sink(() -> Files.newBufferedWriter(path, StandardCharsets.UTF_8)).defaultOptions(opts -> opts.serializers(build -> {
        if (typeSerializerCollection != null) {
            build.registerAll(typeSerializerCollection);
        }
    })).build();

    default ConfigurationLoader<? extends ConfigurationNode> loader(@Nonnull Path path) {
        return loader(path, null);
    }

    ConfigurationLoader<? extends ConfigurationNode> loader(@Nonnull Path path, @Nullable TypeSerializerCollection typeSerializerCollection);
}

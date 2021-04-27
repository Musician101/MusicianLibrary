package io.musician101.musicianlibrary.java.storage.database;

import io.musician101.musicianlibrary.java.storage.DataStorage;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;
import javax.annotation.Nonnull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

public abstract class DatabaseStorage<V> extends DataStorage<String, V> {

    @Nonnull
    protected final String address;
    @Nonnull
    protected final String database;
    @Nonnull
    protected final String password;
    @Nonnull
    protected final String username;

    public DatabaseStorage(@Nonnull Map<String, String> options) {
        this.address = options.get("address");
        this.database = options.get("database");
        this.username = options.get("username");
        this.password = options.get("password");
    }

    protected ConfigurationNode read(String string) throws ConfigurateException {
        return HoconConfigurationLoader.builder().source(() -> new BufferedReader(new StringReader(string))).build().load();
    }

    protected String write(ConfigurationNode configurationNode) {
        return configurationNode.toString();
    }
}

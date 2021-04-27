package io.musician101.musicianlibrary.java.minecraft.spigot.plugin;

import io.musician101.musicianlibrary.java.minecraft.common.config.AbstractConfig;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractSpigotPlugin<C extends AbstractConfig> extends JavaPlugin {

    protected C config;

    public C getPluginConfig() {
        return config;
    }
}

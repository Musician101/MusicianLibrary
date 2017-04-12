package io.musician101.musicianlibrary.java.minecraft.spigot.plugin;

import io.musician101.musicianlibrary.java.minecraft.config.AbstractConfig;
import io.musician101.musicianlibrary.java.minecraft.spigot.command.SpigotCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractSpigotPlugin<C extends AbstractConfig, P extends JavaPlugin> extends JavaPlugin {

    protected final List<SpigotCommand<P>> commands = new ArrayList<>();
    protected C config;

    public List<SpigotCommand<P>> getCommands() {
        return commands;
    }

    public C getPluginConfig() {
        return config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String... args) {
        for (SpigotCommand<P> cmd : commands) {
            if (command.getName().equalsIgnoreCase(cmd.getName())) {
                return cmd.execute(sender, Arrays.asList(args));
            }
        }

        return false;
    }
}

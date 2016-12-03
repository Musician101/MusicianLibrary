package io.musician101.musicianlibrary.java.minecraft.spigot;

import io.musician101.musicianlibrary.java.minecraft.AbstractConfig;
import io.musician101.musicianlibrary.java.minecraft.spigot.command.AbstractSpigotCommand;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AbstractSpigotPlugin<C extends AbstractConfig> extends JavaPlugin {
    protected List<AbstractSpigotCommand> commands;
    protected C config;

    public List<AbstractSpigotCommand> getCommands() {
        return commands;
    }

    public C getPluginConfig() {
        return config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String... args) {
        for (AbstractSpigotCommand cmd : commands)
            if (command.getName().equalsIgnoreCase(cmd.getName()))
                return cmd.onCommand(sender, args);

        return false;
    }
}

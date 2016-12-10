package io.musician101.musicianlibrary.java.minecraft.spigot;

import io.musician101.musicianlibrary.java.minecraft.AbstractConfig;
import io.musician101.musicianlibrary.java.minecraft.command.MLCommandResult;
import io.musician101.musicianlibrary.java.minecraft.spigot.command.SpigotCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractSpigotPlugin<C extends AbstractConfig> extends JavaPlugin {

    protected final List<SpigotCommand> commands = new ArrayList<>();
    protected C config;

    public List<SpigotCommand> getCommands() {
        return commands;
    }

    public C getPluginConfig() {
        return config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String... args) {
        for (SpigotCommand cmd : commands) {
            if (command.getName().equalsIgnoreCase(cmd.getName())) {
                MLCommandResult result = cmd.execute(sender, Arrays.asList(args));
                if (result == MLCommandResult.SUCCESS)
                    return true;
                else if (result == MLCommandResult.NOT_ENOUGH_ARGUMENTS)
                    sender.sendMessage("[" + getDescription().getPrefix() + "] Not enough arguments");
            }
        }

        return false;
    }
}

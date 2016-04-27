package io.musician101.common.java.minecraft.spigot.command;

import java.util.Arrays;

import io.musician101.common.java.minecraft.spigot.AbstractSpigotPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SpigotHelpCommand<P extends AbstractSpigotPlugin> extends AbstractSpigotCommand<P>
{
    private final AbstractSpigotCommand<P> mainCommand;

    public SpigotHelpCommand(P plugin, AbstractSpigotCommand<P> mainCommand)
    {
        super(plugin, "help", "Display help info for " + ChatColor.stripColor(mainCommand.getUsage().getUsage()), new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(ChatColor.stripColor(mainCommand.getUsage().getUsage())), new SpigotCommandArgument("help")), 1), new SpigotCommandPermissions("", false, "", ""));
        this.mainCommand = mainCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + plugin.getName() + " v" + plugin.getDescription().getVersion() + ChatColor.GREEN + " =====");
        sender.sendMessage(mainCommand.getCommandHelpInfo());
        mainCommand.getSubCommands().forEach(command -> sender.sendMessage(command.getCommandHelpInfo()));
        return true;
    }
}

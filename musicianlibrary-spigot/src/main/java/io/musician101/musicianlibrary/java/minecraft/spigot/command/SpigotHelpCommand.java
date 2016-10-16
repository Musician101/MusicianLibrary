package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@SuppressWarnings("unused")
public class SpigotHelpCommand<P extends JavaPlugin> extends AbstractSpigotCommand
{
    private final AbstractSpigotCommand mainCommand;
    private final P plugin;

    public SpigotHelpCommand(P plugin, AbstractSpigotCommand mainCommand)
    {
        super("help", "Display help info for " + ChatColor.stripColor(mainCommand.getUsage().getUsage()), new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(ChatColor.stripColor(mainCommand.getUsage().getUsage())), new SpigotCommandArgument("help")), 1), new SpigotCommandPermissions("", false, "", ""));
        this.plugin = plugin;
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

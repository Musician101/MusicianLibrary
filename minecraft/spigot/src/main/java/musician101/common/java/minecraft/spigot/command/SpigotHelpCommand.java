package musician101.common.java.minecraft.spigot.command;

import java.util.Arrays;

import musician101.common.java.minecraft.spigot.AbstractSpigotPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotHelpCommand<Plugin extends AbstractSpigotPlugin> extends AbstractSpigotCommand<Plugin>
{
    private final AbstractSpigotCommand<Plugin> mainCommand;

    public SpigotHelpCommand(Plugin plugin, AbstractSpigotCommand<Plugin> mainCommand)
    {
        super(plugin, "help", "Display help info for " + ChatColor.stripColor(mainCommand.getUsage()), Arrays.asList(new SpigotCommandArgument(ChatColor.stripColor(mainCommand.getUsage())), new SpigotCommandArgument("help")), 1, "", false, "", "");
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

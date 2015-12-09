package musician101.common.java.minecraft.spigot.command;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SpigotHelpCommand extends AbstractSpigotCommand
{
    private final AbstractSpigotCommand mainCommand;

    public SpigotHelpCommand(AbstractSpigotCommand mainCommand)
    {
        super("help", "Display help info for " + ChatColor.stripColor(mainCommand.getUsage()), Arrays.asList(new SpigotCommandArgument(ChatColor.stripColor(mainCommand.getUsage())), new SpigotCommandArgument("help")), 1, "", false, "", "");
        this.mainCommand = mainCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        sender.sendMessage(mainCommand.getCommandHelpInfo());
        mainCommand.getSubCommands().forEach(command -> sender.sendMessage(command.getCommandHelpInfo()));
        return true;
    }
}

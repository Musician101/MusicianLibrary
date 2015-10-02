package musician101.common.java.minecraft.spigot;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractSpigotCommand
{
    boolean isPlayerOnly;
    int minArgs;
    List<AbstractSpigotCommand> subCommands;
    String description;
    String name;
    String noPermission;
    String permission;
    String playerOnly;
    String usage;

    protected AbstractSpigotCommand(String name, String description, String usage, int minArgs, String permission, boolean isPlayerOnly, String noPermission, String playerOnly)
    {
        this(name, description, usage, minArgs, permission, isPlayerOnly, noPermission, playerOnly, new ArrayList<>());
    }

	protected AbstractSpigotCommand(String name, String description, String usage, int minArgs, String permission, boolean isPlayerOnly, String noPermission, String playerOnly, List<AbstractSpigotCommand> subCommands)
	{
		this.name = name;
        this.description = description;
        this.usage = usage;
        this.minArgs = minArgs;
        this.isPlayerOnly = isPlayerOnly;
		this.permission = permission;
        this.noPermission = noPermission;
        this.playerOnly = playerOnly;
        this.subCommands = subCommands;
	}

    private static String parseUsage(List<String> usageList)
    {
        String usage = ChatColor.GRAY + usageList.get(0);
        if (usageList.size() > 1)
            usage += " " + ChatColor.RESET + usageList.get(1);

        if (usageList.size() > 2)
            usage += " " + ChatColor.AQUA + usageList.get(2);

        return usage;
    }

    public abstract boolean onCommand(CommandSender sender, String... args);

    protected boolean canSenderUseCommand(CommandSender sender)
    {
        if (isPlayerOnly() && !(sender instanceof Player))
        {
            sender.sendMessage(playerOnly);
            return false;
        }

        if (!sender.hasPermission(permission))
        {
            sender.sendMessage(noPermission);
            return false;
        }

        return true;
    }

    public boolean isPlayerOnly()
    {
        return isPlayerOnly;
    }

    public int getMinArgs()
    {
        return minArgs;
    }

    protected List<AbstractSpigotCommand> getSubCommands()
    {
        return subCommands;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public String getUsage()
    {
        return usage;
    }

    public String getCommandHelpInfo()
    {
        return getUsage()  + " " + ChatColor.GREEN + getDescription();
    }

	public String getPermission()
	{
		return permission;
	}

    protected String[] moveArguments(String[] args)
    {
        List<String> list = Arrays.asList(args);
        if (list.size() > 0)
            list.remove(0);

        return list.toArray(new String[list.size()]);
    }
}

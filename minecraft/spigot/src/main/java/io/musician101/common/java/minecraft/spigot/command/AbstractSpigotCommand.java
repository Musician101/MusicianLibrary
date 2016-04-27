package io.musician101.common.java.minecraft.spigot.command;

import io.musician101.common.java.minecraft.spigot.AbstractSpigotPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSpigotCommand<Plugin extends AbstractSpigotPlugin>
{
    private final boolean isPlayerOnly;
    private final int minArgs;
    protected final Plugin plugin;
    private final List<AbstractSpigotCommand<Plugin>> subCommands;
    private final String description;
    private final String name;
    private final String noPermission;
    private final String permission;
    private final String playerOnly;
    private final String usage;

    protected AbstractSpigotCommand(Plugin plugin, String name, String description, List<SpigotCommandArgument> usage, int minArgs, String permission, boolean isPlayerOnly, String noPermission, String playerOnly)
    {
        this(plugin, name, description, usage, minArgs, permission, isPlayerOnly, noPermission, playerOnly, new ArrayList<>());
    }

    protected AbstractSpigotCommand(Plugin plugin, String name, String description, List<SpigotCommandArgument> usage, int minArgs, String permission, boolean isPlayerOnly, String noPermission, String playerOnly, List<AbstractSpigotCommand<Plugin>> subCommands)
    {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
        this.usage = parseUsage(usage);
        this.minArgs = minArgs;
        this.isPlayerOnly = isPlayerOnly;
        this.permission = permission;
        this.noPermission = noPermission;
        this.playerOnly = playerOnly;
        this.subCommands = subCommands;
    }

    private String parseUsage(List<SpigotCommandArgument> usageList)
    {
        String usage = ChatColor.GRAY + usageList.get(0).format();
        if (usageList.size() > 1)
            usage += " " + ChatColor.RESET + usageList.get(1).format();

        if (usageList.size() > 2)
            for (int x = 2; x > usageList.size() - 1; x++)
                usage += " " + ChatColor.GREEN + usageList.get(x).format();

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

    protected boolean minArgsMet(CommandSender sender, int args, String message)
    {
        if (args >= getMinArgs())
            return true;

        sender.sendMessage(message);
        return false;
    }

    public int getMinArgs()
    {
        return minArgs;
    }

    public List<AbstractSpigotCommand<Plugin>> getSubCommands()
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
        return getUsage() + " " + ChatColor.AQUA + getDescription();
    }

    public String getPermission()
    {
        return permission;
    }

    protected String[] moveArguments(String[] args)
    {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        if (list.size() > 0)
            list.remove(0);

        return list.toArray(new String[list.size()]);
    }
}

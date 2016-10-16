package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AbstractSpigotCommand extends AbstractCommand<String, SpigotCommandUsage, SpigotCommandPermissions, AbstractSpigotCommand, CommandSender>
{
    protected AbstractSpigotCommand(String name, String description, SpigotCommandUsage usage, SpigotCommandPermissions permissions)
    {
        this(name, description, usage, permissions, new ArrayList<>());
    }

    protected AbstractSpigotCommand(String name, String description, SpigotCommandUsage usage, SpigotCommandPermissions permissions, List<AbstractSpigotCommand> subCommands)
    {
        super(name, description, usage, permissions, subCommands);
    }

    @SuppressWarnings("SameReturnValue")
    public abstract boolean onCommand(CommandSender sender, String... args);

    protected boolean canSenderUseCommand(CommandSender sender)
    {
        if (permissions.isPlayerOnly() && !(sender instanceof Player))
        {
            sender.sendMessage(permissions.getPlayerOnlyMessage());
            return false;
        }

        if (!sender.hasPermission(permissions.getPermissionNode()))
        {
            sender.sendMessage(permissions.getNoPermissionMessage());
            return false;
        }

        return true;
    }

    @Override
    protected boolean minArgsMet(CommandSender sender, int args, String message)
    {
        if (args >= usage.getMinArgs())
            return true;

        sender.sendMessage(message);
        return false;
    }

    public String getName()
    {
        return name;
    }

    public String getCommandHelpInfo()
    {
        return usage.getUsage() + " " + ChatColor.AQUA + description;
    }

    protected String[] moveArguments(String[] args)
    {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        if (!list.isEmpty())
            list.remove(0);

        return list.toArray(new String[list.size()]);
    }
}

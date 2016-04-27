package io.musician101.common.java.minecraft.spigot.command;

import io.musician101.common.java.minecraft.command.AbstractCommand;
import io.musician101.common.java.minecraft.spigot.AbstractSpigotPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSpigotCommand<P extends AbstractSpigotPlugin> extends AbstractCommand<P, String, SpigotCommandUsage, SpigotCommandArgument, SpigotCommandPermissions, AbstractSpigotCommand<P>, CommandSender>
{
    protected AbstractSpigotCommand(P plugin, String name, String description, SpigotCommandUsage usage, SpigotCommandPermissions permissions)
    {
        this(plugin, name, description, usage, permissions, new ArrayList<>());
    }

    protected AbstractSpigotCommand(P plugin, String name, String description, SpigotCommandUsage usage, SpigotCommandPermissions permissions, List<AbstractSpigotCommand<P>> subCommands)
    {
        super(plugin, name, description, usage, permissions, subCommands);
    }

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

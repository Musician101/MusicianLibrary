package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandUsage;
import org.bukkit.ChatColor;

import java.util.List;

public class SpigotCommandUsage extends AbstractCommandUsage<String, SpigotCommandArgument>
{
    public SpigotCommandUsage(SpigotCommandArgument... arguments)
    {
        super(arguments);
    }

    public SpigotCommandUsage(int minArgs, SpigotCommandArgument... arguments)
    {
        super(minArgs, arguments);
    }

    @Override
    protected String parseUsage(List<SpigotCommandArgument> arguments)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.GRAY).append(arguments.get(0).format());
        if (arguments.size() > 1)
            sb.append(" ").append(ChatColor.RESET).append(arguments.get(1).format());

        if (arguments.size() > 2)
            for (int x = 2; x < arguments.size() - 1; x++)
                sb.append(" ").append(ChatColor.GREEN).append(arguments.get(x).format());

        return sb.toString();
    }
}

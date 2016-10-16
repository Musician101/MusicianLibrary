package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandUsage;
import org.bukkit.ChatColor;

import java.util.List;

@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
public class SpigotCommandUsage extends AbstractCommandUsage<String, SpigotCommandArgument>
{
    @SuppressWarnings("unused")
    public SpigotCommandUsage(List<SpigotCommandArgument> arguments)
    {
        super(arguments);
    }

    public SpigotCommandUsage(List<SpigotCommandArgument> arguments, int minArgs)
    {
        super(arguments, minArgs);
    }

    @Override
    protected String parseUsage(List<SpigotCommandArgument> arguments)
    {
        String usageString = ChatColor.GRAY + arguments.get(0).format();
        if (arguments.size() > 1)
            usageString += " " + ChatColor.RESET + arguments.get(1).format();

        if (arguments.size() > 2)
            for (int x = 2; x < arguments.size() - 1; x++)
                usageString += " " + ChatColor.GREEN + arguments.get(x).format();

        return usageString;
    }
}

package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandArgument;
import org.bukkit.ChatColor;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SpigotCommandArgument extends AbstractCommandArgument<String>
{
    public SpigotCommandArgument(String name)
    {
        super(name);
    }

    @SuppressWarnings("unused")
    public SpigotCommandArgument(String name, Syntax... syntaxes)
    {
        super(name, syntaxes);
    }

    @Override
    public String format()
    {
        String name = this.name;
        if (syntaxList.contains(Syntax.MULTIPLE))
            name = name + "...";

        if (syntaxList.contains(Syntax.REPLACE))
            name = ChatColor.ITALIC + name;

        if (syntaxList.contains(Syntax.OPTIONAL))
            name = "[" + name + "]";

        if (syntaxList.contains(Syntax.REQUIRED))
            name = "<" + name + ">";

        return name;
    }
}

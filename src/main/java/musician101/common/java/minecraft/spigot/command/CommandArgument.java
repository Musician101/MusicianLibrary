package musician101.common.java.minecraft.spigot.command;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class CommandArgument
{
    private String name;
    private final List<Syntax> syntaxes;

    public CommandArgument(AbstractSpigotCommand command)
    {
        this(command.getName());
    }

    public CommandArgument(AbstractSpigotCommand command, Syntax... syntaxes)
    {
        this(command.getName(), syntaxes);
    }

    public CommandArgument(String name)
    {
        this(name, Syntax.LITERAL);
    }

    public CommandArgument(String name, Syntax... syntaxes)
    {
        this.syntaxes = Arrays.asList(syntaxes);
        if (this.syntaxes.contains(Syntax.REQUIRED) && this.syntaxes.contains(Syntax.OPTIONAL))
            throw new IllegalArgumentException("Command arguments cannot be both Optional and Required.");

        this.name = name;
    }

    @Override
    public String toString()
    {
        if (syntaxes.contains(Syntax.MULTIPLE))
            name = name + "...";

        if (syntaxes.contains(Syntax.REPLACE))
            name = ChatColor.ITALIC + name;

        if (syntaxes.contains(Syntax.OPTIONAL))
            name = "[" + name + "]";

        if (syntaxes.contains(Syntax.REQUIRED))
            name = "<" + name + ">";

        return name;
    }

    @SuppressWarnings("UnnecessaryEnumModifier")
    public static enum Syntax
    {
        LITERAL,
        MULTIPLE,
        REPLACE,
        REQUIRED,
        OPTIONAL
    }
}

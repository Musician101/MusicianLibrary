package io.musician101.musicianlibrary.java.minecraft.forge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandArgument;
import net.minecraft.util.text.TextFormatting;

public class ForgeCommandArgument extends AbstractCommandArgument<String>
{
    public ForgeCommandArgument(String name)
    {
        super(name);
    }

    public ForgeCommandArgument(String name, Syntax... syntaxes)
    {
        super(name, syntaxes);
    }

    @Override
    public String format()
    {
        String name = this.name;
        if (syntaxList.contains(Syntax.REPLACE))
            name = TextFormatting.ITALIC + name;

        if (syntaxList.contains(Syntax.MULTIPLE))
            name = name + "...";

        if (syntaxList.contains(Syntax.OPTIONAL))
            name = "[" + name + "]";

        if (syntaxList.contains(Syntax.REQUIRED))
            name = "<" + name + ">";

        return name;
    }
}

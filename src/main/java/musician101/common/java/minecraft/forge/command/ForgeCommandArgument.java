package musician101.common.java.minecraft.forge.command;

import musician101.common.java.minecraft.command.AbstractCommandArgument;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ForgeCommandArgument extends AbstractCommandArgument
{
    public ForgeCommandArgument(String name)
    {
        super(name);
    }

    public ForgeCommandArgument(String name, Syntax... syntaxes)
    {
        super(name, syntaxes);
    }

    public ChatComponentText format()
    {
        String name = this.name;
        if (syntaxList.contains(Syntax.MULTIPLE))
            name = name + "...";

        if (syntaxList.contains(Syntax.REPLACE))
            name = EnumChatFormatting.ITALIC + name;

        if (syntaxList.contains(Syntax.OPTIONAL))
            name = "[" + name + "]";

        if (syntaxList.contains(Syntax.REQUIRED))
            name = "<" + name + ">";

        return new ChatComponentText(name);
    }
}

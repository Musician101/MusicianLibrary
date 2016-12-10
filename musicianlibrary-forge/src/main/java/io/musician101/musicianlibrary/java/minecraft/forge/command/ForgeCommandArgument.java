package io.musician101.musicianlibrary.java.minecraft.forge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandArgument;
import net.minecraft.util.text.TextFormatting;

//TODO incomplete
public class ForgeCommandArgument extends AbstractCommandArgument<String> {

    ForgeCommandArgument(String name) {

    }

    @Override
    public String format() {
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

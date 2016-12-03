package io.musician101.musicianlibrary.java.minecraft.forge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandUsage;
import java.util.List;
import net.minecraft.util.text.TextFormatting;

public class ForgeCommandUsage extends AbstractCommandUsage<String, ForgeCommandArgument> {
    public ForgeCommandUsage(ForgeCommandArgument... arguments) {
        super(arguments);
    }

    public ForgeCommandUsage(int minArgs, ForgeCommandArgument... arguments) {
        super(minArgs, arguments);
    }

    @Override
    protected String parseUsage(List<ForgeCommandArgument> arguments) {
        StringBuilder sb = new StringBuilder();
        sb.append(TextFormatting.GRAY).append(arguments.get(0).format());
        if (arguments.size() > 1)
            sb.append(" ").append(TextFormatting.RESET).append(arguments.get(1).format());

        if (arguments.size() > 2)
            for (int x = 2; x < arguments.size() - 1; x++)
                sb.append(" ").append(TextFormatting.GREEN).append(arguments.get(x).format());

        return sb.toString();
    }
}

package io.musician101.musicianlibrary.java.minecraft.sponge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandUsage;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
public class SpongeCommandUsage extends AbstractCommandUsage<Text, SpongeCommandArgument>
{
    @SuppressWarnings("unused")
    public SpongeCommandUsage(List<SpongeCommandArgument> arguments)
    {
        super(arguments);
    }

    public SpongeCommandUsage(List<SpongeCommandArgument> arguments, int minArgs)
    {
        super(arguments, minArgs);
    }

    @Override
    protected Text parseUsage(List<SpongeCommandArgument> arguments)
    {
        List<Text> textList = new ArrayList<>();
        arguments.forEach(argument -> textList.add(argument.format()));
        return Text.joinWith(Text.of(" "), textList);
    }
}

package io.musician101.musicianlibrary.java.minecraft.sponge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandUsage;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.api.text.Text;

public class SpongeCommandUsage extends AbstractCommandUsage<Text, SpongeCommandArgument> {
    public SpongeCommandUsage(SpongeCommandArgument... arguments) {
        super(arguments);
    }

    public SpongeCommandUsage(int minArgs, SpongeCommandArgument... arguments) {
        super(minArgs, arguments);
    }

    @Override
    protected Text parseUsage(List<SpongeCommandArgument> arguments) {
        List<Text> textList = new ArrayList<>();
        arguments.forEach(argument -> textList.add(argument.format()));
        return Text.joinWith(Text.of(" "), textList);
    }
}

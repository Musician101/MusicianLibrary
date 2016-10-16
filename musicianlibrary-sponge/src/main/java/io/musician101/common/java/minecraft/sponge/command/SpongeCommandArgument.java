package io.musician101.common.java.minecraft.sponge.command;

import io.musician101.common.java.minecraft.command.AbstractCommandArgument;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

@SuppressWarnings("WeakerAccess")
public class SpongeCommandArgument extends AbstractCommandArgument<Text>
{
    public SpongeCommandArgument(String name)
    {
        super(name);
    }

    @SuppressWarnings("unused")
    public SpongeCommandArgument(String name, Syntax... syntaxes)
    {
        super(name, syntaxes);
    }

    @Override
    public Text format()
    {
        Text name = Text.of(this.name);
        if (syntaxList.contains(Syntax.MULTIPLE))
            name = Text.builder().append(name, Text.of("...")).build();

        if (syntaxList.contains(Syntax.REPLACE))
            name = Text.builder().append(name).style(TextStyles.ITALIC).build();

        if (syntaxList.contains(Syntax.OPTIONAL))
            name = Text.builder().append(Text.of("["), name, Text.of("]")).build();

        if (syntaxList.contains(Syntax.REQUIRED))
            name = Text.builder().append(Text.of("<"), name, Text.of(">")).build();

        return name;
    }
}

package musician101.common.java.minecraft.sponge.command;

import musician101.common.java.minecraft.command.AbstractCommandArgument;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextStyles;

public class SpongeCommandArgument extends AbstractCommandArgument
{
    public SpongeCommandArgument(String name)
    {
        super(name);
    }

    public SpongeCommandArgument(String name, Syntax... syntaxes)
    {
        super(name, syntaxes);
    }

    public Text format()
    {
        Text name = Texts.of(this.name);
        if (syntaxList.contains(Syntax.MULTIPLE))
            name = Texts.builder().append(name, Texts.of("...")).build();

        if (syntaxList.contains(Syntax.REPLACE))
            name = Texts.builder().append(name).style(TextStyles.ITALIC).build();

        if (syntaxList.contains(Syntax.OPTIONAL))
            name = Texts.builder().append(Texts.of("["), name, Texts.of("]")).build();

        if (syntaxList.contains(Syntax.REQUIRED))
            name = Texts.builder().append(Texts.of("<"), name, Texts.of(">")).build();

        return name;
    }
}

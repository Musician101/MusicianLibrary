package io.musician101.musicianlibrary.java.minecraft.sponge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandArgument;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;


public class SpongeCommandArgument extends AbstractCommandArgument<Text> {

    SpongeCommandArgument() {

    }

    public static SpongeCommandArgumentBuilder builder() {
        return new SpongeCommandArgumentBuilder();
    }

    public static SpongeCommandArgument of(String argument) {
        return builder().name(argument).build();
    }

    public static SpongeCommandArgument of(String argument, Syntax... syntaxes) {
        SpongeCommandArgumentBuilder builder = builder().name(argument);
        Stream.of(syntaxes).forEach(builder::addSyntax);
        return builder.build();
    }

    @Nonnull
    @Override
    public Text format() {
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

    public static class SpongeCommandArgumentBuilder extends AbstractCommandArgumentBuilder<SpongeCommandArgument, SpongeCommandArgumentBuilder, Text> {

        SpongeCommandArgumentBuilder() {

        }

        @Nonnull
        @Override
        public SpongeCommandArgumentBuilder addSyntax(@Nonnull Syntax syntax) {
            syntaxList.add(syntax);
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandArgument build() throws IllegalStateException {
            if (name == null)
                throw new IllegalStateException("Name has not been set.");

            if (syntaxList.isEmpty())
                syntaxList.add(Syntax.LITERAL);

            if (syntaxList.contains(Syntax.REQUIRED) && syntaxList.contains(Syntax.OPTIONAL))
                throw new IllegalStateException("Common arguments cannot be both Optional and Required.");

            SpongeCommandArgument argument = new SpongeCommandArgument();
            argument.setName(name);
            argument.setSyntaxList(syntaxList);
            return argument;
        }

        @Nonnull
        @Override
        public SpongeCommandArgumentBuilder reset() {
            name = null;
            syntaxList = new ArrayList<>();
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandArgumentBuilder name(@Nonnull String name) {
            this.name = name;
            return this;
        }
    }
}

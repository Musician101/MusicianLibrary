package io.musician101.musicianlibrary.java.minecraft.sponge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.spongepowered.api.text.Text;

public class SpongeCommandUsage extends AbstractCommandUsage<SpongeCommandArgument, Text> {

    SpongeCommandUsage() {

    }

    public static SpongeCommandUsageBuilder builder() {
        return new SpongeCommandUsageBuilder();
    }

    public static SpongeCommandUsage of(SpongeCommandArgument... arguments) {
        SpongeCommandUsageBuilder builder = builder();
        Stream.of(arguments).forEach(builder::addArgument);
        return builder.build();
    }

    public static SpongeCommandUsage of(SpongeCommandArgument argument) {
        return builder().addArgument(argument).build();
    }

    public static class SpongeCommandUsageBuilder extends AbstractCommandUsageBuilder<SpongeCommandArgument, SpongeCommandUsageBuilder, Text, SpongeCommandUsage> {

        SpongeCommandUsageBuilder() {

        }

        @Nonnull
        @Override
        public SpongeCommandUsageBuilder addArgument(@Nonnull SpongeCommandArgument argument) {
            usage.add(argument);
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandUsage build() throws IllegalStateException {
            if (usage.isEmpty())
                throw new IllegalStateException("Usage can not be empty.");

            SpongeCommandUsage scu = new SpongeCommandUsage();
            setMinArgs(minArgs);
            scu.setUsage(parseUsage(usage));
            return scu;
        }

        @Nonnull
        @Override
        protected Text parseUsage(@Nonnull List<SpongeCommandArgument> arguments) {
            List<Text> textList = new ArrayList<>();
            arguments.forEach(argument -> textList.add(argument.format()));
            return Text.joinWith(Text.of(" "), textList);
        }

        @Nonnull
        @Override
        public SpongeCommandUsageBuilder reset() {
            minArgs = 0;
            usage = new ArrayList<>();
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandUsageBuilder setMinArgs(int minArgs) {
            this.minArgs = minArgs;
            return this;
        }
    }
}

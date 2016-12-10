package io.musician101.musicianlibrary.java.minecraft.forge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandUsage;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.util.text.TextFormatting;

public class ForgeCommandUsage extends AbstractCommandUsage<ForgeCommandArgument, String> {

    ForgeCommandUsage() {

    }

    public static ForgeCommandUsageBuilder builder() {
        return new ForgeCommandUsageBuilder();
    }

    public static ForgeCommandUsage of(ForgeCommandArgument... arguments) {
        ForgeCommandUsageBuilder builder = builder();
        Stream.of(arguments).forEach(builder::addArgument);
        return builder.build();
    }

    //TODO incomplete
    public static class ForgeCommandUsageBuilder extends AbstractCommandUsageBuilder<ForgeCommandArgument, ForgeCommandUsageBuilder, String, ForgeCommandUsage> {

        ForgeCommandUsageBuilder() {

        }

        @Nonnull
        @Override
        public ForgeCommandUsageBuilder addArgument(@Nonnull ForgeCommandArgument argument) {
            return null;
        }

        @Nonnull
        @Override
        public ForgeCommandUsage build() throws IllegalStateException {
            return null;
        }

        @Nonnull
        @Override
        protected String parseUsage(@Nonnull List<ForgeCommandArgument> arguments) {
            StringBuilder sb = new StringBuilder();
            sb.append(TextFormatting.GRAY).append(arguments.get(0).format());
            if (arguments.size() > 1)
                sb.append(" ").append(TextFormatting.RESET).append(arguments.get(1).format());

            if (arguments.size() > 2)
                for (int x = 2; x < arguments.size() - 1; x++)
                    sb.append(" ").append(TextFormatting.GREEN).append(arguments.get(x).format());

            return sb.toString();
        }

        @Nonnull
        @Override
        public ForgeCommandUsageBuilder reset() {
            return null;
        }

        @Nonnull
        @Override
        public ForgeCommandUsageBuilder minArgs(int minArgs) {
            return null;
        }
    }
}

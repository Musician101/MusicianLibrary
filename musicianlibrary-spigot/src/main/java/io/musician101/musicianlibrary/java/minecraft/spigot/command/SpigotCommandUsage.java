package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;

public class SpigotCommandUsage extends AbstractCommandUsage<SpigotCommandArgument, String> {

    SpigotCommandUsage() {

    }

    public static SpigotCommandUsageBuilder builder() {
        return new SpigotCommandUsageBuilder();
    }

    public static SpigotCommandUsage of(SpigotCommandArgument argument) {
        return builder().addArgument(argument).build();
    }

    public static SpigotCommandUsage of(SpigotCommandArgument... arguments) {
        SpigotCommandUsageBuilder builder = builder();
        Stream.of(arguments).forEach(builder::addArgument);
        return builder.build();
    }

    public static class SpigotCommandUsageBuilder extends AbstractCommandUsageBuilder<SpigotCommandArgument, SpigotCommandUsageBuilder, String, SpigotCommandUsage> {

        SpigotCommandUsageBuilder() {

        }

        @Nonnull
        @Override
        public SpigotCommandUsageBuilder addArgument(@Nonnull SpigotCommandArgument argument) {
            usage.add(argument);
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandUsage build() throws IllegalStateException {
            if (usage.isEmpty())
                throw new IllegalStateException("Usage can not be empty.");

            SpigotCommandUsage scu = new SpigotCommandUsage();
            scu.setMinArgs(minArgs);
            scu.setUsage(parseUsage(usage));
            return scu;
        }

        @Nonnull
        @Override
        protected String parseUsage(@Nonnull List<SpigotCommandArgument> arguments) {
            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.GRAY).append(arguments.get(0).format());
            if (arguments.size() > 1)
                sb.append(" ").append(ChatColor.RESET).append(arguments.get(1).format());

            if (arguments.size() > 2)
                for (int x = 2; x < arguments.size() - 1; x++)
                    sb.append(" ").append(ChatColor.GREEN).append(arguments.get(x).format());

            return sb.toString();
        }

        @Nonnull
        @Override
        public SpigotCommandUsageBuilder reset() {
            minArgs = 0;
            usage = new ArrayList<>();
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandUsageBuilder setMinArgs(int minArgs) {
            this.minArgs = minArgs;
            return this;
        }
    }
}

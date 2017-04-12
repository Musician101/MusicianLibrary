package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;

public class SpigotCommandUsage {

    private int minArgs;
    private String usage;

    private SpigotCommandUsage() {

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

    public int getMinArgs() {
        return minArgs;
    }

    protected void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public String getUsage() {
        return usage;
    }

    protected void setUsage(String usage) {
        this.usage = usage;
    }

    public static class SpigotCommandUsageBuilder {

        private int minArgs = 0;
        private List<SpigotCommandArgument> usage = new ArrayList<>();

        SpigotCommandUsageBuilder() {

        }

        @Nonnull
        public SpigotCommandUsageBuilder addArgument(@Nonnull SpigotCommandArgument argument) {
            usage.add(argument);
            return this;
        }

        @Nonnull
        public SpigotCommandUsage build() throws IllegalStateException {
            if (usage.isEmpty()) {
                throw new IllegalStateException("Usage can not be empty.");
            }

            SpigotCommandUsage scu = new SpigotCommandUsage();
            scu.setMinArgs(minArgs);
            scu.setUsage(parseUsage(usage));
            return scu;
        }

        @Nonnull
        public SpigotCommandUsageBuilder minArgs(int minArgs) {
            this.minArgs = minArgs;
            return this;
        }

        @Nonnull
        protected String parseUsage(@Nonnull List<SpigotCommandArgument> arguments) {
            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.GRAY).append(arguments.get(0).format());
            if (arguments.size() > 1) {
                sb.append(" ").append(ChatColor.RESET).append(arguments.get(1).format());
            }

            if (arguments.size() > 2) {
                for (int x = 2; x < arguments.size() - 1; x++) {
                    sb.append(" ").append(ChatColor.GREEN).append(arguments.get(x).format());
                }
            }

            return sb.toString();
        }

        @Nonnull
        public SpigotCommandUsageBuilder reset() {
            minArgs = 0;
            usage = new ArrayList<>();
            return this;
        }
    }
}

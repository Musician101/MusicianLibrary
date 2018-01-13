package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import java.util.stream.Stream;

public class SpigotCommandUsage {

    private final int minArgs;
    private final String usage;

    SpigotCommandUsage(int minArgs, String usage) {
        this.minArgs = minArgs;
        this.usage = usage;
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

    public String getUsage() {
        return usage;
    }
}

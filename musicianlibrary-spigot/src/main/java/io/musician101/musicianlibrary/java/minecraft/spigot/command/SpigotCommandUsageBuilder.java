package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.common.util.Builder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;

import static com.google.common.base.Preconditions.checkArgument;

public class SpigotCommandUsageBuilder implements Builder<SpigotCommandUsageBuilder, SpigotCommandUsage> {

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
    @Override
    public SpigotCommandUsage build() throws IllegalStateException {
        checkArgument(!usage.isEmpty(), "Usage can not be empty.");
        return new SpigotCommandUsage(minArgs, parseUsage(usage));
    }

    @Nonnull
    public SpigotCommandUsageBuilder minArgs(int minArgs) {
        this.minArgs = minArgs;
        return this;
    }

    private String parseUsage(@Nonnull List<SpigotCommandArgument> arguments) {
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
    @Override
    public SpigotCommandUsageBuilder reset() {
        minArgs = 0;
        usage = new ArrayList<>();
        return this;
    }
}

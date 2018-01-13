package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import static com.google.common.base.Preconditions.checkNotNull;

public class SpigotCommandBuilder<I extends JavaPlugin> {

    private BiFunction<CommandSender, List<String>, Boolean> biFunction = (sender, args) -> true;
    private Map<String, SpigotCommand<I>> subCommands = new HashMap<>();
    private SpigotCommandPermissions permissions;
    private SpigotCommandUsage usage;
    private String description;
    private String name;

    SpigotCommandBuilder() {

    }

    @Nonnull
    public SpigotCommandBuilder<I> addCommand(@Nonnull SpigotCommand<I> command) {
        subCommands.put(command.getName(), command);
        return this;
    }

    @Nonnull
    public SpigotCommand<I> build(@Nonnull I plugin) throws IllegalStateException {
        checkNotNull(permissions, "Permissions cannot be null.");
        checkNotNull(usage, "Usage cannot be null.");
        checkNotNull(description, "Description cannot be null.");
        checkNotNull(name, "Name cannot be null.");
        return new SpigotCommand<>(plugin, biFunction, subCommands, permissions, usage, description, name);
    }

    @Nonnull
    public SpigotCommandBuilder<I> description(@Nonnull String description) {
        this.description = description;
        return this;
    }

    @Nonnull
    public SpigotCommandBuilder<I> function(@Nonnull BiFunction<CommandSender, List<String>, Boolean> biFunction) {
        this.biFunction = biFunction;
        return this;
    }

    @Nonnull
    public SpigotCommandBuilder<I> name(@Nonnull String name) {
        this.name = name;
        return this;
    }

    @Nonnull
    public SpigotCommandBuilder<I> permissions(@Nonnull SpigotCommandPermissions permissions) {
        this.permissions = permissions;
        return this;
    }

    @Nonnull
    public SpigotCommandBuilder<I> reset() {
        biFunction = (sender, args) -> true;
        description = null;
        name = null;
        permissions = null;
        subCommands = new HashMap<>();
        usage = null;
        return this;
    }

    @Nonnull
    public SpigotCommandBuilder<I> usage(@Nonnull SpigotCommandUsage usage) {
        this.usage = usage;
        return this;
    }
}

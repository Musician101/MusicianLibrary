package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommand;
import io.musician101.musicianlibrary.java.minecraft.command.MLCommandResult;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotCommand<I extends JavaPlugin> extends AbstractCommand<SpigotCommandArgument, SpigotCommand<I>, I, String, SpigotCommandPermissions, CommandSender, SpigotCommandUsage> {

    private SpigotCommand() {

    }

    public static <I extends JavaPlugin> SpigotCommandBuilder<I> builder() {
        return new SpigotCommandBuilder<>();
    }

    @Nonnull
    @Override
    public String getHelp() {
        return getUsage() + " " + ChatColor.AQUA + getDescription();
    }

    @Nonnull
    @Override
    protected SpigotCommand<I> getHelpCommand(I plugin) {
        return SpigotCommand.<I>builder().name("help").description("Display help info for " + ChatColor.stripColor(getUsage()))
                .usage(SpigotCommandUsage.of(SpigotCommandArgument.of(ChatColor.stripColor(getUsage())), SpigotCommandArgument.of("help")))
                .permissions(SpigotCommandPermissions.blank())
                .function((sender, args) -> {
                    sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + plugin.getName() + " v" + plugin.getDescription().getVersion() + ChatColor.GREEN + " =====");
                    sender.sendMessage(getHelp());
                    getSubCommands().values().forEach(command -> sender.sendMessage(command.getHelp()));
                    return MLCommandResult.SUCCESS;
                }).build();
    }

    public static class SpigotCommandBuilder<I extends JavaPlugin> extends AbstractCommandBuilder<SpigotCommandArgument, SpigotCommandBuilder<I>, SpigotCommand<I>, I, String, SpigotCommandPermissions, CommandSender, SpigotCommandUsage> {

        SpigotCommandBuilder() {

        }

        @Nonnull
        @Override
        public SpigotCommandBuilder<I> addCommand(@Nonnull SpigotCommand<I> command) {
            subCommands.put(command.getName(), command);
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommand<I> build() throws IllegalStateException {
            SpigotCommand<I> sc = new SpigotCommand<>();
            if (description == null)
                throw new IllegalStateException("Description has not been set.");

            if (name == null)
                throw new IllegalStateException("Name has not been set.");

            if (permissions == null)
                throw new IllegalStateException("Permissions have not been set.");

            if (usage == null)
                throw new IllegalStateException("Usage has not been set.");

            sc.setDescription(description);
            sc.setName(name);
            sc.setPermissions(permissions);
            sc.setUsage(usage);
            sc.setSubCommands(subCommands);
            if (biFunction == null)
                sc.setBiFunction((sender, args) -> {
                    sender.sendMessage(sc.getHelp());
                    return MLCommandResult.SUCCESS;
                });
            else
                sc.setBiFunction(biFunction);

            return sc;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder<I> reset() {
            biFunction = (sender, args) -> MLCommandResult.SUCCESS;
            description = null;
            name = null;
            permissions = null;
            subCommands = new HashMap<>();
            usage = null;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder<I> function(@Nonnull BiFunction<CommandSender, List<String>, MLCommandResult> biFunction) {
            this.biFunction = biFunction;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder<I> description(@Nonnull String description) {
            this.description = description;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder<I> name(@Nonnull String name) {
            this.name = name;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder<I> permissions(@Nonnull SpigotCommandPermissions permissions) {
            this.permissions = permissions;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder<I> usage(@Nonnull SpigotCommandUsage usage) {
            this.usage = usage;
            return this;
        }
    }
}

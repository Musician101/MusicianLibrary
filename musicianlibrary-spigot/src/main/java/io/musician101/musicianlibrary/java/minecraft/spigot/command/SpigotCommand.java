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

public class SpigotCommand extends AbstractCommand<SpigotCommandArgument, SpigotCommand, String, SpigotCommandPermissions, CommandSender, SpigotCommandUsage> {

    private SpigotCommand() {

    }

    public static SpigotCommandBuilder builder() {
        return new SpigotCommandBuilder();
    }

    public static <P extends JavaPlugin> SpigotCommand getHelpCommand(@Nonnull P plugin, @Nonnull SpigotCommand mainCommand) {
        return builder().setName("help")
                .setDescription("Display help info for " + ChatColor.stripColor(mainCommand.getUsage()))
                .setUsage(SpigotCommandUsage.of(SpigotCommandArgument.of(ChatColor.stripColor(mainCommand.getUsage())), SpigotCommandArgument.of("help")))
                .setPermissions(SpigotCommandPermissions.builder().setPermissionNode("").setIsPlayerOnly(false).setNoPermissionMessage("").setPlayerOnlyMessage("").build())
                .setBiFunction((sender, args) -> {
                    sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + plugin.getName() + " v" + plugin.getDescription().getVersion() + ChatColor.GREEN + " =====");
                    sender.sendMessage(mainCommand.getHelp());
                    mainCommand.getSubCommands().values().forEach(command -> sender.sendMessage(command.getHelp()));
                    return MLCommandResult.SUCCESS;
                }).build();
    }

    @Nonnull
    @Override
    public String getHelp() {
        return getUsage() + " " + ChatColor.AQUA + getDescription();
    }

    public static class SpigotCommandBuilder extends AbstractCommandBuilder<SpigotCommandArgument, SpigotCommandBuilder, SpigotCommand, String, SpigotCommandPermissions, CommandSender, SpigotCommandUsage> {

        SpigotCommandBuilder() {

        }

        @Nonnull
        @Override
        public SpigotCommandBuilder addCommand(@Nonnull SpigotCommand command) {
            subCommands.put(command.getName(), command);
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommand build() throws IllegalStateException {
            SpigotCommand sc = new SpigotCommand();
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
            sc.setBiFunction(biFunction);
            return sc;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder reset() {
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
        public SpigotCommandBuilder setBiFunction(@Nonnull BiFunction<CommandSender, List<String>, MLCommandResult> biFunction) {
            this.biFunction = biFunction;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder setDescription(@Nonnull String description) {
            this.description = description;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder setName(@Nonnull String name) {
            this.name = name;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder setPermissions(@Nonnull SpigotCommandPermissions permissions) {
            this.permissions = permissions;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandBuilder setUsage(@Nonnull SpigotCommandUsage usage) {
            this.usage = usage;
            return this;
        }
    }
}

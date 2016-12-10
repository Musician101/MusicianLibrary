package io.musician101.musicianlibrary.java.minecraft.sponge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommand;
import io.musician101.musicianlibrary.java.minecraft.command.MLCommandResult;
import io.musician101.musicianlibrary.java.minecraft.sponge.TextUtils;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;


public class SpongeCommand extends AbstractCommand<SpongeCommandArgument, SpongeCommand, Text, SpongeCommandPermissions, CommandSource, SpongeCommandUsage> {

    SpongeCommand() {

    }

    public static SpongeCommandBuilder builder() {
        return new SpongeCommandBuilder();
    }

    public static SpongeCommand getHelpCommand(PluginContainer plugin, SpongeCommand mainCommand) {
        return builder().setName("help")
                .setDescription(Text.join(Text.of("Display help info for "), mainCommand.getUsage()))
                .setUsage(SpongeCommandUsage.of(SpongeCommandArgument.of(((LiteralText) mainCommand.getUsage()).getContent()), SpongeCommandArgument.of("help")))
                .setBiFunction((sender, args) -> {
                    sender.sendMessage(Text.join(TextUtils.greenText("===== "), Text.of(plugin.getName() + " v" + plugin.getVersion()), TextUtils.greenText(" =====")));
                    sender.sendMessage(mainCommand.getHelp());
                    mainCommand.getSubCommands().values().forEach(cmd -> sender.sendMessage(cmd.getHelp()));
                    return MLCommandResult.SUCCESS;
                })
                .build();
    }

    @Nonnull
    @Override
    public Text getHelp() {
        return Text.join(getUsage(), Text.of(" "), getDescription());
    }

    public static class SpongeCommandBuilder extends AbstractCommandBuilder<SpongeCommandArgument, SpongeCommandBuilder, SpongeCommand, Text, SpongeCommandPermissions, CommandSource, SpongeCommandUsage> {

        SpongeCommandBuilder() {

        }

        @Nonnull
        @Override
        public SpongeCommandBuilder addCommand(@Nonnull SpongeCommand command) {
            subCommands.put(command.getName(), command);
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommand build() throws IllegalStateException {
            SpongeCommand sc = new SpongeCommand();
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
        public SpongeCommandBuilder reset() {
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
        public SpongeCommandBuilder setBiFunction(@Nonnull BiFunction<CommandSource, List<String>, MLCommandResult> biFunction) {
            this.biFunction = biFunction;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandBuilder setDescription(@Nonnull Text description) {
            this.description = description;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandBuilder setName(@Nonnull String name) {
            this.name = name;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandBuilder setPermissions(@Nonnull SpongeCommandPermissions permissions) {
            this.permissions = permissions;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandBuilder setUsage(@Nonnull SpongeCommandUsage usage) {
            this.usage = usage;
            return this;
        }
    }
}

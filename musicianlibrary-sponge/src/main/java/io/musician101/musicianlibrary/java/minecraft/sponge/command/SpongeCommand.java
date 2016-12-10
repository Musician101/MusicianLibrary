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


public class SpongeCommand<I extends PluginContainer> extends AbstractCommand<SpongeCommandArgument, SpongeCommand<I>, I, Text, SpongeCommandPermissions, CommandSource, SpongeCommandUsage> {

    SpongeCommand() {

    }

    public static <I extends PluginContainer> SpongeCommandBuilder<I> builder() {
        return new SpongeCommandBuilder<>();
    }

    @Nonnull
    @Override
    public Text getHelp() {
        return Text.join(getUsage(), Text.of(" "), getDescription());
    }

    @Nonnull
    @Override
    protected SpongeCommand<I> getHelpCommand(I plugin) {
        return SpongeCommand.<I>builder().name("help")
                .description(Text.join(Text.of("Display help info for "), getUsage()))
                .usage(SpongeCommandUsage.of(SpongeCommandArgument.of(((LiteralText) getUsage()).getContent()), SpongeCommandArgument.of("help")))
                .function((sender, args) -> {
                    sender.sendMessage(Text.join(TextUtils.greenText("===== "), Text.of(plugin.getName() + " v" + plugin.getVersion()), TextUtils.greenText(" =====")));
                    sender.sendMessage(getHelp());
                    getSubCommands().values().forEach(cmd -> sender.sendMessage(cmd.getHelp()));
                    return MLCommandResult.SUCCESS;
                })
                .build();
    }

    public static class SpongeCommandBuilder<I extends PluginContainer> extends AbstractCommandBuilder<SpongeCommandArgument, SpongeCommandBuilder<I>, SpongeCommand<I>, I, Text, SpongeCommandPermissions, CommandSource, SpongeCommandUsage> {

        SpongeCommandBuilder() {

        }

        @Nonnull
        @Override
        public SpongeCommandBuilder<I> addCommand(@Nonnull SpongeCommand<I> command) {
            subCommands.put(command.getName(), command);
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommand<I> build() throws IllegalStateException {
            SpongeCommand<I> sc = new SpongeCommand<>();
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
        public SpongeCommandBuilder<I> reset() {
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
        public SpongeCommandBuilder<I> function(@Nonnull BiFunction<CommandSource, List<String>, MLCommandResult> biFunction) {
            this.biFunction = biFunction;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandBuilder<I> description(@Nonnull Text description) {
            this.description = description;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandBuilder<I> name(@Nonnull String name) {
            this.name = name;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandBuilder<I> permissions(@Nonnull SpongeCommandPermissions permissions) {
            this.permissions = permissions;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandBuilder<I> usage(@Nonnull SpongeCommandUsage usage) {
            this.usage = usage;
            return this;
        }
    }
}

package io.musician101.common.java.minecraft.sponge.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("unused")
public class SpongeHelpCommand extends AbstractSpongeCommand
{
    private final PluginContainer plugin;
    private final AbstractSpongeCommand mainCommand;

    public SpongeHelpCommand(AbstractSpongeCommand mainCommand, CommandSource source, PluginContainer plugin)
    {
        super("help", Text.join(Text.of("Display help info for "), mainCommand.getUsage(source)), new SpongeCommandUsage(Arrays.asList(new SpongeCommandArgument(((LiteralText) mainCommand.getUsage(source)).getContent()), new SpongeCommandArgument("help")), 1), new SpongeCommandPermissions("", false, null, null));
        this.mainCommand = mainCommand;
        this.plugin = plugin;
    }

    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments) throws CommandException
    {
        source.sendMessage(Text.join(Text.builder("===== ").color(TextColors.GREEN).build(), Text.of(plugin.getName() + " v" + plugin.getVersion()), Text.builder(" =====").color(TextColors.GREEN).build()));
        source.sendMessage(Text.of(mainCommand.getUsage(source), mainCommand.getShortDescription(source)));
        for (AbstractSpongeCommand command : subCommands)
        {
            Optional<? extends Text> textOptional = command.getHelp(source);
            if (textOptional.isPresent())
                source.sendMessage(textOptional.get());
        }

        return CommandResult.success();
    }
}

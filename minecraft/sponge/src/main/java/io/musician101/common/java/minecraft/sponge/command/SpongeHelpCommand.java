package io.musician101.common.java.minecraft.sponge.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Optional;

public class SpongeHelpCommand<P> extends AbstractSpongeCommand<P>
{
    private final AbstractSpongeCommand mainCommand;

    public SpongeHelpCommand(P plugin, AbstractSpongeCommand mainCommand, CommandSource source)
    {
        super(plugin, "help", Text.join(Text.of("Display help info for "), mainCommand.getUsage(source)), new SpongeCommandUsage(Arrays.asList(new SpongeCommandArgument(((LiteralText) mainCommand.getUsage(source)).getContent()), new SpongeCommandArgument("help")), 1), new SpongeCommandPermissions("", false, null, null));
        this.mainCommand = mainCommand;
    }

    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments) throws CommandException
    {
        source.sendMessage(Text.of(mainCommand.getUsage(source), mainCommand.getShortDescription(source)));
        for (AbstractSpongeCommand<P> command : subCommands)
        {
            Optional<? extends Text> textOptional = command.getHelp(source);
            if (textOptional.isPresent())
                source.sendMessage(textOptional.get());
        }

        return CommandResult.success();
    }
}

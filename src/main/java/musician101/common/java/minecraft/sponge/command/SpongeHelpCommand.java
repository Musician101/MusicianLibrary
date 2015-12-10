package musician101.common.java.minecraft.sponge.command;

import org.spongepowered.api.text.Text.Literal;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SpongeHelpCommand extends AbstractSpongeCommand
{
    private final AbstractSpongeCommand mainCommand;

    public SpongeHelpCommand(AbstractSpongeCommand mainCommand, CommandSource source)
    {
        super("help", Texts.builder("Display help info for ").append(mainCommand.getUsage(source)).build(), Arrays.asList(new SpongeCommandArgument(((Literal) mainCommand.getUsage(source)).getContent()), new SpongeCommandArgument("help")), 1, "", false, null, null);
        this.mainCommand = mainCommand;
    }

    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments) throws CommandException
    {
        source.sendMessage(Texts.of());
        source.sendMessage(mainCommand.getUsage(source));
        mainCommand.getSubCommands().forEach(command -> source.sendMessage(command.getHelp(source).get()));
        return CommandResult.success();
    }
}

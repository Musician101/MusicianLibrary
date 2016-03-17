package musician101.common.java.minecraft.sponge.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SpongeHelpCommand extends AbstractSpongeCommand
{
    private final AbstractSpongeCommand mainCommand;

    public SpongeHelpCommand(AbstractSpongeCommand mainCommand, CommandSource source)
    {
        super("help", "Display help info for " + mainCommand.getUsage(source), Arrays.asList(new SpongeCommandArgument(((LiteralText) mainCommand.getUsage(source)).getContent()), new SpongeCommandArgument("help")), 1, "", false, null, null);
        this.mainCommand = mainCommand;
    }

    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments) throws CommandException
    {
        source.sendMessage(Text.of(mainCommand.getUsage(source), mainCommand.getShortDescription(source)));
        mainCommand.getSubCommands().forEach(command -> source.sendMessage(command.getHelp(source).get()));
        return CommandResult.success();
    }
}

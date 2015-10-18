package musician101.common.java.minecraft.sponge.command;

import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import javax.annotation.Nonnull;
import java.util.Arrays;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SpongeHelpCommand extends AbstractSpongeCommand
{
    private final AbstractSpongeCommand mainCommand;

    public SpongeHelpCommand(AbstractSpongeCommand mainCommand, CommandSource source)
    {
        super("help", Texts.builder("Display help info for ").append(mainCommand.getUsage(source)).build(), Arrays.asList(mainCommand.getUsage(source), Texts.of("help")), 1, "", false, null, null);
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

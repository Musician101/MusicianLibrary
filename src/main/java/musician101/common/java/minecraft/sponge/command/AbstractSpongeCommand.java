package musician101.common.java.minecraft.sponge.command;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandSource;

public abstract class AbstractSpongeCommand implements CommandCallable
{
    boolean isPlayerOnly;
    int minArgs;
    List<AbstractSpongeCommand> subCommands;
    String name;
    String permission;
    Text description;
    Text noPermission;
    Text playerOnly;
    Text usage;

    @Override
    public boolean testPermission(@Nonnull CommandSource source)
    {
        if (isPlayerOnly && !(source instanceof Player))
        {
            source.sendMessage(playerOnly);
            return false;
        }

        if (!source.hasPermission(permission))
        {
            source.sendMessage(noPermission);
            return false;
        }

        return true;
    }

    @Nonnull
    @Override
    public Optional<? extends Text> getShortDescription(@Nonnull CommandSource source)
    {
        return Optional.of(description);
    }

    @Nonnull
    @Override
    public Optional<? extends Text> getHelp(@Nonnull CommandSource source)
    {
        return Optional.of(Texts.join(Texts.of(" "), new Text[]{usage, description}));
    }

    @Nonnull
    @Override
    public Text getUsage(@Nonnull CommandSource source)
    {
        return usage;
    }

    public int getMinArgs()
    {
        return minArgs;
    }

    public List<AbstractSpongeCommand> getSubCommands()
    {
        return subCommands;
    }

    public String getName()
    {
        return name;
    }

    public String getPermission()
    {
        return permission;
    }

    protected boolean minArgsMet(CommandSource source, int args, String message)
    {
        if (args >= getMinArgs())
            return true;

        source.sendMessage(Texts.of(message));
        return false;
    }

    protected String[] splitArgs(String arguments)
    {
        return arguments.split("\\s");
    }
}

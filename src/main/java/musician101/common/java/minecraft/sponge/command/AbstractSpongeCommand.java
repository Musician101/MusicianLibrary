package musician101.common.java.minecraft.sponge.command;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandSource;

public abstract class AbstractSpongeCommand implements CommandCallable
{
    boolean isPlayerOnly;
    int minArgs;
    List<AbstractSpongeCommand> subCommands;
    String name;
    String permission;
    String usage;
    Text description;
    Text noPermission;
    Text playerOnly;

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
        Texts.of();
        return Optional.of();
    }

    @Nonnull
    @Override
    public Text getUsage(@Nonnull CommandSource source)
    {
        return null;
    }
}

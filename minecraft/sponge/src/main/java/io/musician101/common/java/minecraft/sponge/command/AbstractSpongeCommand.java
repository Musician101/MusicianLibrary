package io.musician101.common.java.minecraft.sponge.command;

import io.musician101.common.java.minecraft.command.AbstractCommand;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractSpongeCommand<P> extends AbstractCommand<P, Text, SpongeCommandUsage, SpongeCommandArgument, SpongeCommandPermissions, AbstractSpongeCommand<P>, CommandSource> implements CommandCallable
{
    public AbstractSpongeCommand(P plugin, String name, Text description, SpongeCommandUsage usage, SpongeCommandPermissions permissions)
    {
        this(plugin, name, description, usage, permissions, new ArrayList<>());
    }

    public AbstractSpongeCommand(P plugin, String name, Text description, SpongeCommandUsage usage, SpongeCommandPermissions permissions, List<AbstractSpongeCommand<P>> subCommands)
    {
        super(plugin, name, description, usage, permissions, subCommands);
    }

    @Nonnull
    @Override
    public List<String> getSuggestions(@Nonnull CommandSource source, @Nonnull String arguments)
    {
        String[] args = splitArgs(arguments);
        List<String> list = new ArrayList<>();
        for (AbstractSpongeCommand command : subCommands)
        {
            if (args.length == 0)
                list.add(command.getName());
            if (command.getName().startsWith(args[args.length - 1]))
                list.add(command.getName());
        }

        return list;
    }

    @Override
    public boolean testPermission(@Nonnull CommandSource source)
    {
        if (permissions.isPlayerOnly() && !(source instanceof Player))
        {
            source.sendMessage(permissions.getPlayerOnlyMessage());
            return false;
        }

        if (!source.hasPermission(permissions.getPermissionNode()))
        {
            source.sendMessage(permissions.getNoPermissionMessage());
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
        return Optional.of(Text.joinWith(Text.of(" "), usage.getUsage(), description));
    }

    @Nonnull
    @Override
    public Text getUsage(@Nonnull CommandSource source)
    {
        return usage.getUsage();
    }

    public String getName()
    {
        return name;
    }

    protected boolean minArgsMet(CommandSource source, int args, Text message)
    {
        if (args >= usage.getMinArgs())
            return true;

        source.sendMessage(message);
        return false;
    }

    protected String[] splitArgs(String arguments)
    {
        return arguments.split("\\s");
    }

    protected String moveArguments(String[] arguments)
    {
        StringBuilder sb = new StringBuilder();
        for (String arg : arguments)
        {
            if (sb.length() == 0)
                sb.append(arg);
            else
                sb.append(" ").append(arg);
        }

        return sb.toString();
    }
}

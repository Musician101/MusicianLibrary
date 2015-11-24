package musician101.common.java.minecraft.sponge.command;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandSource;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractSpongeCommand implements CommandCallable
{
    private final boolean isPlayerOnly;
    private final int minArgs;
    private final List<AbstractSpongeCommand> subCommands;
    private final String name;
    private final String permission;
    private final Text description;
    private final Text noPermission;
    private final Text playerOnly;
    private final Text usage;

    public AbstractSpongeCommand(String name, Text description, List<SpongeCommandArgument> arguments, int minArgs, String permission, boolean isPlayerOnly, Text noPermission, Text playerOnly)
    {
        this(name, description, arguments, minArgs, permission, isPlayerOnly, noPermission, playerOnly, new ArrayList<>());
    }

    public AbstractSpongeCommand(String name, Text description, List<SpongeCommandArgument> arguments, int minArgs, String permission, boolean isPlayerOnly, Text noPermission, Text playerOnly, List<AbstractSpongeCommand> subCommands)
    {
        this.name = name;
        this.description = description;
        this.usage = parseUsage(arguments);
        this.minArgs = minArgs;
        this.permission = permission;
        this.isPlayerOnly = isPlayerOnly;
        this.noPermission = noPermission;
        this.playerOnly = playerOnly;
        this.subCommands = subCommands;
    }

    private Text parseUsage(List<SpongeCommandArgument> arguments)
    {
        List<Text> textList = new ArrayList<>();
        arguments.forEach(argument -> textList.add(argument.format()));
        return Texts.join(Texts.of(" "), textList.toArray(new Text[arguments.size()]));
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

    protected boolean minArgsMet(CommandSource source, int args, Text message)
    {
        if (args >= getMinArgs())
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
package io.musician101.musicianlibrary.java.minecraft.sponge.command;

import io.musician101.musicianlibrary.java.minecraft.command.MLCommandResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class MLCommandCallable<P extends PluginContainer> implements CommandCallable {

    private final SpongeCommand<P> command;
    private final P plugin;

    public MLCommandCallable(P plugin, SpongeCommand<P> command) {
        this.plugin = plugin;
        this.command = command;
    }

    @Nonnull
    @Override
    public Optional<Text> getHelp(@Nonnull CommandSource source) {
        return Optional.of(command.getHelp());
    }

    @Nonnull
    @Override
    public Optional<Text> getShortDescription(@Nonnull CommandSource source) {
        return Optional.of(command.getDescription());
    }

    @Nonnull
    @Override
    public List<String> getSuggestions(@Nonnull CommandSource source, @Nonnull String arguments, @Nullable Location<World> targetPosition) throws CommandException {
        List<String> args = splitArgs(arguments);
        List<String> list = new ArrayList<>();
        for (SpongeCommand cmd : command.getSubCommands().values()) {
            if (args.isEmpty())
                list.add(cmd.getName());

            if (cmd.getName().startsWith(args.get(args.size() - 1)))
                list.add(cmd.getName());
        }

        return list;
    }

    @Nonnull
    @Override
    public Text getUsage(@Nonnull CommandSource source) {
        return command.getUsage();
    }

    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments) throws CommandException {
        for (SpongeCommand<P> cmd : command.getSubCommands().values()) {
            if (command.getName().equalsIgnoreCase(cmd.getName())) {
                MLCommandResult result = cmd.execute(source, splitArgs(arguments));
                if (result == MLCommandResult.SUCCESS)
                    return CommandResult.success();
                else if (result == MLCommandResult.NOT_ENOUGH_ARGUMENTS)
                    source.sendMessage(Text.of("[" + plugin.getName() + "] Not enough arguments"));
            }
        }

        return CommandResult.empty();
    }

    private List<String> splitArgs(String arguments) {
        return Arrays.asList(arguments.split("\\s"));
    }

    @Override
    public boolean testPermission(@Nonnull CommandSource source) {
        return false;
    }
}
